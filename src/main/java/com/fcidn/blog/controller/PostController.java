package com.fcidn.blog.controller;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    PostService postService;


    @GetMapping("")
    public Iterable<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{slug}")
    public Post getPostBySlug(@PathVariable String slug) {
        return postService.getPostBySlug(slug);
    }

    @PostMapping("")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Post updatePostById(@PathVariable Integer id, @RequestBody Post updatedPost) {
        return postService.updatePostById(id, updatedPost);
    }

    @DeleteMapping("/{id}")
    public Boolean deletePostById(@PathVariable Integer id) {
        return postService.deletePostById(id);
    }

    @PostMapping("/{id}/publish")
    public Post publishPost(@PathVariable Integer id) {
        return postService.publishPost(id);
    }
}
