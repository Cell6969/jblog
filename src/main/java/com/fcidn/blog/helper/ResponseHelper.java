package com.fcidn.blog.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    public static <T> ResponseEntity<ApiResponse<T>> response(T data, HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(ApiResponse.<T>builder()
                        .code(httpStatus.value())
                        .message(message)
                        .data(data)
                        .build());
    }
}
