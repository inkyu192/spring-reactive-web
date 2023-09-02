package com.toy.shopwebflux.role.dto;

import com.toy.shopwebflux.role.domain.Role;
import lombok.Getter;

@Getter
public class RoleResponse {

    private final String id;
    private final String name;
    private final String description;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }
}
