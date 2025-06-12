package com.fcidn.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcidn.blog.entity.Category;
import com.fcidn.blog.jwt.JwtService;
import com.fcidn.blog.repository.CategoryRepository;
import com.fcidn.blog.request.category.CreateCategoryRequest;
import com.fcidn.blog.request.category.UpdateCategoryRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    private String generateToken() {
        String username = "aldo";
        return jwtService.generateTokenByUsername(username);
    }

    private Category createCategory(String numCategory) {
        String id = String.format("dummy%s", numCategory);
        Category category = new Category();
        category.setName(id);
        category.setSlug(id);
        category.setIsDeleted(false);
        category.setCreatedAt(Instant.now().getEpochSecond());
        category.setUpdatedAt(0L);
        return categoryRepository.save(category);
    }


    @Test
    void getCategories_shouldReturnEmpty() throws Exception{
        String token = generateToken();
        mockMvc.perform(get("/api/admin/categories")
                .param("page", "1")
                .param("limit", "10")
                .header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void getCategories_shouldReturnOne() throws Exception {
        createCategory("1");
        String token = generateToken();
        mockMvc.perform(get("/api/admin/categories")
                        .param("page", "1")
                        .param("limit", "10")
                        .header("Authorization", "Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("dummy1"))
                .andExpect(jsonPath("$.data[0].slug").value("dummy1"));
    }

    @Test
    void createCategory_shouldReturnBadRequest() throws Exception {
        String token = generateToken();
        CreateCategoryRequest request = CreateCategoryRequest
                .builder()
                .name("1")
                .slug("1")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void createCategory_shouldReturnSuccess() throws Exception {
        String token = generateToken();
        CreateCategoryRequest request = CreateCategoryRequest
                .builder()
                .name("dummy")
                .slug("dummy")
                .build();
        System.out.println(request);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.slug").value(request.getSlug()));
    }

    @Test
    void getCategory_shouldReturnNotFound() throws Exception {
        String token = generateToken();
        mockMvc.perform(get("/api/admin/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCategory_shouldReturnOne() throws Exception {
        String token = generateToken();
        Category category = createCategory("1");
        mockMvc.perform(get(String.format("/api/admin/categories/%s", category.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(category.getId()))
                .andExpect(jsonPath("$.data.name").value(category.getName()))
                .andExpect(jsonPath("$.data.slug").value(category.getSlug()));
    }

    @Test
    void updateCategory_shouldReturnBadRequest() throws Exception {
        String token = generateToken();
        Category category = createCategory("1");
        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .name("1")
                .slug("1")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(String.format("/api/admin/categories/%s", category.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void updateCategory_shouldReturnSuccess() throws Exception {
        String token = generateToken();
        Category category = createCategory("1");
        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .name("updated_category")
                .slug("updated_category")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(String.format("/api/admin/categories/%s", category.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.slug").value(request.getSlug()));
    }

    @Test
    void deleteCategory_shouldReturnNotFound() throws Exception {
        String token = generateToken();
        mockMvc.perform(delete(String.format("/api/admin/categories/%s", "1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_shouldReturnSuccess() throws Exception {
        String token = generateToken();
        Category category = createCategory("1");
        mockMvc.perform(delete(String.format("/api/admin/categories/%s", category.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        Category updatedCategory = categoryRepository.findById(category.getId()).orElse(null);
        Assertions.assertNotNull(updatedCategory);
        Assertions.assertTrue(updatedCategory.getIsDeleted());
    }
}
