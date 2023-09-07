package com.toy.shopwebflux.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {

    OK("200", "성공"),
    BAD_REQUEST("400", "요청 오류"),
    INTERNAL_SERVER_ERROR("500", "시스템 오류");

    private final String code;
    private final String message;
}