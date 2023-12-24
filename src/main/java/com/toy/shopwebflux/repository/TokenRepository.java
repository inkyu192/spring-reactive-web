package com.toy.shopwebflux.repository;

import com.toy.shopwebflux.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<Token> save(Token token) {
        ReactiveSetOperations<String, String> set = reactiveRedisTemplate.opsForSet();
        ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();


        return Mono.zip(
                        set.add("token", token.getAccount()),
                        hashOps.put("token:" + token.getAccount(), "account", token.getAccount()),
                        hashOps.put("token:" + token.getAccount(), "refreshToken", token.getRefreshToken()),

                        hashOps.put("token:" + token.getAccount(), "_class", token.getClass().getName())
                )
                .thenReturn(token);
    }

    // Token findById account
}
