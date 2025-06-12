package com.fcidn.blog.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponse {
    private Integer id;
    private String name;
    private String slug;
    private Long created_at;
    private Long updated_at;
}
