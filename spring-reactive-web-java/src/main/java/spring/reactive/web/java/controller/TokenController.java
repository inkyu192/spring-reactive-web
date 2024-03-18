package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.dto.request.TokenRequest;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.service.TokenService;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("reissue")
    public Mono<ApiResponse<TokenResponse>> reissue(@RequestBody TokenRequest tokenRequest) {
        return tokenService.reissue(tokenRequest)
                .map(ApiResponse::new);
    }
}
