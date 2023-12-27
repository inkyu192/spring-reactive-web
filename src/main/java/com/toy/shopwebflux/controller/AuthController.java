package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.dto.request.LoginRequest;
import com.toy.shopwebflux.dto.request.TokenRequest;
import com.toy.shopwebflux.dto.response.ApiResponse;
import com.toy.shopwebflux.dto.response.TokenResponse;
import com.toy.shopwebflux.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public Mono<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest)
                .map(ApiResponse::new);
    }

    @PostMapping("refresh")
    public Mono<ApiResponse<TokenResponse>> refresh(@RequestBody TokenRequest tokenRequest) {
        return authService.refresh(tokenRequest)
                .map(ApiResponse::new);
    }
}
