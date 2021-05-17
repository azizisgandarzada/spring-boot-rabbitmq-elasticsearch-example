package com.azizi.common.payload;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPayload {

    private Integer id;
    private String text;
    private List<String> tags;
    private OwnerPayload owner;
    private Instant publishedAt;

}
