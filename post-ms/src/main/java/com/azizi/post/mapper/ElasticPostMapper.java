package com.azizi.post.mapper;

import com.azizi.common.constants.DateTimePatternConstants;
import com.azizi.post.document.ElasticPost;
import com.azizi.post.dto.PostDto;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ElasticPostMapper {

    @Mapping(target = "publishedAt", ignore = true)
    PostDto toDto(ElasticPost elasticPost);

    @AfterMapping
    default void formatInstantToString(ElasticPost elasticPost, @MappingTarget PostDto postDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePatternConstants.DATE_TIME)
                .withLocale(Locale.forLanguageTag("az"))
                .withZone(ZoneId.systemDefault());
        postDto.setPublishedAt(formatter.format(elasticPost.getPublishedAt()));
    }

}
