package spring.reactive.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.config.security.JwtTokenProvider;
import spring.reactive.web.java.config.security.UserDetailsImpl;
import spring.reactive.web.java.constant.ApiResponseCode;
import spring.reactive.web.java.domain.Member;
import spring.reactive.web.java.domain.Token;
import spring.reactive.web.java.dto.request.LoginRequest;
import spring.reactive.web.java.dto.request.MemberSaveRequest;
import spring.reactive.web.java.dto.request.MemberUpdateRequest;
import spring.reactive.web.java.dto.response.MemberResponse;
import spring.reactive.web.java.dto.response.TokenResponse;
import spring.reactive.web.java.exception.CommonException;
import spring.reactive.web.java.repository.MemberRepository;
import spring.reactive.web.java.repository.TokenRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Mono<MemberResponse> saveMember(MemberSaveRequest memberSaveRequest) {
        return memberRepository.findByAccount(memberSaveRequest.account())
                .flatMap(member -> Mono.error(new CommonException(ApiResponseCode.DATA_DUPLICATE)))
                .switchIfEmpty(Mono.defer(() -> memberRepository.findMemberSeq()
                        .flatMap(id -> memberRepository.save(
                                Member.create(
                                        id + 1,
                                        memberSaveRequest.account(),
                                        passwordEncoder.encode(memberSaveRequest.password()),
                                        memberSaveRequest.name(),
                                        memberSaveRequest.role(),
                                        memberSaveRequest.city(),
                                        memberSaveRequest.street(),
                                        memberSaveRequest.zipcode()
                                )
                        ))
                ))
                .cast(Member.class)
                .map(MemberResponse::new);
    }

    public Mono<TokenResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        return reactiveAuthenticationManager.authenticate(usernamePasswordAuthenticationToken)
                .map(authentication -> (UserDetailsImpl) authentication.getPrincipal())
                .zipWhen(userDetails -> tokenRepository.save(
                        Token.create(userDetails.getMemberId(), jwtTokenProvider.createRefreshToken())
                ))
                .map(TupleUtils.function((userDetails, token) -> new TokenResponse(
                        jwtTokenProvider.createAccessToken(
                                userDetails.getMemberId(),
                                userDetails.getRole()
                        ),
                        token.getRefreshToken()
                )))
                .onErrorResume(authentication -> Mono.error(new CommonException(ApiResponseCode.BAD_CREDENTIALS)));
    }

    public Mono<Page<MemberResponse>> findMembers(Pageable pageable, String account, String name) {
        return memberRepository.findAllWithDatabaseClient(pageable, account, name)
                .map(page -> page.map(MemberResponse::new));
    }

    public Mono<MemberResponse> findMember(Long id) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommonException(ApiResponseCode.DATA_NOT_FOUND)))
                .map(MemberResponse::new);
    }

    @Transactional
    public Mono<MemberResponse> updateMember(Long id,   MemberUpdateRequest memberUpdateRequest) {
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
