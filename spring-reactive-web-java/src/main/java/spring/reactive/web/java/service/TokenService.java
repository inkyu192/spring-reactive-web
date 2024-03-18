package spring.reactive.web.java.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.config.security.JwtTokenProvider;
import spring.reactive.web.java.constant.ApiResponseCode;
import spring.reactive.web.java.dto.request.TokenRequest;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.exception.CommonException;
import spring.reactive.web.java.repository.MemberRepository;
import spring.reactive.web.java.repository.TokenRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public Mono<TokenResponse> reissue(TokenRequest tokenRequest) {
        Claims claims;

        try {
            claims = jwtTokenProvider.parseAccessToken(tokenRequest.accessToken());
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        jwtTokenProvider.parseRefreshToken(tokenRequest.refreshToken());

        String memberId = String.valueOf(claims.get("memberId"));

        return tokenRepository.findById(memberId)
                .filter(token -> tokenRequest.refreshToken().equals(token.getRefreshToken()))
                .zipWith(memberRepository.findById(Long.valueOf(memberId)))
                .map(TupleUtils.function((token, member) -> new TokenResponse(
                        jwtTokenProvider.createAccessToken(
                                Long.valueOf(memberId),
                                member.getRole()
                        ),
                        token.getRefreshToken()
                )))
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)));
    }
}
