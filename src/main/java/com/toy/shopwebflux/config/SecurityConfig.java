package com.toy.shopwebflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebflux.config.security.JwtAuthenticationWebFilter;
import com.toy.shopwebflux.config.security.JwtExceptionFilter;
import com.toy.shopwebflux.config.security.JwtTokenProvider;
import com.toy.shopwebflux.config.security.ReactiveUserDetailsServiceImpl;
import com.toy.shopwebflux.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity serverHttpSecurity,
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            JwtAuthenticationWebFilter jwtAuthenticationWebFilter,
            JwtExceptionFilter jwtExceptionFilter
    ) {
        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(jwtExceptionFilter, SecurityWebFiltersOrder.FIRST)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/member").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(MemberRepository memberRepository) {
        return new ReactiveUserDetailsServiceImpl(memberRepository);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService reactiveUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
        return userDetailsRepositoryReactiveAuthenticationManager;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.access-token.key}") String accessTokenKey,
            @Value("${jwt.access-token.expiration-time}") long accessTokenExpirationTime
    ) {
        return new JwtTokenProvider(accessTokenKey, accessTokenExpirationTime);
    }

    @Bean
    public JwtAuthenticationWebFilter jwtAuthenticationWebFilter(
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        return new JwtAuthenticationWebFilter(reactiveAuthenticationManager, jwtTokenProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(ObjectMapper objectMapper) {
        return new JwtExceptionFilter(objectMapper);
    }
}
