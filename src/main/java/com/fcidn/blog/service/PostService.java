package com.fcidn.blog.service;

import com.fcidn.blog.entity.Category;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.CategoryRepository;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.GetPostBySlugRequest;
import com.fcidn.blog.request.post.GetPostRequest;
import com.fcidn.blog.request.post.UpdatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import com.fcidn.blog.response.post.GetPostResponse;
import com.fcidn.blog.response.post.UpdatePostResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    CategoryRepository categoryRepository;

    @Autowired
    PostMapper postMapper;

    @Cacheable(value = "PostService.getPosts", key = "{#isPublished, #isDeleted, #request.page}")
    public Iterable<GetPostResponse> getPosts(GetPostRequest request, Boolean isPublished, Boolean isDeleted) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getLimit());
        List<Post> posts = postRepository.findAllByIsDeletedAndIsPublished(isDeleted, isPublished, pageRequest)
                .getContent();
        return postMapper.mapToListPost(posts);
    }

    @Cacheable(value = "PostService.getPostBySlug", key = "#request.slug")
    public GetPostResponse getPostBySlug(GetPostBySlugRequest request, Boolean isDeleted) {
        Post post = postRepository.findFirstBySlugAndIsDeleted(request.getSlug(), isDeleted)
                .orElseThrow(() -> new ApiException("not found", HttpStatus.NOT_FOUND));
        return postMapper.mapToGetPost(post);
    }

    @CacheEvict(value = "PostService.getPosts", allEntries = true)
    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
        String categorySlug = createPostRequest.getCategory_name().toLowerCase();
        Category category = categoryRepository.findFirstBySlug(categorySlug)
                .orElseThrow(() -> new ApiException("category not found", HttpStatus.NOT_FOUND));
        Post post = postMapper.mapToCreatePost(createPostRequest);
        post.setCategory(category);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        return postMapper.mapToCreatePost(post);
    }

    // @CachePut(value = "PostService.getPostBySlug", key = "#request.slug")
    @Caching(
        evict = {
            @CacheEvict(value = "PostService.getPostBySlug", allEntries = true),
            @CacheEvict(value = "PostService.getPosts", allEntries = true)
        }
    )
    public UpdatePostResponse updatePostById(Integer id, UpdatePostRequest request) {
        Post post = postRepository
                .findFirstByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        String categorySlug = request.getCategory_name().toLowerCase();
        Category category = categoryRepository.findFirstBySlug(categorySlug)
                .orElseThrow(() -> new ApiException("category not found", HttpStatus.NOT_FOUND));
        postMapper.updatePost(request, post);
        post.setCategory(category);
        Post updatedPost = postRepository.save(post);
        return postMapper.mapToUpdatePost(updatedPost);
    }

    @Caching(
        evict = {
            @CacheEvict(value = "PostService.getPostBySlug", allEntries = true),
            @CacheEvict(value = "PostService.getPosts", allEntries = true)
        }
    )
    public Boolean deletePostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setDeleted(true);
        postRepository.save(post);
        return true;
    }

    @Caching(
        evict = {
            @CacheEvict(value = "PostService.getPostBySlug", allEntries = true),
            @CacheEvict(value = "PostService.getPosts", allEntries = true)
        }
    )
    public GetPostResponse publishPost(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
        post.setPublished(true);
        post.setPublishedAt(Instant.now().getEpochSecond());
        post = postRepository.save(post);
        return postMapper.mapToGetPost(post);
    }
}
