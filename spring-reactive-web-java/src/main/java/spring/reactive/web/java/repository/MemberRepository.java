package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.domain.Member;
import spring.reactive.web.java.repository.custom.MemberCustomRepository;

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
