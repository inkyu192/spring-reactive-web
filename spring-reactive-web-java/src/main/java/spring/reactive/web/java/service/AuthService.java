package spring.reactive.web.java.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.config.security.JwtTokenProvider;
import spring.reactive.web.java.constant.ApiResponseCode;
import spring.reactive.web.java.domain.Token;
import spring.reactive.web.java.dto.request.LoginRequest;
import spring.reactive.web.java.dto.request.TokenRequest;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.exception.CommonException;
import spring.reactive.web.java.repository.TokenRepository;

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
                .onErrorResume(authentication -> Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)))
                .zipWhen(authentication -> tokenRepository.save(
                        Token.create(authentication.getName(), jwtTokenProvider.createRefreshToken())
                ))
                .map(TupleUtils.function((authentication, token) -> {
                    String accessToken = jwtTokenProvider.createAccessToken(
                            authentication.getName(),
                            authentication.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining(","))
                    );

                    return new TokenResponse(accessToken, token.getRefreshToken());
                }));
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
                .filter(token -> tokenRequest.refreshToken().equals(token.getRefreshToken()))
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)))
                .map(token -> new TokenResponse(jwtTokenProvider.createAccessToken(account, authorities)));
    }
}
