package com.fcidn.blog.mapper;

import com.fcidn.blog.entity.Post;
import com.fcidn.blog.request.post.CreatePostRequest;
import com.fcidn.blog.request.post.UpdatePostRequest;
import com.fcidn.blog.response.post.CreatePostResponse;
import com.fcidn.blog.response.post.GetPostResponse;
import com.fcidn.blog.response.post.UpdatePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post mapToCreatePost (CreatePostRequest postRequest);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "publishedAt", target = "published_at")
    @Mapping(source = "commentCount", target = "comment_count")
    CreatePostResponse mapToCreatePost(Post post);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "publishedAt", target = "published_at")
    @Mapping(source = "published", target = "is_published")
    @Mapping(source = "commentCount", target = "comment_count")
    GetPostResponse mapToGetPost(Post post);

    List<GetPostResponse> mapToListPost(List<Post> posts);

    void updatePost(UpdatePostRequest request, @MappingTarget Post post);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "publishedAt", target = "published_at")
    @Mapping(source = "commentCount", target = "comment_count")
    UpdatePostResponse mapToUpdatePost(Post post);
}
