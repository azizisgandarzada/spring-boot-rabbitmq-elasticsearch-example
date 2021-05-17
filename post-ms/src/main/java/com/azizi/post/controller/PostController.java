package com.azizi.post.controller;

import com.azizi.post.dto.PageDto;
import com.azizi.post.dto.PostDto;
import com.azizi.post.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public PageDto<PostDto> getPosts(Pageable pageable) {
        return postService.getPosts(pageable);
    }

    @GetMapping("{id}")
    public PostDto getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto addPost(@RequestBody @Valid PostDto postDto) {
        return postService.addPost(postDto);
    }

    @PutMapping("{id}")
    public PostDto updatePost(@PathVariable Integer id, @RequestBody @Valid PostDto postDto) {
        return postService.updatePost(id, postDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
    }

    @GetMapping("searching")
    public PageDto<PostDto> searchByFulltext(@RequestParam String fulltext, Pageable pageable) {
        return postService.searchByFulltext(fulltext, pageable);
    }

    @GetMapping("autocomplete-searching")
    public List<PostDto> autoCompleteSearch(@RequestParam String query) {
        return postService.autoCompleteSearch(query);
    }

}
