package com.toy.shopwebflux.dto.member;

import com.toy.shopwebflux.constant.Role;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    String password;
    String name;
    Role role;
    String city;
    String street;
    String zipcode;
}
