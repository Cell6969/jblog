package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.CreatePostRequest;
import com.fcidn.blog.request.GetPostBySlugRequest;
import com.fcidn.blog.response.CreatePostResponse;
import com.fcidn.blog.response.GetPostBySlugResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public GetPostBySlugResponse getPostBySlug(GetPostBySlugRequest request) {
        Post post =  postRepository.findFirstBySlugAndIsDeleted(request.getSlug(), false)
                .orElseThrow(() -> new ApiException("not found", HttpStatus.NOT_FOUND));
        if (post == null) {
            return null;
        }
        return PostMapper.INSTANCE.mapToGetPostBySlug(post);
    }

    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        Post post = PostMapper.INSTANCE.mapToCreatePost(createPostRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        return PostMapper.INSTANCE.mapToCreatePost(post);
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
