package com.fcidn.blog.mapper;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.request.CreatePostRequest;
import com.fcidn.blog.response.CreatePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Post map (CreatePostRequest postRequest);

    @Mapping(source = "slug", target = "path")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "publishedAt", target = "published_at")
    @Mapping(source = "commentCount", target = "comment_count")
    CreatePostResponse map(Post post);
}
