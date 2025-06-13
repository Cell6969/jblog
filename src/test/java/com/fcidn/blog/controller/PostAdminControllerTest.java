package com.fcidn.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcidn.blog.entity.Category;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.jwt.JwtService;
import com.fcidn.blog.repository.CategoryRepository;
import com.fcidn.blog.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtService jwtService;

    @AfterEach
    void cleanUp() {
        postRepository.deleteAll();
    }

    private String generateToken() {
        String username = "aldo";
        return jwtService.generateTokenByUsername(username);
    }

    private void createPost(String numPost) {
        Category category = createCategory();

        String id = String.format("dummy%s", numPost);
        Post post = new Post();
        post.setTitle(id);
        post.setBody(id);
        post.setBody(id);
        post.setCategory(category);
        post.setSlug(id);
        post.setCommentCount(0L);
        post.setDeleted(false);
        post.setPublished(false);
        post.setCreatedAt(Instant.now().getEpochSecond());
        postRepository.save(post);
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName("cat_dummy");
        category.setSlug("cat_dummy");
        category.setIsDeleted(false);
        category.setCreatedAt(Instant.now().getEpochSecond());
        category.setUpdatedAt(0L);
        return categoryRepository.save(category);
    }

    @Test
    void getPosts_shouldReturnEmpty() throws Exception{
        String token = generateToken();
        mockMvc.perform(get("/api/admin/posts")
                        .param("page", "1")
                        .param("limit", "10")
                .header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void getPosts_shouldReturnOne() throws Exception {
        String token = generateToken();
        createPost("1");
        mockMvc.perform(get("/api/admin/posts")
                .param("page", "1")
                .param("limit", "10")
                .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void getPostBySlug_withValidParam_shouldReturnOne() throws Exception {
        String token = generateToken();
        createPost("1");
        String postParam = "dummy1";
        mockMvc.perform(get(String.format("/api/admin/posts/%s", postParam))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value(postParam));
    }

    @Test
    void getPostBySlug_withInvalidParams_shouldReturnNotFound() throws Exception {
        String token = generateToken();
        String postParam = "invalid";
        mockMvc.perform(get(String.format("/api/admin/posts/%s", postParam))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
