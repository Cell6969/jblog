package com.fcidn.blog.mapper;

import com.fcidn.blog.entity.Comment;
import com.fcidn.blog.request.CreateCommentRequest;
import com.fcidn.blog.response.CreateCommentResponse;
import com.fcidn.blog.response.GetCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment mapToCreateComment(CreateCommentRequest commentRequest);

    @Mapping(source = "createdAt", target = "created_at")
    CreateCommentResponse mapToCreateComment(Comment comment);

    @Mapping(target = "post", ignore = true)
    @Mapping(source = "createdAt", target = "created_at")
    GetCommentResponse mapToGetComment(Comment comment);
}
