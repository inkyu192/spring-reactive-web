package spring.reactive.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.domain.Member;

public interface MemberCustomRepository {

    Mono<Page<Member>> findAllWithDatabaseClient(Pageable pageable, String account, String name);
}
