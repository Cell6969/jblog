package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.GetPostBySlugRequest;
import com.fcidn.blog.request.post.UpdatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import com.fcidn.blog.response.post.GetPostResponse;
import com.fcidn.blog.response.post.UpdatePostResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostMapper postMapper;

    public Iterable<GetPostResponse> getPosts(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Post> posts = postRepository.findAllByIsDeleted(false, pageRequest).getContent();
        return postMapper.mapToListPost(posts);
    }

    public GetPostResponse getPostBySlug(GetPostBySlugRequest request) {
        Post post =  postRepository.findFirstBySlugAndIsDeleted(request.getSlug(), false)
                .orElseThrow(() -> new ApiException("not found", HttpStatus.NOT_FOUND));
        return postMapper.mapToGetPost(post);
    }

    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        Post post = postMapper.mapToCreatePost(createPostRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        return postMapper.mapToCreatePost(post);
    }

    public UpdatePostResponse updatePostById(Integer id, UpdatePostRequest request) {
        Post post = postRepository
                .findFirstByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        postMapper.updatePost(request, post);
        Post updatedPost = postRepository.save(post);
        return postMapper.mapToUpdatePost(updatedPost);
    }

    public Boolean deletePostById(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setDeleted(true);
        postRepository.save(post);
        return true;
    }

    public GetPostResponse publishPost(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setPublished(true);
        post.setPublishedAt(Instant.now().getEpochSecond());
        post =  postRepository.save(post);
        return postMapper.mapToGetPost(post);
    }
}
