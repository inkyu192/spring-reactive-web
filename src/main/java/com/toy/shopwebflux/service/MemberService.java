package com.toy.shopwebflux.service;

import com.toy.shopwebflux.constant.ApiResponseCode;
import com.toy.shopwebflux.domain.Member;
import com.toy.shopwebflux.dto.request.MemberSaveRequest;
import com.toy.shopwebflux.dto.request.MemberUpdateRequest;
import com.toy.shopwebflux.dto.response.MemberResponse;
import com.toy.shopwebflux.exception.CommonException;
import com.toy.shopwebflux.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Mono<MemberResponse> saveMember(MemberSaveRequest memberSaveRequest) {
        return memberRepository.findByAccount(memberSaveRequest.account())
                .defaultIfEmpty(Member.empty())
                .flatMap(member -> {
                    if (member.getMemberId() != null) {
                        return Mono.error(new CommonException(ApiResponseCode.DATA_DUPLICATE));
                    }

                    return memberRepository.findMaxMemberId()
                            .defaultIfEmpty(1L)
                            .flatMap(memberId -> memberRepository.save(
                                    Member.create(
                                            memberId + 1,
                                            memberSaveRequest.account(),
                                            memberSaveRequest.password(),
                                            memberSaveRequest.name(),
                                            memberSaveRequest.role(),
                                            memberSaveRequest.city(),
                                            memberSaveRequest.street(),
                                            memberSaveRequest.zipcode()
                                    )
                            ));
                })
                .map(MemberResponse::new);
    }

    public Mono<Page<MemberResponse>> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAllWithEntityTemplate(pageable, account, name)
                .map(page -> page.map(MemberResponse::new));
    }

    public Mono<MemberResponse> findMember(Long id) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)))
                .map(MemberResponse::new);
    }

    @Transactional
    public Mono<MemberResponse> updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)))
                .flatMap(member -> {
                    member.update(
                            memberUpdateRequest.name(),
                            memberUpdateRequest.role(),
                            memberUpdateRequest.city(),
                            memberUpdateRequest.street(),
                            memberUpdateRequest.zipcode()
                    );
                    return memberRepository.save(member);
                })
                .map(MemberResponse::new);
    }

    @Transactional
    public Mono<Void> deleteMember(Long id) {
        return memberRepository.deleteById(id);
    }
}
