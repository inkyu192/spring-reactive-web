package com.toy.shopwebflux.repository;

import com.toy.shopwebflux.domain.Member;
import com.toy.shopwebflux.repository.custom.MemberCustomRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<Member, Long>, MemberCustomRepository {

    @Query("""
                 SELECT m.*
                 FROM member m
                 WHERE (:account IS NULL OR m.account LIKE CONCAT('%', :account, '%'))
                 AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%'))
                 LIMIT :offset, :pageSize
            """)
    Flux<Member> findAll(
            @Param("offset") long offset,
            @Param("pageSize") int pageSize,
            @Param("account") String account,
            @Param("name") String name
    );

    @Query("""
            SELECT MAX(member_id)
            FROM member m
            """)
    Mono<Long> findMaxMemberId();
}
