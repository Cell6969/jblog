package com.fcidn.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.repository.CommentRepository;
import com.fcidn.blog.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
public class CommentAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        createPost("1");
    }

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    private void createPost(String numPost) {
        String id = String.format("dummy%s", numPost);
        Post post = new Post();
        post.setTitle(id);
        post.setBody(id);
        post.setBody(id);
        post.setSlug(id);
        post.setCommentCount(0L);
        post.setDeleted(false);
        post.setPublished(false);
        post.setCreatedAt(Instant.now().getEpochSecond());
        postRepository.save(post);
    }

    private void createComment(String numComment) {
        Post post = postRepository.findById(1).orElse(null);
        String id = String.format("dummycomment%s", numComment);
        Comment comment = new Comment();
        comment.setBody(id);
        comment.setName(id);
        comment.setEmail(id);
        comment.setPost(post);
        comment.setCreatedAt(Instant.now().getEpochSecond());
        commentRepository.save(comment);
    }

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2Jsb2dzLmZjaWRuLmNvbSIsInN1YiI6ImFsZG8iLCJpYXQiOjE3NDk0ODcwNDAsImV4cCI6MTc0OTQ4NzY0MH0.XOXqHHA0aQbGdVxGkpn-IXuHk-wIsGmCQOG_Muv0vsQ";

    @Test
    void getComments_shouldReturnEmpty() throws Exception {
        mockMvc.perform(get("/api/admin/comments")
                        .param("page", "1")
                        .param("limit", "10")
                        .header("Authorization", "Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void getComment_shouldReturnOne() throws Exception {
        createComment("1");
        mockMvc.perform(get("/api/admin/comments")
                .param("page", "1")
                .param("limit", "10")
                .header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }
}
