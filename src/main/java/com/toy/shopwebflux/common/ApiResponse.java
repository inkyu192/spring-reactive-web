package com.toy.shopwebflux.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse(T data) {
        if (data == null) {
            this.code = "500";
            this.message = "실패";
        } else {
            this.code = "200";
            this.message = "성공";
            this.data = data;
        }
    }
}
