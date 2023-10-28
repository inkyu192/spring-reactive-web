package com.toy.shopwebflux.dto.response;

import com.toy.shopwebflux.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private final Long id;
    private final String account;
    private final String name;
    private final Role role;
    private final String city;
    private final String street;
    private final String zipcode;
}
