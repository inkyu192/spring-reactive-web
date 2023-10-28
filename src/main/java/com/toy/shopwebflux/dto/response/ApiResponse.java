package com.toy.shopwebflux.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toy.shopwebflux.constant.ApiResponseCode;
import lombok.Getter;


@Getter
public class ApiResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse() {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage());
    }

    public ApiResponse(T data) {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), data);
    }

    public ApiResponse(String code, String message) {
        this(code, message, null);
    }

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
