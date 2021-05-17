package com.azizi.post.service;

import com.azizi.common.constants.RabbitMqRoutingKeyConstants;
import com.azizi.common.payload.PostPayload;
import com.azizi.post.document.ElasticPost;
import com.azizi.post.dto.PageDto;
import com.azizi.post.dto.PostDto;
import com.azizi.post.entity.Post;
import com.azizi.post.exception.PostNotFoundException;
import com.azizi.post.mapper.ElasticPostMapper;
import com.azizi.post.mapper.PostMapper;
import com.azizi.post.producer.RabbitMqMessageSender;
import com.azizi.post.repository.PostRepository;
import com.azizi.post.repository.elasticsearch.ElasticPostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ElasticPostMapper elasticPostMapper;
    private final RabbitMqMessageSender rabbitMqMessageSender;
    private final ElasticPostRepository elasticPostRepository;
    private final ElasticsearchOperations elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    @Value("classpath:json/posts.json")
    private Resource resource;

    @PostConstruct
    public void createPosts() {
        postRepository.deleteAll();
        elasticPostRepository.deleteAll();
        Arrays.stream(getPosts())
                .forEach(postDto -> {
                    Post post = postMapper.toEntity(postDto);
                    Instant publishedAt = Instant.parse(postDto.getPublishedAt());
                    post.setPublishedAt(LocalDateTime.ofInstant(publishedAt, ZoneOffset.UTC));
                    postRepository.save(post);
                    PostPayload payload = postMapper.toPayload(post);
                    payload.setTags(postDto.getTags());
                    payload.setPublishedAt(publishedAt);
                    rabbitMqMessageSender.sendMessage(RabbitMqRoutingKeyConstants.POST_CREATED, payload);
                });
    }

    @Transactional(readOnly = true)
    public PageDto<PostDto> getPosts(Pageable pageable) {
        Page<PostDto> postDtoPage = postRepository.findAll(pageable)
                .map(postMapper::toDto);
        return PageDto.of(postDtoPage);
    }

    @Transactional(readOnly = true)
    public PostDto getPostById(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return postMapper.toDto(post);
    }

    @Transactional
    public PostDto addPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);
        PostPayload payload = postMapper.toPayload(post);
        payload.setPublishedAt(post.getPublishedAt().toInstant(ZoneOffset.UTC));
        rabbitMqMessageSender.sendMessage(RabbitMqRoutingKeyConstants.POST_CREATED, payload);
        return postMapper.toDto(post);
    }

    @Transactional
    public PostDto updatePost(Integer id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post = postMapper.toEntity(postDto, post);
        postRepository.save(post);
        PostPayload payload = postMapper.toPayload(post);
        rabbitMqMessageSender.sendMessage(RabbitMqRoutingKeyConstants.POST_UPDATED, payload);
        return postMapper.toDto(post);
    }

    @Transactional
    public void deletePost(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostPayload payload = postMapper.toPayload(post);
        rabbitMqMessageSender.sendMessage(RabbitMqRoutingKeyConstants.POST_DELETED, payload);
        postRepository.delete(post);
    }

    public PageDto<PostDto> searchByFulltext(String fulltext, Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(fulltext)
                        .field("text")
                        .field("tags")
                        .field("owner.firstName")
                        .field("owner.lastName")
                        .operator(Operator.OR)
                        .type(MultiMatchQueryBuilder.Type.MOST_FIELDS))
                .build();
        searchQuery.setPageable(pageable);
        SearchHits<ElasticPost> searchHits = elasticsearchTemplate.search(searchQuery, ElasticPost.class);
        List<PostDto> posts = searchHits.getSearchHits()
                .stream()
                .map(searchHit -> elasticPostMapper.toDto(searchHit.getContent()))
                .collect(Collectors.toList());
        return PageDto.of(posts, searchHits.getTotalHits(), pageable);
    }

    public List<PostDto> autoCompleteSearch(String query) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(query)
                        .field("text")
                        .field("tags")
                        .field("owner.firstName")
                        .field("owner.lastName")
                        .operator(Operator.OR)
                        .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX))
                .build();
        searchQuery.setPageable(PageRequest.of(0, 5));
        SearchHits<ElasticPost> searchHits = elasticsearchTemplate.search(searchQuery, ElasticPost.class);
        return searchHits.getSearchHits()
                .stream()
                .map(searchHit -> elasticPostMapper.toDto(searchHit.getContent()))
                .collect(Collectors.toList());
    }


    private PostDto[] getPosts() {
        try {
            String posts = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()),
                    StandardCharsets.UTF_8);
            return objectMapper.readValue(posts, PostDto[].class);
        } catch (Exception ignored) {
            return new PostDto[0];
        }
    }

}
