package com.toy.shopwebflux.service;

import com.toy.shopwebflux.config.security.JwtTokenProvider;
import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.domain.Token;
import com.toy.shopwebflux.dto.request.LoginRequest;
import com.toy.shopwebflux.dto.response.TokenResponse;
import com.toy.shopwebflux.exception.CommonException;
import com.toy.shopwebflux.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;

    public Mono<TokenResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        return reactiveAuthenticationManager.authenticate(usernamePasswordAuthenticationToken)
                .onErrorResume(BadCredentialsException.class, e ->
                        Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS))
                )
                .flatMap(authentication -> {
                    String accessToken = jwtTokenProvider.createAccessToken(authentication);
                    String refreshToken = jwtTokenProvider.createRefreshToken();

                    return tokenRepository.save(Token.create(authentication.getName(), refreshToken))
                            .thenReturn(new TokenResponse(accessToken, refreshToken));
                });
    }
}
