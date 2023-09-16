package com.toy.shopwebflux.dto.member;

import com.toy.shopwebflux.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String account;
    private final String name;
    private final String roleId;
    private final String city;
    private final String street;
    private final String zipcode;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.account = member.getAccount();
        this.name = member.getName();
        this.roleId = member.getRoleId();
        this.city = member.getCity();
        this.street = member.getStreet();
        this.zipcode = member.getZipcode();
    }
}