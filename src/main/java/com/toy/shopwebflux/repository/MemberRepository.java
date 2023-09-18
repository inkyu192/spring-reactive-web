package com.toy.shopwebflux.repository;

import com.toy.shopwebflux.domain.Member;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<Member, Long> {

    @Query(
        "select m.* " +
        "from Member m " +
        "where (:name is null or m.name like concat('%', :name, '%'))"
    )
    Flux<Member> findAll(@Param("name") String name);

    @Query(
        "select max(member_id) " +
        "from member m"
    )
    Mono<Long> findMaxMemberId();
}
