package com.fcidn.blog.controller;

import com.fcidn.blog.helper.ApiResponse;
import com.fcidn.blog.helper.ResponseHelper;
import com.fcidn.blog.request.category.GetCategoryRequest;
import com.fcidn.blog.response.category.GetCategoryResponse;
import com.fcidn.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/categories")
public class CategoryPublicController {

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
        Iterable<GetCategoryResponse> result = categoryService.getCategories(request, false);
        return ResponseHelper.response(result, HttpStatus.OK, "Success get categories");
    }
}
