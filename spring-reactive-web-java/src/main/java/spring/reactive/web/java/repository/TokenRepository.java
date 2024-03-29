package spring.reactive.web.java.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.util.function.Tuples;
import spring.reactive.web.java.domain.Token;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<Token> save(Token token) {
        ReactiveSetOperations<String, String> reactiveSetOperations = reactiveRedisTemplate.opsForSet();
        ReactiveHashOperations<String, String, String> reactiveHashOperations = reactiveRedisTemplate.opsForHash();

        return Mono.when(
                        reactiveSetOperations.add("token", String.valueOf(token.getMemberId())),
                        reactiveHashOperations.put("token:" + token.getMemberId(), "memberId", String.valueOf(token.getMemberId())),
                        reactiveHashOperations.put("token:" + token.getMemberId(), "refreshToken", token.getRefreshToken()),
                        reactiveHashOperations.put("token:" + token.getMemberId(), "_class", token.getClass().getName())
                )
                .thenReturn(token);
    }

    public Mono<Token> findById(String id) {
        ReactiveHashOperations<String, String, String> reactiveHashOperations = reactiveRedisTemplate.opsForHash();

        return reactiveHashOperations.get("token:" + id, "memberId")
                .zipWith(reactiveHashOperations.get("token:" + id, "refreshToken"),
                        (memberId, refreshToken) -> Tuples.of(Long.valueOf(memberId), refreshToken))
                .map(TupleUtils.function(Token::create));
    }
}
