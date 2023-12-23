package com.toy.shopwebflux.service;

import com.toy.shopwebflux.config.security.JwtTokenProvider;
import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.dto.request.LoginRequest;
import com.toy.shopwebflux.dto.response.TokenResponse;
import com.toy.shopwebflux.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final JwtTokenProvider jwtTokenProvider;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    public Mono<TokenResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        return reactiveAuthenticationManager.authenticate(usernamePasswordAuthenticationToken)
                .onErrorResume(BadCredentialsException.class, e ->
                        Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS))
                )
                .map(authentication -> new TokenResponse(jwtTokenProvider.createAccessToken(authentication)));
    }
}
