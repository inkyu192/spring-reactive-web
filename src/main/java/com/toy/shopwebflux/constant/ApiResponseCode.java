package com.toy.shopwebflux.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {

    OK("200", "정상"),
    DATA_NOT_FOUND("204", "데이터 없음"),
    BAD_REQUEST("400", "요청 오류"),
    INTERNAL_SERVER_ERROR("500", "시스템 오류");

    private final String code;
    private final String message;
}