package com.toy.shopwebflux.config.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationWebFilter(
            ReactiveAuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String accessToken = jwtTokenProvider.getAccessToken(exchange.getRequest());

        if (StringUtils.hasText(accessToken)) {
            Claims claims = jwtTokenProvider.parseClaims(accessToken);
            UserDetails userDetails = new UserDetailsImpl(claims);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            return super.filter(exchange, chain)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        return super.filter(exchange, chain);
    }
}
