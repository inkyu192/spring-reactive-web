package com.toy.shopwebflux.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key accessTokenKey;
    private final long accessTokenExpirationTime;

    public JwtTokenProvider(
            @Value("${jwt.access-token.key}") String accessTokenKey,
            @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime
    ) {
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime * 60 * 1000;
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("account", authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationTime))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getAccessToken(ServerHttpRequest request) {
        String accessToken = null;
        String token = request.getHeaders().getFirst("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            accessToken = token.replace("Bearer ", "");
        }

        return accessToken;
    }

    public Claims parseClaims(String token) {
        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(accessTokenKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            throw new SecurityException("잘못된 토큰", e);
        }  catch (MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 토큰", e);
        }  catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 토큰", e);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 토큰", e);
        }

        return claims;
    }
}
