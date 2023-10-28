package com.toy.shopwebflux.exception;

import com.toy.shopwebflux.constant.ApiResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final String code;
    private final String message;

    public CommonException(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.name(), apiResponseCode.getMessage());
    }

    public CommonException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
