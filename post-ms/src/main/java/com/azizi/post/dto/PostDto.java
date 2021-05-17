package com.azizi.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Integer id;

    @NotBlank
    private String text;

    @NotEmpty
    private List<String> tags;

    private OwnerDto owner;

    private String publishedAt;

}
