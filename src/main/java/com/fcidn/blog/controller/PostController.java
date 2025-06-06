package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.GetPostBySlugRequest;
import com.fcidn.blog.request.post.UpdatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import com.fcidn.blog.response.post.GetPostResponse;
import com.fcidn.blog.response.post.UpdatePostResponse;
import com.fcidn.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Iterable<GetPostResponse>>> getPosts(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        page = page < 0 ? 0 : --page;
        return postService.getPosts(page, limit);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<GetPostResponse>> getPostBySlug(@Valid @PathVariable GetPostBySlugRequest slug) {
        return postService.getPostBySlug(slug);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@Valid  @RequestBody CreatePostRequest createPostRequest) {
        return postService.createPost(createPostRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(@PathVariable Integer id, @RequestBody @Valid UpdatePostRequest request) {
        return postService.updatePostById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePost(@PathVariable Integer id) {
        return postService.deletePostById(id);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<GetPostResponse>> publishPost(@PathVariable Integer id) {
        return postService.publishPost(id);
    }
}
