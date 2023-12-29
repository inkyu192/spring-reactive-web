package com.toy.shopwebflux.service;

import com.toy.shopwebflux.config.security.JwtTokenProvider;
import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.domain.Token;
import com.toy.shopwebflux.dto.request.LoginRequest;
import com.toy.shopwebflux.dto.request.TokenRequest;
import com.toy.shopwebflux.dto.response.TokenResponse;
import com.toy.shopwebflux.exception.CommonException;
import com.toy.shopwebflux.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

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
                    String accessToken = jwtTokenProvider.createAccessToken(
                            authentication.getName(),
                            authentication.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining(","))
                    );
                    String refreshToken = jwtTokenProvider.createRefreshToken();

                    return tokenRepository.save(Token.create(authentication.getName(), refreshToken))
                            .thenReturn(new TokenResponse(accessToken, refreshToken));
                });
    }

    public Mono<TokenResponse> refresh(TokenRequest tokenRequest) {
        Claims claims;

        try {
            claims = jwtTokenProvider.parseAccessToken(tokenRequest.accessToken());
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        String account = (String) claims.get("account");
        String authorities = claims.get("authorities").toString();

        return tokenRepository.findById(account)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)))
                .flatMap(token -> {
                    String refreshToken = token.getRefreshToken();

                    if (!tokenRequest.refreshToken().equals(refreshToken)) {
                        return Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS));
                    }

                    return Mono.just(new TokenResponse(jwtTokenProvider.createAccessToken(account, authorities)));
                });
    }
}
