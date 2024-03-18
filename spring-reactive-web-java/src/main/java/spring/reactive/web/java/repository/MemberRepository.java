package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.domain.Member;
import spring.reactive.web.java.repository.custom.MemberCustomRepository;

public interface MemberRepository extends R2dbcRepository<Member, Long>, MemberCustomRepository {

    Mono<Member> findByAccount(String account);

    @Query("""
            SELECT next_val
            FROM member_seq
            """)
    Mono<Long> findMemberSeq();
}
