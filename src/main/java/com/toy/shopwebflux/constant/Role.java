package com.toy.shopwebflux.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("어드민"),
    ROLE_MEMBER("사용자");

    private final String description;
}
