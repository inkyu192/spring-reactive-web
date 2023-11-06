package com.toy.shopwebflux.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toy.shopwebflux.constant.ApiResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;

    public ApiResponse() {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), null);
    }

    public ApiResponse(T payload) {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), payload);
    }

    public ApiResponse(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), null);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, T payload) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), payload);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, String message) {
        this(apiResponseCode.name(), message, null);
    }
}
