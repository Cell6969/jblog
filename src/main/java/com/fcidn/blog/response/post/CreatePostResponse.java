package com.fcidn.blog.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostResponse {
    private String title;
    private String body;
    private String slug;
    private Long created_at;
    private Long published_at;
    private Long comment_count;
}
