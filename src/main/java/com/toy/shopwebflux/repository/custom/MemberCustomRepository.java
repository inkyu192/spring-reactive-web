package com.toy.shopwebflux.repository.custom;

import com.toy.shopwebflux.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface MemberCustomRepository {

    Mono<Page<Member>> findAllWithDatabaseClient(Pageable pageable, String account, String name);

    Mono<Page<Member>> findAllWithEntityTemplate(Pageable pageable, String account, String name);
}
