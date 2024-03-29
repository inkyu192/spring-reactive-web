package spring.reactive.web.java.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.constant.ApiResponseCode;
import spring.reactive.web.java.dto.response.ApiResponse;

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
    public Mono<ApiResponse<Void>> handler(JwtException e) {
        return Mono.error(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<Void>> handler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return Mono.just(new ApiResponse<>(ApiResponseCode.SYSTEM_ERROR));
    }
}
