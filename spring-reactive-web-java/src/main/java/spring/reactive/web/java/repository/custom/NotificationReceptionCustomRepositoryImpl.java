package spring.reactive.web.java.repository.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.domain.NotificationReception;

import java.util.List;

@RequiredArgsConstructor
public class NotificationReceptionCustomRepositoryImpl implements NotificationReceptionCustomRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityOperations r2dbcEntityOperations;

    @Override
    public Mono<Page<NotificationReception>> findWithR2dbcEntityOperations(Pageable pageable, Long memberId) {
        Criteria criteria = Criteria.empty();

        if (memberId != null) criteria.and("member_id").is(memberId);

        Mono<Long> countMono = r2dbcEntityOperations
                .select(NotificationReception.class)
                .matching(Query.query(criteria))
                .count();

        Mono<List<NotificationReception>> contentMono = r2dbcEntityOperations
                .select(NotificationReception.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .collectList();

        return Mono.zip(countMono, contentMono)
                .map(TupleUtils.function((count, notificationReceptions) ->
                        new PageImpl<>(notificationReceptions, pageable, count)));
    }
}
