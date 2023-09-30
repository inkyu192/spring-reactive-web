package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.dto.role.RoleSaveRequest;
import com.toy.shopwebflux.dto.role.RoleUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table
public class Role extends Base implements Persistable<String> {

    @Id
    @Column("role_id")
    private String id;
    private String name;
    private String description;

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public static Role createRole(RoleSaveRequest roleSaveRequest) {
        return Role.builder()
                .id(roleSaveRequest.getRoleId())
                .name(roleSaveRequest.getName())
                .description(roleSaveRequest.getDescription())
                .build();
    }

    public Role updateRole(RoleUpdateRequest roleUpdateRequest) {
        this.name = roleUpdateRequest.getName();
        this.description = roleUpdateRequest.getDescription();

        return this;
    }
}
