package com.fcidn.blog.service;

import com.fcidn.blog.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    public Iterable<Comment> getComments(String postSlug, Integer page, Integer limit) {
        List<Comment> commentList = new ArrayList<>();
        return commentList;
    }

    public Comment getComment(Integer id) {
        return new Comment();
    }

    public Comment createComment(Comment comment) {
        return comment;
    }
}
