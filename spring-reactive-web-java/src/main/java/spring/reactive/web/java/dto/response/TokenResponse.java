package spring.reactive.web.java.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
