package com.toy.shopwebflux.exception;

import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.toy.shopwebflux.constant.ApiResponseCode.*;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public Mono<ApiResponse<Void>> commonExceptionHandler(CommonException e) {
        return Mono.just(new ApiResponse<>(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<List<String>>> handler(BindException e) {
        return Mono.just(
                new ApiResponse<>(
                        ApiResponseCode.PARAMETER_NOT_VALID.name(),
                        ApiResponseCode.PARAMETER_NOT_VALID.getMessage(),
                        e.getBindingResult().getFieldErrors().stream()
                                .map(FieldError::getField)
                                .toList()
                )
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<Void>> handler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return Mono.just(new ApiResponse<>(SYSTEM_ERROR.name(), SYSTEM_ERROR.getMessage()));
    }
}
