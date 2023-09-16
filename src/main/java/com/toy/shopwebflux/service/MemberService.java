package com.toy.shopwebflux.service;

import com.toy.shopwebflux.dto.member.MemberResponse;
import com.toy.shopwebflux.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Flux<MemberResponse> findAll(String name) {
        return memberRepository.findAll(name)
                .map(MemberResponse::new);
    }
}
