package com.azizi.post.indexer.repository;

import com.azizi.post.indexer.document.ElasticPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPostRepository extends ElasticsearchRepository<ElasticPost, Integer> {

}
