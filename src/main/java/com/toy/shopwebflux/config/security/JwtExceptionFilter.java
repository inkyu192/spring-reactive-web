package com.toy.shopwebflux.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtExceptionFilter implements WebFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(SecurityException.class, e -> handleJwtException(exchange.getResponse(), "잘못된 토큰"))
                .onErrorResume(MalformedJwtException.class, e -> handleJwtException(exchange.getResponse(), "잘못된 토큰"))
                .onErrorResume(UnsupportedJwtException.class, e -> handleJwtException(exchange.getResponse(), "지원되지 않는 토큰"))
                .onErrorResume(ExpiredJwtException.class, e -> handleJwtException(exchange.getResponse(), "만료된 토큰"))
                .onErrorResume(JwtException.class, e -> handleJwtException(exchange.getResponse(), e.getMessage()));
    }

    @SneakyThrows
    private Mono<Void> handleJwtException(ServerHttpResponse response, String message) {
        ApiResponse<String> apiResponse = new ApiResponse<>(ApiResponseCode.BAD_REQUEST, message);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] responseData = objectMapper.writeValueAsBytes(apiResponse);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseData)));
    }
}
