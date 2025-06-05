package com.fcidn.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionResponse> handler(ApiException exception) {
        return buildResponse(exception.getHttpStatus(), Collections.singletonList(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, messages);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiExceptionResponse> handleMissingParameters(MissingServletRequestParameterException exception) {
        String messages = "Missing required parameters: " + exception.getParameterName();
        return buildResponse(HttpStatus.BAD_REQUEST, Collections.singletonList(messages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiExceptionResponse> handleInvalidJson(HttpMessageNotReadableException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, Collections.singletonList("Malformed or unreadable JSON request"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonList("Internal Server error: " + exception.getMessage()));
    }

    private ResponseEntity<ApiExceptionResponse> buildResponse(HttpStatus statusCode, List<String> messages) {
        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .code(statusCode.value())
                .errorMessages(messages)
                .build();
        return ResponseEntity.status(statusCode).body(response);
    }
}
