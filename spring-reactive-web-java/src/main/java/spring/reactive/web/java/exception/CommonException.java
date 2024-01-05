package spring.reactive.web.java.exception;

import lombok.Getter;
import spring.reactive.web.java.constant.ApiResponseCode;

@Getter
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;

    public CommonException(ApiResponseCode apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }
}
