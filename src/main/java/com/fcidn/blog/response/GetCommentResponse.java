package com.fcidn.blog.response;

import com.fcidn.blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer post_id;
    private Long created_at;
}
