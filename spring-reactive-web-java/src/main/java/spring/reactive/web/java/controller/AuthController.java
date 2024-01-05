package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.dto.request.LoginRequest;
import spring.reactive.web.java.dto.request.TokenRequest;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.service.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public Mono<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest)
                .map(ApiResponse::new);
    }

    @PostMapping("refresh")
    public Mono<ApiResponse<TokenResponse>> refresh(@RequestBody TokenRequest tokenRequest) {
        return authService.refresh(tokenRequest)
                .map(ApiResponse::new);
    }
}
