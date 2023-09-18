package com.toy.shopwebflux.dto.member;

import lombok.Getter;

@Getter
public class MemberSaveRequest {

    String account;
    String password;
    String name;
    String roleId;
    String city;
    String street;
    String zipcode;
}
