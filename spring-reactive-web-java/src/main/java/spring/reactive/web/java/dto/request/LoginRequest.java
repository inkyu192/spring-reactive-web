package spring.reactive.web.java.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
