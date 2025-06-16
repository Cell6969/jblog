package com.fcidn.blog.response.comment;

import com.fcidn.blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponse {
    private String name;
    private String email;
    private String body;
    private Post post;
    private Long created_at;
}
