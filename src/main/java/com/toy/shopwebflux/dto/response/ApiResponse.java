package com.toy.shopwebflux.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import static com.toy.shopwebflux.constant.ApiResponseCode.OK;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse() {
        this.code = OK.getCode();
        this.message = OK.getMessage();
    }

    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(T data) {
        this.code = OK.getCode();
        this.message = OK.getMessage();
        this.data = data;
    }
}
