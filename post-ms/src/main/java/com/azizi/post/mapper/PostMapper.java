package com.azizi.post.mapper;

import com.azizi.common.constants.DateTimePatternConstants;
import com.azizi.common.payload.PostPayload;
import com.azizi.post.dto.PostDto;
import com.azizi.post.entity.Post;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PostMapper {

    @Mapping(target = "publishedAt", dateFormat = DateTimePatternConstants.DATE_TIME)
    PostDto toDto(Post post);

    @Mapping(target = "publishedAt", ignore = true)
    Post toEntity(PostDto postDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    Post toEntity(PostDto postDto, @MappingTarget Post post);

    @Mapping(target = "publishedAt", ignore = true)
    PostPayload toPayload(Post post);

}
