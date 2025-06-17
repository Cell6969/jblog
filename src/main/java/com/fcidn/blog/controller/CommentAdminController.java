package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.request.comment.CreateCommentRequest;
import com.fcidn.blog.response.comment.CreateCommentResponse;
import com.fcidn.blog.response.comment.GetCommentResponse;
import com.fcidn.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/comments")
public class CommentAdminController {
    @Autowired
    CommentService commentService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Iterable<GetCommentResponse>>> getComments(
            @RequestParam(required = false) String postSlug,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {

        page = page < 0 ? 0 : --page;
        postSlug = (postSlug == null || postSlug.isBlank()) ? "all" : postSlug;
        Iterable<GetCommentResponse> result = commentService.getComments(postSlug, page, limit);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully get list comment");
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @Valid @RequestBody CreateCommentRequest comment) {
        CreateCommentResponse result = commentService.createComment(comment);
        return ResponseHelper.response(result, HttpStatus.OK, "Successfully create comment");
    }
}
