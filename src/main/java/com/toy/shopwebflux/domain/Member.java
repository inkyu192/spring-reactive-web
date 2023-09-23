package com.toy.shopwebflux.domain;

import com.toy.shopwebflux.dto.member.MemberSaveRequest;
import com.toy.shopwebflux.dto.member.MemberUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

@Getter
@Builder
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

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public static Member createMember(Long id, MemberSaveRequest memberSaveRequest) {
        return Member.builder()
                .id(id)
                .account(memberSaveRequest.getAccount())
                .password(memberSaveRequest.getPassword())
                .name(memberSaveRequest.getName())
                .roleId(memberSaveRequest.getRoleId())
                .city(memberSaveRequest.getCity())
                .street(memberSaveRequest.getStreet())
                .zipcode(memberSaveRequest.getStreet())
                .build();
    }

    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        this.password = memberUpdateRequest.getPassword();
        this.name = memberUpdateRequest.getName();
        this.roleId = memberUpdateRequest.getRoleId();
        this.city = memberUpdateRequest.getCity();
        this.street = memberUpdateRequest.getStreet();
        this.zipcode = memberUpdateRequest.getZipcode();
    }
}
