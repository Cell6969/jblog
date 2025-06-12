package com.fcidn.blog.request.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    @Size(min = 3)
    @NotEmpty
    private String name;

    @Size(min = 3)
    @NotEmpty
    private String slug;
}
