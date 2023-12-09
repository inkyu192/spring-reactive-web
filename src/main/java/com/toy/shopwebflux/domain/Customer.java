package com.toy.shopwebflux.domain;

import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName= lastName;
    }
}
