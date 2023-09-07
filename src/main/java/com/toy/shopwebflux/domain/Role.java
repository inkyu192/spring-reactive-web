package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.dto.role.RoleSaveRequest;
import com.toy.shopwebflux.dto.role.RoleUpdateRequest;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Role extends Base implements Persistable<String> {

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

    public Role updateRole(RoleUpdateRequest roleUpdateRequest) {
        this.name = roleUpdateRequest.getName();
        this.description = roleUpdateRequest.getDescription();

        return this;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
