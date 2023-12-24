package com.toy.shopwebflux.exception;

import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.List;



@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public Mono<ApiResponse<Void>> handler(CommonException e) {
        return Mono.just(new ApiResponse<>(e.getApiResponseCode()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<List<String>>> handler(MethodArgumentNotValidException e) {
        return Mono.just(
                new ApiResponse<>(
                        ApiResponseCode.PARAMETER_NOT_VALID,
                        e.getBindingResult().getFieldErrors().stream()
                                .map(FieldError::getField)
                                .toList()
                )
        );
    }

    @ExceptionHandler
    public ApiResponse<Void> handler(JwtException e) {
        if (e instanceof UnsupportedJwtException) {
            return new ApiResponse<>(ApiResponseCode.UNSUPPORTED_TOKEN);
        } else if (e instanceof ExpiredJwtException) {
            return new ApiResponse<>(ApiResponseCode.EXPIRED_TOKEN);
        } else{
            return new ApiResponse<>(ApiResponseCode.BAD_TOKEN);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<Void>> handler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return Mono.just(new ApiResponse<>(ApiResponseCode.SYSTEM_ERROR));
    }
}
