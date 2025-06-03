package com.fcidn.blog.controller;


import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("")
    public Iterable<Comment> getComments(
            @RequestParam(required = false) String postSlug,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        return commentService.getComments(postSlug, page, limit);
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Integer id) {
        return commentService.getComment(id);
    }

    @PostMapping("")
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }
}
