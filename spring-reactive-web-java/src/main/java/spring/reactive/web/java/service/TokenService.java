package spring.reactive.web.java.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.config.security.JwtTokenProvider;
import spring.reactive.web.java.constant.ApiResponseCode;
import spring.reactive.web.java.dto.request.TokenRequest;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.exception.CommonException;
import spring.reactive.web.java.repository.TokenRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public Mono<TokenResponse> reissue(TokenRequest tokenRequest) {
        Claims claims;

        try {
            claims = jwtTokenProvider.parseAccessToken(tokenRequest.accessToken());
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        jwtTokenProvider.parseRefreshToken(tokenRequest.refreshToken());

        String account = (String) claims.get("account");
        String authorities = claims.get("authorities").toString();

        return tokenRepository.findById(account)
                .filter(token -> tokenRequest.refreshToken().equals(token.getRefreshToken()))
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)))
                .map(token -> new TokenResponse(jwtTokenProvider.createAccessToken(account, authorities)));
    }
}
