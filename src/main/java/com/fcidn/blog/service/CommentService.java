package com.fcidn.blog.service;

import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.entity.Post;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.mapper.CommentMapper;
import com.fcidn.blog.repository.CommentRepository;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.comment.CreateCommentRequest;
import com.fcidn.blog.response.comment.CreateCommentResponse;
import com.fcidn.blog.response.comment.GetCommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Cacheable(value = "CommentService.getComments", key = "{#postSlug, #page, #limit}")
    public Iterable<GetCommentResponse> getComments(String postSlug, Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        List<Comment> comments;

        if (!postSlug.equals("all")) {
            Post post = postRepository
                    .findFirstBySlugAndIsDeleted(postSlug, false)
                    .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
            comments = commentRepository.findByPostId(post.getId(), pageRequest).getContent();
        } else {
            comments = commentRepository.findAll(pageRequest).getContent();
        }
        return commentMapper.mapToGetListComment(comments);
    }

    public ResponseEntity<ApiResponse<GetCommentResponse>> getComment(Integer id) {
        Comment comment =  commentRepository.findById(id).orElseThrow(() -> new ApiException("comment not found", HttpStatus.NOT_FOUND));
        GetCommentResponse response = commentMapper.mapToGetComment(comment);
        return ResponseHelper.response(response, HttpStatus.OK, "Successfully get comment");
    }

    @CacheEvict(value = "CommentService.getComments", allEntries = true)
    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest request) {
        Post post = postRepository
                .findFirstBySlugAndIsDeleted(request.getPost().getSlug(), false)
                .orElseThrow(() -> new ApiException("post not found", HttpStatus.NOT_FOUND));
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
