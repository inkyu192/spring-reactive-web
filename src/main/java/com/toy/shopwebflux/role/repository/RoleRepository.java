package com.toy.shopwebflux.role.repository;

import com.toy.shopwebflux.role.domain.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role, String> {
}
