package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PostService {
    @Autowired
    PostRepository postRepository;

    public Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    public Post getPostBySlug(String slug) {
        return postRepository.findBySlug(slug).orElse(null);
    }

    public Post createPost(Post post) {
        post.setCreatedAt(Instant.now().getEpochSecond());
        return postRepository.save(post);
    }

    public Post updatePostById(Integer id, Post updatedPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }

        updatedPost.setId(post.getId());
        updatedPost.setCreatedAt(post.getCreatedAt());
        return postRepository.save(updatedPost);
    }

    public Boolean deletePostById(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return false;
        }
        postRepository.deleteById(id);
        return true;
    }

    public Post publishPost(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }

        post.setPublished(true);
        post.setPublishedAt(Instant.now().getEpochSecond());
        return postRepository.save(post);
    }
}
