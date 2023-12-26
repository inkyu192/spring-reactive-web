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
        ReactiveSetOperations<String, String> reactiveSetOperations = reactiveRedisTemplate.opsForSet();
        ReactiveHashOperations<String, String, String> reactiveHashOperations = reactiveRedisTemplate.opsForHash();

        return Mono.zip(
                        reactiveSetOperations.add("token", token.getAccount()),
                        reactiveHashOperations.put("token:" + token.getAccount(), "account", token.getAccount()),
                        reactiveHashOperations.put("token:" + token.getAccount(), "refreshToken", token.getRefreshToken()),
                        reactiveHashOperations.put("token:" + token.getAccount(), "_class", token.getClass().getName())
                )
                .thenReturn(token);
    }

    public Mono<Token> findById(String id) {
        ReactiveHashOperations<String, String, String> reactiveHashOperations = reactiveRedisTemplate.opsForHash();

        return Mono.zip(
                        reactiveHashOperations.get("token:" + id, "account"),
                        reactiveHashOperations.get("token:" + id, "refreshToken")
                )
                .map(objects -> Token.create(objects.getT1(), objects.getT2()));
    }
}
