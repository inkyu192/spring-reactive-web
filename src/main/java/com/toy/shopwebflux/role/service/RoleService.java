package com.toy.shopwebflux.role.service;

import com.toy.shopwebflux.role.dto.RoleResponse;
import com.toy.shopwebflux.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Flux<RoleResponse> findAll() {
        return roleRepository.findAll()
                .map(RoleResponse::new);
    }

    public Mono<RoleResponse> findById(String id) {
        return roleRepository.findById(id)
                .map(RoleResponse::new);
    }
}
