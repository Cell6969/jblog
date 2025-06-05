package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.CreatePostRequest;
import com.fcidn.blog.request.GetPostBySlugRequest;
import com.fcidn.blog.request.UpdatePostRequest;
import com.fcidn.blog.response.CreatePostResponse;
import com.fcidn.blog.response.GetPostResponse;
import com.fcidn.blog.response.UpdatePostResponse;
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

    public ResponseEntity<ApiResponse<Iterable<GetPostResponse>>> getPosts(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Post> posts = postRepository.findAllByIsDeleted(false, pageRequest).getContent();
        Iterable<GetPostResponse> response = postMapper.mapToListPost(posts);
        return ResponseHelper.response(response, HttpStatus.OK, "Successfully get list post");
    }

    public ResponseEntity<ApiResponse<GetPostResponse>> getPostBySlug(GetPostBySlugRequest request) {
        Post post =  postRepository.findFirstBySlugAndIsDeleted(request.getSlug(), false)
                .orElseThrow(() -> new ApiException("not found", HttpStatus.NOT_FOUND));
        GetPostResponse response = postMapper.mapToGetPost(post);
        return ResponseHelper.response(response, HttpStatus.OK, "Successfully get post");
    }

    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(CreatePostRequest createPostRequest) {
        Post post = postMapper.mapToCreatePost(createPostRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        CreatePostResponse response =  postMapper.mapToCreatePost(post);
        return ResponseHelper.response(response, HttpStatus.CREATED, "Successfully create post");
    }

    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePostById(Integer id, UpdatePostRequest request) {
        Post post = postRepository
                .findFirstByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        postMapper.updatePost(request, post);
        Post updatedPost = postRepository.save(post);
        UpdatePostResponse response =  postMapper.mapToUpdatePost(updatedPost);
        return ResponseHelper.response(response, HttpStatus.OK, "Successfully updated post");
    }

    public ResponseEntity<ApiResponse<Boolean>> deletePostById(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setDeleted(true);
        postRepository.save(post);
        return ResponseHelper.response(true,HttpStatus.OK,"successfully deleted");
    }

    public ResponseEntity<ApiResponse<GetPostResponse>> publishPost(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setPublished(true);
        post.setPublishedAt(Instant.now().getEpochSecond());
        post =  postRepository.save(post);
        GetPostResponse response = postMapper.mapToGetPost(post);
        return ResponseHelper.response(response,HttpStatus.OK,"successfully published");
    }
}
