package com.fcidn.blog.request.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @Size(min = 3)
    @NotEmpty
    private String name;

    @Size(min = 3)
    @NotEmpty
    private String slug;
}
