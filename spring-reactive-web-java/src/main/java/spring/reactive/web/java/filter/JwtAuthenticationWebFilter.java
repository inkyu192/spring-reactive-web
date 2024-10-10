package spring.reactive.web.java.filter;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
import spring.reactive.web.java.config.security.JwtTokenProvider;
import spring.reactive.web.java.config.security.UserDetailsImpl;

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
        String accessToken = getAccessToken(exchange.getRequest());

        if (StringUtils.hasText(accessToken)) {
            Claims claims = jwtTokenProvider.parseAccessToken(accessToken);
            UserDetails userDetails = new UserDetailsImpl(claims);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            return super.filter(exchange, chain)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        return super.filter(exchange, chain);
    }

    public String getAccessToken(ServerHttpRequest request) {
        String accessToken = null;
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            accessToken = token.replace("Bearer ", "");
        }

        return accessToken;
    }
}
