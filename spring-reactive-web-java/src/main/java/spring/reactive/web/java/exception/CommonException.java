package spring.reactive.web.java.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.reactive.web.java.constant.ApiResponseCode;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;
}
