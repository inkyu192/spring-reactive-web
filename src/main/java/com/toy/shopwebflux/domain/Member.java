package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.constant.Role;
import com.toy.shopwebflux.dto.request.MemberSaveRequest;
import com.toy.shopwebflux.dto.request.MemberUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

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

    public static Member createMember(Long id, MemberSaveRequest memberSaveRequest) {
        Member member = new Member();

        member.id = id;
        member.account = memberSaveRequest.getAccount();
        member.password = memberSaveRequest.getPassword();
        member.name = memberSaveRequest.getName();
        member.role = memberSaveRequest.getRole();
        member.city = memberSaveRequest.getCity();
        member.street = memberSaveRequest.getStreet();
        member.zipcode = memberSaveRequest.getZipcode();

        return member;
    }

    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        this.password = memberUpdateRequest.getPassword();
        this.name = memberUpdateRequest.getName();
        this.role = memberUpdateRequest.getRole();
        this.city = memberUpdateRequest.getCity();
        this.street = memberUpdateRequest.getStreet();
        this.zipcode = memberUpdateRequest.getZipcode();
    }

    public static Member createMember(Map<String, Object> row) {
        Member member = new Member();

        member.id = (Long) row.get("member_id");
        member.account = (String) row.get("account");
        member.password = (String) row.get("password");
        member.name = (String) row.get("name");
        member.role = Role.valueOf((String) row.get("role"));
        member.city = (String) row.get("city");
        member.street = (String) row.get("street");
        member.zipcode = (String) row.get("zipcode");

        return member;
    }
}
