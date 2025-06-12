package com.fcidn.blog.service;

import com.fcidn.blog.entity.Category;
import com.fcidn.blog.exception.ApiException;
import com.fcidn.blog.mapper.CategoryMapper;
import com.fcidn.blog.repository.CategoryRepository;
import com.fcidn.blog.repository.PostRepository;
import com.fcidn.blog.request.category.CreateCategoryRequest;
import com.fcidn.blog.request.category.GetCategoryRequest;
import com.fcidn.blog.request.category.UpdateCategoryRequest;
import com.fcidn.blog.response.category.CreateCategoryResponse;
import com.fcidn.blog.response.category.GetCategoryResponse;
import com.fcidn.blog.response.category.UpdateCategoryResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryMapper categoryMapper;


    public Iterable<GetCategoryResponse> getCategories(GetCategoryRequest request, Boolean isDeleted) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getLimit());
        List<Category> categories = categoryRepository.findAllByIsDeletedOptional(isDeleted, pageRequest).getContent();
        return categoryMapper.mapToListCategory(categories);
    }

    public GetCategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException("data not found", HttpStatus.NOT_FOUND));
        return categoryMapper.mapToGetCategory(category);
    }

    public CreateCategoryResponse create(@Valid CreateCategoryRequest request) {
        Category category = categoryMapper.mapToCreateCategory(request);
        category.setCreatedAt(Instant.now().getEpochSecond());
        category.setIsDeleted(false);
        category.setUpdatedAt(0L);
        categoryRepository.save(category);
        return categoryMapper.mapToCreateCategory(category);
    }

    public UpdateCategoryResponse update(Integer id, @Valid UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException("data not found", HttpStatus.NOT_FOUND));
        categoryMapper.updateCategory(request, category);
        category.setUpdatedAt(Instant.now().getEpochSecond());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.mapToUpdateCategory(updatedCategory);
    }

    public Boolean delete(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException("data not found", HttpStatus.NOT_FOUND));

        Long numberOfPosts = postRepository.countByCategory(category);

        if (numberOfPosts != 0) {
            throw new ApiException("post exist on this category", HttpStatus.BAD_REQUEST);
        }
        category.setIsDeleted(true);
        categoryRepository.save(category);
        return true;
    }
}
