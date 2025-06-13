package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.GetPostBySlugRequest;
import com.fcidn.blog.request.post.UpdatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import com.fcidn.blog.response.post.GetPostResponse;
import com.fcidn.blog.response.post.UpdatePostResponse;
import com.fcidn.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/posts")
public class PostPublicController {
    @Autowired
    PostService postService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Iterable<GetPostResponse>>> getPosts(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        // TODO: only published post
        page = page < 0 ? 0 : --page;
        Iterable<GetPostResponse> result = postService.getPosts(page, limit);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully get list post");
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<GetPostResponse>> getPostBySlug(@Valid @PathVariable GetPostBySlugRequest slug) {
        // TODO: only published post
        GetPostResponse result = postService.getPostBySlug(slug);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully get post");
    }
}
