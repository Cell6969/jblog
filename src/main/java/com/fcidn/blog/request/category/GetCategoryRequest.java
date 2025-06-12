package com.fcidn.blog.request.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCategoryRequest {
    private Integer page;
    private Integer limit;
}
