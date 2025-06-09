package com.fcidn.blog.controller;


import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.request.comment.CreateCommentRequest;
import com.fcidn.blog.response.comment.CreateCommentResponse;
import com.fcidn.blog.response.comment.GetCommentResponse;
import com.fcidn.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {

        page = page < 0 ? 0 : --page;
        postSlug = (postSlug == null || postSlug.isBlank()) ? "all" : postSlug;
        return commentService.getComments(postSlug, page, limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetCommentResponse>> getComment(@PathVariable Integer id) {
        return commentService.getComment(id);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(@Valid  @RequestBody CreateCommentRequest comment) {
        return commentService.createComment(comment);
    }
}
