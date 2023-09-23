package com.toy.shopwebflux.dto.member;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    String password;
    String name;
    String roleId;
    String city;
    String street;
    String zipcode;
}
