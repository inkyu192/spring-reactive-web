package com.toy.shopwebflux.role.controller;

import com.toy.shopwebflux.common.ApiResponse;
import com.toy.shopwebflux.role.dto.RoleResponse;
import com.toy.shopwebflux.role.dto.RoleSaveRequest;
import com.toy.shopwebflux.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public Mono<ApiResponse<List<RoleResponse>>> roles() {
        return roleService.findAll()
                .collectList()
                .map(ApiResponse::new);
    }

    @GetMapping("{id}")
    public Mono<ApiResponse<RoleResponse>> role(@PathVariable String id) {
        return roleService.findById(id)
                .map(ApiResponse::new);
    }

    @PostMapping
    public Mono<ApiResponse<RoleResponse>> saveRole(@RequestBody RoleSaveRequest roleSaveRequest) {
        return roleService.save(roleSaveRequest)
                .map(ApiResponse::new);
    }

    @DeleteMapping("{id}")
    public Mono<ApiResponse<Void>> deleteRole(@PathVariable String id) {
        return roleService.delete(id)
                .thenReturn(new ApiResponse<>());
    }
}
