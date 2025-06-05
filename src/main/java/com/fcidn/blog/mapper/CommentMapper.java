package com.fcidn.blog.mapper;

import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.request.CreateCommentRequest;
import com.fcidn.blog.response.CreateCommentResponse;
import com.fcidn.blog.response.GetCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment mapToCreateComment(CreateCommentRequest commentRequest);

    @Mapping(source = "createdAt", target = "created_at")
    CreateCommentResponse mapToCreateComment(Comment comment);

    @Mapping(source = "post.id", target = "post_id")
    @Mapping(source = "createdAt", target = "created_at")
    GetCommentResponse mapToGetComment(Comment comment);

    List<GetCommentResponse> mapToGetListComment(List<Comment> comments);
}
