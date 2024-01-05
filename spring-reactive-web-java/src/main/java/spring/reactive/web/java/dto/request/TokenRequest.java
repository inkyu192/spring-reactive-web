package spring.reactive.web.java.dto.request;

public record TokenRequest(
        String accessToken,
        String refreshToken
) {
}
