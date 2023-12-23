package com.toy.shopwebflux.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
                .onErrorResume(UnsupportedJwtException.class, e -> setResponse(exchange, ApiResponseCode.UNSUPPORTED_TOKEN))
                .onErrorResume(ExpiredJwtException.class, e -> setResponse(exchange, ApiResponseCode.EXPIRED_TOKEN))
                .onErrorResume(JwtException.class, e -> setResponse(exchange, ApiResponseCode.BAD_TOKEN));
    }

    @SneakyThrows
    private Mono<Void> setResponse(ServerWebExchange exchange, ApiResponseCode apiResponseCode) {
        ServerHttpResponse response = exchange.getResponse();
        ApiResponse<String> apiResponse = new ApiResponse<>(apiResponseCode);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] responseData = objectMapper.writeValueAsBytes(apiResponse);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseData)));
    }
}
