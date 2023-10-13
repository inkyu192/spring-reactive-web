package com.toy.shopwebflux.repository.custom;

import com.toy.shopwebflux.domain.Member;
import reactor.core.publisher.Flux;

public interface MemberCustomRepository {

    Flux<Member> findAllWithDatabaseClient(String name);
}
