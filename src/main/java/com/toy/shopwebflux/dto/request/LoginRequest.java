package com.toy.shopwebflux.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
