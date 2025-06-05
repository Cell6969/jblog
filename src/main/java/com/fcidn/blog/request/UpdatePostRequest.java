package com.fcidn.blog.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    @Size(min = 3, message = "minimal 3 karakter")
    @NotNull
    private String title;

    @Size(min = 5, message = "minimal 5 karakter")
    @NotNull
    private String body;

    @Size(min = 3, message = "minimal 3 karakter")
    @NotNull
    private String slug;
}
