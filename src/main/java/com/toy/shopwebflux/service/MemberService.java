package com.toy.shopwebflux.service;

import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.domain.Member;
import com.toy.shopwebflux.dto.request.MemberSaveRequest;
import com.toy.shopwebflux.dto.request.MemberUpdateRequest;
import com.toy.shopwebflux.dto.response.MemberResponse;
import com.toy.shopwebflux.exception.CommonException;
import com.toy.shopwebflux.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Mono<MemberResponse> saveMember(MemberSaveRequest memberSaveRequest) {
        return memberRepository.findByAccount(memberSaveRequest.getAccount())
                .defaultIfEmpty(Member.builder().build())
                .flatMap(member -> {
                    if (member.getId() != null)
                        return Mono.error(new CommonException(ApiResponseCode.DATA_DUPLICATE));

                    return memberRepository.findMaxMemberId()
                            .defaultIfEmpty(1L)
                            .flatMap(id -> memberRepository.save(
                                    Member.builder()
                                            .id(id + 1)
                                            .account(memberSaveRequest.getAccount())
                                            .password(memberSaveRequest.getPassword())
                                            .name(memberSaveRequest.getName())
                                            .city(memberSaveRequest.getCity())
                                            .street(memberSaveRequest.getStreet())
                                            .zipcode(memberSaveRequest.getZipcode())
                                            .role(memberSaveRequest.getRole())
                                            .build()
                            ));
                })
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getCity())
                        .street(member.getStreet())
                        .zipcode(member.getCity())
                        .role(member.getRole())
                        .build());
    }

    public Flux<MemberResponse> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAll(pageable.getOffset(), pageable.getPageSize(), account, name)
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getCity())
                        .street(member.getStreet())
                        .zipcode(member.getCity())
                        .role(member.getRole())
                        .build());
    }

    public Mono<MemberResponse> findMember(Long id) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)))
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getCity())
                        .street(member.getStreet())
                        .zipcode(member.getCity())
                        .role(member.getRole())
                        .build());
    }

    @Transactional
    public Mono<MemberResponse> updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)))
                .flatMap(member -> {
                    member.updateMember(memberUpdateRequest);
                    return memberRepository.save(member);
                })
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .account(member.getAccount())
                        .name(member.getName())
                        .city(member.getCity())
                        .street(member.getStreet())
                        .zipcode(member.getCity())
                        .role(member.getRole())
                        .build());
    }

    @Transactional
    public Mono<Void> deleteMember(Long id) {
        return memberRepository.deleteById(id);
    }
}
