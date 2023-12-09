package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.dto.request.MemberSaveRequest;
import com.toy.shopwebflux.dto.request.MemberUpdateRequest;
import com.toy.shopwebflux.dto.response.ApiResponse;
import com.toy.shopwebflux.dto.response.MemberResponse;
import com.toy.shopwebflux.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Mono<ApiResponse<MemberResponse>> saveMember(@RequestBody MemberSaveRequest memberSaveRequest) {
        return memberService.saveMember(memberSaveRequest)
                .map(ApiResponse::new);
    }

    @GetMapping
    public Mono<ApiResponse<Page<MemberResponse>>> fineMembers(
            Pageable pageable,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String name
    ) {
        return memberService.findMembers(pageable, account, name)
                .map(ApiResponse::new);
    }

    @GetMapping("{id}")
    public Mono<ApiResponse<MemberResponse>> findMember(@PathVariable Long id) {
        return memberService.findMember(id)
                .map(ApiResponse::new);
    }

    @PatchMapping("{id}")
    public Mono<ApiResponse<MemberResponse>> updateMember(
            @PathVariable Long id,
            @RequestBody MemberUpdateRequest memberUpdateRequest
    ) {
        return memberService.updateMember(id, memberUpdateRequest)
                .map(ApiResponse::new);
    }

    @DeleteMapping("{id}")
    public Mono<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        return memberService.deleteMember(id)
                .thenReturn(new ApiResponse<>());
    }
}
