package com.azizi.post.repository.elasticsearch;

import com.azizi.post.document.ElasticPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPostRepository extends ElasticsearchRepository<ElasticPost, Integer> {

    Page<ElasticPost> findAllByTextStartingWith(String text, Pageable pageable);

}
