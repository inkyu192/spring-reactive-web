package spring.reactive.web.java.dto.request;


import spring.reactive.web.java.constant.Role;

public record MemberSaveRequest(
        String account,
        String password,
        String name,
        Role role,
        String city,
        String street,
        String zipcode
) {
}
