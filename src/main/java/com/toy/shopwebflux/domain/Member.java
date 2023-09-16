package com.toy.shopwebflux.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Member extends Base {

    @Id
    @Column("member_id")
    private Long id;
    private String account;
    private String password;
    private String name;
    private String roleId;
    private String city;
    private String street;
    private String zipcode;
}
