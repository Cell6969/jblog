package com.fcidn.blog.service;

import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.repository.CommentRepository;
import com.fcidn.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

    public Iterable<Comment> getComments(String postSlug, Integer page, Integer limit) {
        Post post = postRepository.findFirstBySlugAndIsDeleted(postSlug, false).orElse(null);
        if (post == null) {
            return null;
        }

        PageRequest pageRequest = PageRequest.of(page, limit);
        return commentRepository.findByPostId(post.getId(), pageRequest).getContent();
    }

    public Comment getComment(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment createComment(Comment comment) {
        Post post = postRepository.findFirstBySlugAndIsDeleted(comment.getPost().getSlug(), false).orElse(null);
        if (post == null) {
            return null;
        }
        comment.setCreatedAt(Instant.now().getEpochSecond());
        comment.getPost().setId(post.getId());
        return commentRepository.save(comment);
    }
}
