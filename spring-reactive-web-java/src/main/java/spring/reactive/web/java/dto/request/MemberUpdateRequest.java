package spring.reactive.web.java.dto.request;


import spring.reactive.web.java.constant.Role;

public record MemberUpdateRequest(
        String name,
        Role role,
        String city,
        String street,
        String zipcode
) {
}
