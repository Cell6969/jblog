package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.CreatePostRequest;
import com.fcidn.blog.response.CreatePostResponse;
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
        return postRepository.findAllByIsDeleted(false);
    }

    public Post getPostBySlug(String slug) {
        return postRepository.findFirstBySlugAndIsDeleted(slug, false).orElse(null);
    }

    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        Post post = PostMapper.INSTANCE.map(createPostRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        return PostMapper.INSTANCE.map(post);
    }

    // TODO: refactor code & change to slug
    public Post updatePostById(Integer id, Post updatedPost) {
        Post post = postRepository.findFirstByIdAndIsDeleted(id, false).orElse(null);
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
        post.setDeleted(true);
        postRepository.save(post);
        return true;
    }

    // TODO: change to slug
    public Post publishPost(Integer id) {
        Post post = postRepository.findFirstByIdAndIsDeleted(id, false).orElse(null);
        if (post == null) {
            return null;
        }

        post.setPublished(true);
        post.setPublishedAt(Instant.now().getEpochSecond());
        return postRepository.save(post);
    }
}
