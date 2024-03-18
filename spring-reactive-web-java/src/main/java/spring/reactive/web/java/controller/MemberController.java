package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.config.security.UserDetailsImpl;
import spring.reactive.web.java.dto.request.LoginRequest;
import spring.reactive.web.java.dto.request.MemberSaveRequest;
import spring.reactive.web.java.dto.request.MemberUpdateRequest;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.MemberResponse;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.service.MemberService;

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

    @PostMapping("login")
    public Mono<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest)
                .map(ApiResponse::new);
    }

    public Mono<ApiResponse<Page<MemberResponse>>> fineMembers(
            Pageable pageable,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String name
    ) {
        return memberService.findMembers(pageable, account, name)
                .map(ApiResponse::new);
    }

    @GetMapping
    public Mono<ApiResponse<MemberResponse>> findMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.findMember(userDetails.getMemberId())
                .map(ApiResponse::new);
    }

    @PatchMapping
    public Mono<ApiResponse<MemberResponse>> updateMember(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MemberUpdateRequest memberUpdateRequest
    ) {
        return memberService.updateMember(userDetails.getMemberId(), memberUpdateRequest)
                .map(ApiResponse::new);
    }

    @DeleteMapping
    public Mono<ApiResponse<Void>> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.deleteMember(userDetails.getMemberId())
                .thenReturn(new ApiResponse<>());
    }
}
