package com.toy.shopwebflux.dto.request;

import com.toy.shopwebflux.constant.Role;

public record MemberUpdateRequest(
        String name,
        Role role,
        String city,
        String street,
        String zipcode
) {
}
