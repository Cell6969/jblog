package com.fcidn.blog.request.comment;

import com.fcidn.blog.entity.Post;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotNull(message = "name is required")
    private String name;

    @Size(max = 100)
    @Email(message = "email is invalid")
    @NotBlank(message = "email")
    private String email;

    @NotNull(message = "required post slug")
    private Post post;

    @Size(min = 5, max = 1000)
    @NotBlank(message = "content is required")
    private String body;
}
