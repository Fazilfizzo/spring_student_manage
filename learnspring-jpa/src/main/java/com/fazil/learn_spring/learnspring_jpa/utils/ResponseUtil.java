package com.fazil.learn_spring.learnspring_jpa.utils;

import com.fazil.learn_spring.learnspring_jpa.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class ResponseUtil {
    public static <T> ApiResponse<T> success(T data, String message, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        response.setErrorCode(0);
        response.setTimeStamp(System.currentTimeMillis());
        response.setPath(path);
        return response;
    }
    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        response.setErrorCode(errorCode);
        response.setTimeStamp(System.currentTimeMillis());
        response.setPath(path);
        return response;
    }

    public static <T> ApiResponse<T> error(String error, String message, int errorCode, String path){
        return error(Arrays.asList(error), message, errorCode, path);
    }

    public static <T> ResponseEntity<List<T>> emptyListToNoContent(List<T> list) {
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}
