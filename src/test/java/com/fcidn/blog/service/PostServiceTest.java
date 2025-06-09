package com.fcidn.blog.service;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceTest {
    @Autowired
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Autowired
    PostMapper postMapper;

    private AutoCloseable mocks;

    @BeforeEach
    void beforeEach() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void afterEach() throws Exception {
        mocks.close();
    }

    @Test
    void createPost_givenValid_shouldReturnOk(){
        CreatePostRequest postRequest = new CreatePostRequest();
        postRequest.setTitle("dummy");
        postRequest.setSlug("dummy");
        postRequest.setBody("ini body");

        Post post = postMapper.mapToCreatePost(postRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post.setId(1);
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);


        ResponseEntity<ApiResponse<CreatePostResponse>> postResponse = postService.createPost(postRequest);

        Assertions.assertNotNull(postResponse.getBody());
        Assertions.assertEquals(0, postResponse.getBody().getData().getComment_count());
        Assertions.assertEquals(postResponse.getBody().getData().getSlug(), post.getSlug());
        Assertions.assertEquals(postResponse.getBody().getData().getTitle(), post.getTitle());
        Assertions.assertEquals(postResponse.getBody().getData().getComment_count(), post.getCommentCount());

        long maxSeconds = 3;
        long diffCreatedAt = Math.abs(postResponse.getBody().getData().getCreated_at() - post.getCreatedAt());
        Assertions.assertTrue(diffCreatedAt < maxSeconds);

        verify(postRepository, times(1)).save(Mockito.any(Post.class));
    }
}
