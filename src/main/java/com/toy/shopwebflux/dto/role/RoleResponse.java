package com.toy.shopwebflux.dto.role;

import com.toy.shopwebflux.domain.Role;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleResponse {

    private final String id;
    private final String name;
    private final String description;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
        this.createdDate = role.getCreatedDate();
        this.lastModifiedDate = role.getLastModifiedDate();
    }
}
