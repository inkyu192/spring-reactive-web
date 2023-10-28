package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.constant.Role;
import com.toy.shopwebflux.dto.request.MemberUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table
public class Member extends Base implements Persistable<Long> {

    @Id
    @Column("member_id")
    private Long id;
    private String account;
    private String password;
    private String name;
    private Role role;
    private String city;
    private String street;
    private String zipcode;

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    @Builder
    public Member(Long id, String account, String password, String name, String city, String street, String zipcode, Role role) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.role = role;
    }

    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        this.password = memberUpdateRequest.getPassword();
        this.name = memberUpdateRequest.getName();
        this.role = memberUpdateRequest.getRole();
        this.city = memberUpdateRequest.getCity();
        this.street = memberUpdateRequest.getStreet();
        this.zipcode = memberUpdateRequest.getZipcode();
    }
}
