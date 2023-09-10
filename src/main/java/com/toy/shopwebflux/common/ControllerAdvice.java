package com.toy.shopwebflux.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CommonException.class)
    public Mono<ApiResponse<Void>> commonExceptionHandler(CommonException e) {
        ApiResponseCode apiResponseCode = e.getApiResponseCode();
        return Mono.just(new ApiResponse<>(apiResponseCode.getCode(), apiResponseCode.getMessage()));
    }
}
