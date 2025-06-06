package com.fcidn.blog.request.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Size(min = 2, max = 100)
    @NotEmpty
    private String username;

    @Size(min = 3)
    @NotEmpty
    private String password;
}
