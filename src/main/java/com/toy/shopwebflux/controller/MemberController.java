package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.common.ApiResponse;
import com.toy.shopwebflux.dto.member.MemberResponse;
import com.toy.shopwebflux.dto.member.MemberSaveRequest;
import com.toy.shopwebflux.dto.member.MemberUpdateRequest;
import com.toy.shopwebflux.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public Mono<ApiResponse<List<MemberResponse>>> members(@RequestParam(required = false) String name) {
        return memberService.findAll(name)
                .collectList()
                .map(ApiResponse::new);
    }

    @GetMapping("{id}")
    public Mono<ApiResponse<MemberResponse>> member(@PathVariable Long id) {
        return memberService.findById(id)
                .map(ApiResponse::new);
    }

    @PostMapping
    public Mono<ApiResponse<MemberResponse>> saveMember(@RequestBody MemberSaveRequest memberSaveRequest) {
        return memberService.save(memberSaveRequest)
                .map(ApiResponse::new);
    }

    @PutMapping("{id}")
    public Mono<ApiResponse<MemberResponse>> updateMember(@PathVariable Long id,
                                                          @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return memberService.update(id, memberUpdateRequest)
                .map(ApiResponse::new);
    }

    @DeleteMapping("{id}")
    public Mono<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        return memberService.delete(id)
                .thenReturn(new ApiResponse<>());
    }
}
