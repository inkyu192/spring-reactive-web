package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.dto.request.LoginRequest;
import com.toy.shopwebflux.dto.request.TokenRequest;
import com.toy.shopwebflux.dto.response.ApiResponse;
import com.toy.shopwebflux.dto.response.TokenResponse;
import com.toy.shopwebflux.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @PostMapping("/auth/login")
    public Mono<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return commonService.login(loginRequest)
                .map(ApiResponse::new);
    }

    @PostMapping("/auth/refresh")
    public Mono<ApiResponse<TokenResponse>> refresh(@RequestBody TokenRequest tokenRequest) {
        return commonService.refresh(tokenRequest)
                .map(ApiResponse::new);
    }
}
