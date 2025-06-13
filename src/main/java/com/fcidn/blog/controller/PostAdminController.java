package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.GetPostBySlugRequest;
import com.fcidn.blog.request.post.GetPostRequest;
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
@RequestMapping("/api/admin/posts")
public class PostAdminController {
    @Autowired
    PostService postService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Iterable<GetPostResponse>>> getPosts(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        page = page < 0 ? 0 : --page;
        GetPostRequest request = GetPostRequest.builder()
                .page(page)
                .limit(limit)
                .build();
        Iterable<GetPostResponse> result = postService.getPosts(request, null, null);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully get list post");
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<GetPostResponse>> getPostBySlug(@Valid @PathVariable GetPostBySlugRequest slug) {
        GetPostResponse result = postService.getPostBySlug(slug, null);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully get post");
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@Valid  @RequestBody CreatePostRequest createPostRequest) {
        CreatePostResponse result =  postService.createPost(createPostRequest);
        return ResponseHelper.response(result, HttpStatus.CREATED, "Successfully create post");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(@PathVariable Integer id, @RequestBody @Valid UpdatePostRequest request) {
        UpdatePostResponse result =  postService.updatePostById(id, request);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully updated post");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePost(@PathVariable Integer id) {
        Boolean result =  postService.deletePostById(id);
        return ResponseHelper.response(result,HttpStatus.OK,"successfully deleted");
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<GetPostResponse>> publishPost(@PathVariable Integer id) {
        GetPostResponse result =  postService.publishPost(id);
        return ResponseHelper.response(result,HttpStatus.OK,"successfully published");
    }
}
