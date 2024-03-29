package spring.reactive.web.java.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import spring.reactive.web.java.constant.Role;

@Getter
@Table
public class Member extends Base implements Persistable<Long> {

    @Id
    private Long memberId;
    private String account;
    private String password;
    private String name;
    private Role role;
    private String city;
    private String street;
    private String zipcode;

    @Override
    public Long getId() {
        return memberId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public static Member create(
            Long memberId,
            String account,
            String password,
            String name,
            Role role,
            String city,
            String street,
            String zipcode
    ) {
        Member member = new Member();

        member.memberId = memberId;
        member.account = account;
        member.password = password;
        member.name = name;
        member.role = role;
        member.city = city;
        member.street = street;
        member.zipcode = zipcode;

        return member;
    }

    public void update(String name, Role role, String city, String street, String zipcode) {
        this.name = name;
        this.role = role;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
