package com.toy.shopwebflux.repository;

import com.toy.shopwebflux.domain.Member;
import com.toy.shopwebflux.repository.custom.MemberCustomRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<Member, Long>, MemberCustomRepository {

    @Query("""
                SELECT m.*
                FROM member m
                WHERE m.account = :account
            """)
    Mono<Member> findByAccount(@Param("account") String account);

    @Query("""
            SELECT MAX(member_id)
            FROM member m
            """)
    Mono<Long> findMaxMemberId();
}
