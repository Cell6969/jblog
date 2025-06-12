package com.fcidn.blog.mapper;

import com.fcidn.blog.entity.Category;
import com.fcidn.blog.request.category.CreateCategoryRequest;
import com.fcidn.blog.request.category.UpdateCategoryRequest;
import com.fcidn.blog.response.category.CreateCategoryResponse;
import com.fcidn.blog.response.category.GetCategoryResponse;
import com.fcidn.blog.response.category.UpdateCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    List<GetCategoryResponse> mapToListCategory(List<Category> categories);

    Category mapToCreateCategory(CreateCategoryRequest createCategoryRequest);
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    CreateCategoryResponse mapToCreateCategory(Category category);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    GetCategoryResponse mapToGetCategory(Category category);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UpdateCategoryResponse mapToUpdateCategory(Category category);
    void updateCategory(UpdateCategoryRequest request, @MappingTarget Category category);
}
