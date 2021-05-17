package com.azizi.post.indexer.document;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticPost {

    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Object, includeInParent = true)
    private ElasticOwner owner;

    @Field(type = FieldType.Text, includeInParent = true)
    private List<String> tags;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Instant publishedAt;

}
