package com.toy.shopwebflux.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import static com.toy.shopwebflux.common.ApiResponseCode.*;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CommonException.class)
    public Mono<ApiResponse<Void>> commonExceptionHandler(CommonException e) {
        ApiResponseCode apiResponseCode = e.getApiResponseCode();
        return Mono.just(new ApiResponse<>(apiResponseCode.getCode(), apiResponseCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ApiResponse<Void>> exceptionHandler(Exception e) {
        return Mono.just(new ApiResponse<>(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()));
    }
}
