package com.toy.shopwebflux.controller;

import com.toy.shopwebflux.common.ApiResponse;
import com.toy.shopwebflux.dto.member.MemberResponse;
import com.toy.shopwebflux.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
