package com.azizi.post.indexer.mapper;

import com.azizi.common.payload.PostPayload;
import com.azizi.post.indexer.document.ElasticPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ElasticPostMapper {

    ElasticPost toDocument(PostPayload payload);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    ElasticPost toDocument(PostPayload payload, @MappingTarget ElasticPost elasticPost);

}
