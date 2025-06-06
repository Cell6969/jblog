package com.fcidn.blog.request.post;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostBySlugRequest {
    @Size(min = 3)
    private String slug;
}
