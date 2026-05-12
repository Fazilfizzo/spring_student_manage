package com.fazil.learn_spring.learnspring_jpa.exception;

import com.fazil.learn_spring.learnspring_jpa.dto.ApiResponse;
import com.fazil.learn_spring.learnspring_jpa.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<ApiResponse<Void>> handleStudentException(HttpServletRequest request, StudentException studentException) {
        List<String> errors = Arrays.asList(studentException.getMessage());
        ApiResponse<Void>  response = ResponseUtil.error(errors, "Student exception occured", 1000, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
