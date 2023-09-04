package com.toy.shopwebflux.role.dto;

import lombok.Getter;

@Getter
public class RoleSaveRequest {

    private String roleId;
    private String name;
    private String description;
}
