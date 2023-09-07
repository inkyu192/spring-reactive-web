package com.toy.shopwebflux.dto.role;

import lombok.Getter;

@Getter
public class RoleSaveRequest {

    private String roleId;
    private String name;
    private String description;
}
