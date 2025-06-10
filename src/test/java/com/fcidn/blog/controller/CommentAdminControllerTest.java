package com.fcidn.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.jwt.JwtService;
import com.fcidn.blog.repository.CommentRepository;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.comment.CreateCommentRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private JwtService jwtService;

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

    private String generateToken() {
        String username = "aldo";
        return jwtService.generateTokenByUsername(username);
    }

    @Test
    void getComments_shouldReturnEmpty() throws Exception {
        String token = generateToken();
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
        String token = generateToken();
        createComment("1");
        mockMvc.perform(get("/api/admin/comments")
                .param("page", "1")
                .param("limit", "10")
                .header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void createComment_shouldReturnSuccess() throws Exception {
        String token = generateToken();
        Post dummyPost = new Post();
        dummyPost.setSlug("dummy1");

        CreateCommentRequest request = new CreateCommentRequest();
        request.setName("test");
        request.setPost(dummyPost);
        request.setEmail("test@gmail.com");
        request.setBody("test body");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/admin/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+ token)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.email").value(request.getEmail()));
    }
}
