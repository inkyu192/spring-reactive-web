package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.dto.member.MemberSaveRequest;
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
    private String roleId;
    private String city;
    private String street;
    private String zipcode;

    public static Member createMember(Long id, MemberSaveRequest memberSaveRequest) {
        Member member = new Member();

        member.id = id;
        member.account = memberSaveRequest.getAccount();
        member.password = memberSaveRequest.getPassword();
        member.name = memberSaveRequest.getName();
        member.roleId = memberSaveRequest.getRoleId();
        member.city = memberSaveRequest.getCity();
        member.street = memberSaveRequest.getStreet();
        member.zipcode = memberSaveRequest.getZipcode();

        return member;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
