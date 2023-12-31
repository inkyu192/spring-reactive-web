package spring.reactive.web.java.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record TokenResponse(
        String accessToken,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken
) {
    public TokenResponse(String accessToken) {
        this(accessToken, null);
    }
}
