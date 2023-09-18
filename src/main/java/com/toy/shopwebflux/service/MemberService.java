package com.toy.shopwebflux.service;

import com.toy.shopwebflux.domain.Member;
import com.toy.shopwebflux.dto.member.MemberResponse;
import com.toy.shopwebflux.dto.member.MemberSaveRequest;
import com.toy.shopwebflux.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Flux<MemberResponse> findAll(String name) {
        return memberRepository.findAll(name)
                .map(MemberResponse::new);
    }

    @Transactional
    public Mono<MemberResponse> save(MemberSaveRequest memberSaveRequest) {
        return memberRepository.findMaxMemberId()
                .defaultIfEmpty(1L)
                .flatMap(memberId -> memberRepository.save(Member.createMember(memberId + 1, memberSaveRequest)))
                .map(MemberResponse::new);
    }
}
