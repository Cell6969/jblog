package com.fcidn.blog.request.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPostRequest {
    private Integer page;
    private Integer limit;
}
