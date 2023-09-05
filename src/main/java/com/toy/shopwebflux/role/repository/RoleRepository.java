package com.toy.shopwebflux.role.repository;

import com.toy.shopwebflux.role.domain.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface RoleRepository extends R2dbcRepository<Role, String> {

    @Query(
        "select r.*" +
        " from Role r" +
        " where (:name is null or r.name like concat('%', :name, '%'))"
    )
    Flux<Role> findAll(@Param("name") String name);
}
