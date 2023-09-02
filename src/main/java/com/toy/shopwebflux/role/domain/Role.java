package com.toy.shopwebflux.role.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
public class Role {

    @Id
    @Column("role_id")
    private String id;
    private String name;
    private String description;
}
