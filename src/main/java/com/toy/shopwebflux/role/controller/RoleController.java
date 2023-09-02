package com.toy.shopwebflux.role.controller;

import com.toy.shopwebflux.common.ApiResponse;
import com.toy.shopwebflux.role.dto.RoleResponse;
import com.toy.shopwebflux.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<RoleResponse>>>> roles() {
        return roleService.findAll()
                .collectList()
                .map(roleResponses -> ResponseEntity.ok(new ApiResponse<>(roleResponses)));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ApiResponse<RoleResponse>>> role(@PathVariable String id) {
        return roleService.findById(id)
                .map(roleResponse -> ResponseEntity.ok(new ApiResponse<>(roleResponse)));
    }
}
