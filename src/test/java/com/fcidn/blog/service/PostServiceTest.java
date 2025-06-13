package com.fcidn.blog.service;

import com.fcidn.blog.entity.Category;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.mapper.PostMapper;
import com.fcidn.blog.repository.CategoryRepository;
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

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceTest {
    @Autowired
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    CategoryRepository categoryRepository;

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
        postRequest.setCategory_name("dummy_category");


        Category category = new Category();
        category.setName("dummy_category");
        category.setSlug("dummy_category");
        category.setIsDeleted(false);
        category.setCreatedAt(Instant.now().getEpochSecond());
        category.setUpdatedAt(0L);
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
        when(categoryRepository.findFirstBySlug(category.getSlug())).thenReturn(Optional.of(category));

        Post post = postMapper.mapToCreatePost(postRequest);
        post.setCommentCount(0L);
        post.setCreatedAt(Instant.now().getEpochSecond());
        post.setId(1);
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);


        CreatePostResponse postResponse = postService.createPost(postRequest);

        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(0, postResponse.getComment_count());
        Assertions.assertEquals(postResponse.getSlug(), post.getSlug());
        Assertions.assertEquals(postResponse.getTitle(), post.getTitle());
        Assertions.assertEquals(postResponse.getComment_count(), post.getCommentCount());

        long maxSeconds = 3;
        long diffCreatedAt = Math.abs(postResponse.getCreated_at() - post.getCreatedAt());
        Assertions.assertTrue(diffCreatedAt < maxSeconds);

        verify(postRepository, times(1)).save(Mockito.any(Post.class));
    }
}
