package spring.reactive.web.java.dto.response;


import spring.reactive.web.java.constant.Role;
import spring.reactive.web.java.domain.Member;


public record MemberResponse(
        Long id,
        String account,
        String name,
        String city,
        String street,
        String zipcode,
        Role role
) {
    public MemberResponse(Member member) {
        this(
                member.getMemberId(),
                member.getAccount(),
                member.getName(),
                member.getCity(),
                member.getStreet(),
                member.getZipcode(),
                member.getRole()
        );
    }
}
