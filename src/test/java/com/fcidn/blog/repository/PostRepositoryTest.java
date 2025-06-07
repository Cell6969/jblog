package com.fcidn.blog.repository;

import com.fcidn.blog.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }


    private Post createDummyPost(String numPost) {
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
        return post;
    }

    @Test
    void findAllByIsDeleted_givenNoPost_shouldReturnEmpty() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Post> posts = postRepository.findAllByIsDeleted(true, pageRequest).getContent();

        Assertions.assertNotNull(posts);
        Assertions.assertEquals(0, posts.size());
    }

    @Test
    void findAllByIsDeleted_givenPostsOnlyOneNotDeleted_shouldReturnOnlyOne() {
        Post post1 = createDummyPost("1");
        post1.setDeleted(true);
        postRepository.save(post1);

        Post post2 = createDummyPost("2");
        post2.setDeleted(false);
        postRepository.save(post2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Post> posts = postRepository.findAllByIsDeleted(false, pageRequest).getContent();

        Assertions.assertNotNull(posts);
        Assertions.assertEquals(1, posts.size());
    }
}
