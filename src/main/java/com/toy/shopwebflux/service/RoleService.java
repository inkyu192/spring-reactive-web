package com.toy.shopwebflux.service;

import com.toy.shopwebflux.common.ApiResponseCode;
import com.toy.shopwebflux.common.CommonException;
import com.toy.shopwebflux.domain.Role;
import com.toy.shopwebflux.dto.role.RoleResponse;
import com.toy.shopwebflux.dto.role.RoleSaveRequest;
import com.toy.shopwebflux.dto.role.RoleUpdateRequest;
import com.toy.shopwebflux.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Flux<RoleResponse> findAll(String name) {
        return roleRepository.findAll(name)
                .map(RoleResponse::new);
    }

    public Mono<RoleResponse> findById(String id) {
        return roleRepository.findById(id)
                .map(RoleResponse::new)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)));
    }

    @Transactional
    public Mono<RoleResponse> save(RoleSaveRequest roleSaveRequest) {
        return roleRepository.save(Role.createRole(roleSaveRequest))
                .map(RoleResponse::new);
    }

    @Transactional
    public Mono<RoleResponse> update(String id, RoleUpdateRequest roleUpdateRequest) {
        return roleRepository.findById(id)
                .flatMap(role -> roleRepository.save(role.updateRole(roleUpdateRequest)))
                .map(RoleResponse::new);
    }

    @Transactional
    public Mono<Void> delete(String id) {
        return roleRepository.deleteById(id);
    }
}
