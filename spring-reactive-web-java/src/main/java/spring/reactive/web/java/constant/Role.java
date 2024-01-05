package spring.reactive.web.java.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자");

    private final String description;
}
