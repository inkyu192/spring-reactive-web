package com.toy.shopwebflux.dto.request;

public record TokenRequest(
        String accessToken,
        String refreshToken
) {
}
