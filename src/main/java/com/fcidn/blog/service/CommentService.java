package com.fcidn.blog.service;

import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.mapper.CommentMapper;
import com.fcidn.blog.repository.CommentRepository;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.CreateCommentRequest;
import com.fcidn.blog.response.CreateCommentResponse;
import com.fcidn.blog.response.GetCommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentMapper commentMapper;

    public Iterable<GetCommentResponse> getComments(String postSlug, Integer page, Integer limit) {
        Post post = postRepository.findFirstBySlugAndIsDeleted(postSlug, false).orElse(null);
        if (post == null) {
            return null;
        }

        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Comment> comments =  commentRepository.findByPostId(post.getId(), pageRequest).getContent();
        return commentMapper.mapToGetListComment(comments);
    }

    public GetCommentResponse getComment(Integer id) {
        Comment comment =  commentRepository.findById(id).orElseThrow(() -> new ApiException("comment not found", HttpStatus.NOT_FOUND));
        return commentMapper.mapToGetComment(comment);
    }

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest request) {
        Post post = postRepository.findFirstBySlugAndIsDeleted(request.getPost().getSlug(), false).orElse(null);
        if (post == null) {
            return null;
        }

        Comment comment = commentMapper.mapToCreateComment(request);

        // save comment
        comment.setCreatedAt(Instant.now().getEpochSecond());
        comment.getPost().setId(post.getId());
        commentRepository.save(comment);

        // update comment count
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
        return commentMapper.mapToCreateComment(comment);
    }
}
