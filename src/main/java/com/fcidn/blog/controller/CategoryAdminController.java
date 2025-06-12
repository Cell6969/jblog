package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.request.category.CreateCategoryRequest;
import com.fcidn.blog.request.category.GetCategoryRequest;
import com.fcidn.blog.request.category.UpdateCategoryRequest;
import com.fcidn.blog.response.category.CreateCategoryResponse;
import com.fcidn.blog.response.category.GetCategoryResponse;
import com.fcidn.blog.response.category.UpdateCategoryResponse;
import com.fcidn.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<GetCategoryResponse>>> getCategories(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        page = page < 0 ? 0 : --page;
        GetCategoryRequest request = GetCategoryRequest.builder()
                .page(page)
                .limit(limit)
                .build();
        Iterable<GetCategoryResponse> result = categoryService.getCategories(request, null);
        return ResponseHelper.response(result, HttpStatus.OK, "Success get categories");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetCategoryResponse>> getCategoryById(@PathVariable Integer id) {
        GetCategoryResponse result = categoryService.getCategoryById(id);
        return ResponseHelper.response(result, HttpStatus.OK, "Success get category");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateCategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryResponse result = categoryService.create(request);
        return ResponseHelper.response(result, HttpStatus.CREATED, "Success create category");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateCategoryResponse>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        UpdateCategoryResponse result = categoryService.update(id,request);
        return ResponseHelper.response(result, HttpStatus.OK, "Success update category");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCategory(@PathVariable Integer id) {
        Boolean result = categoryService.delete(id);
        return ResponseHelper.response(result, HttpStatus.OK, "Success delete category");
    }
}
