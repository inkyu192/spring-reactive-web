package com.toy.shopwebflux.role.domain;

import com.toy.shopwebflux.role.dto.RoleSaveRequest;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Role extends Common implements Persistable<String> {

    @Id
    @Column("role_id")
    private String id;
    private String name;
    private String description;

    public static Role createRole(RoleSaveRequest roleSaveRequest) {
        Role role = new Role();

        role.id = roleSaveRequest.getRoleId();
        role.name = roleSaveRequest.getName();
        role.description = roleSaveRequest.getDescription();

        return role;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
