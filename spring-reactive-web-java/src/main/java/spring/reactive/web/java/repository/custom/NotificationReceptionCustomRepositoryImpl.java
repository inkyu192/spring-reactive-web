package spring.reactive.web.java.repository.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import spring.reactive.web.java.domain.Notification;
import spring.reactive.web.java.domain.NotificationReception;

import java.util.List;

public class NotificationReceptionCustomRepositoryImpl implements NotificationReceptionCustomRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public NotificationReceptionCustomRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        this.objectMapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Override
    public Mono<Page<Tuple2<Notification, NotificationReception>>> findByMemberId(Pageable pageable, Long memberId) {
        String countSql = """
                SELECT COUNT(*) AS cnt
                FROM notification_reception
                WHERE 1 = 1
                """;

        String contentSql = """
                SELECT *
                FROM notification_reception nr
                JOIN notification n ON nr.notification_id = n.notification_id
                WHERE 1 = 1
                """;

        if (memberId != null) {
            countSql += " AND mr.member_id = " + memberId;
            contentSql += " AND mr.member_id = " + memberId;
        }

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<String> orderList = sort
                    .map(order -> order.getProperty() + " " + order.getDirection())
                    .toList();

            contentSql += " ORDER BY ";
            contentSql += String.join(",", orderList);
        }

        contentSql += " LIMIT " + pageable.getOffset() + ", " + pageable.getPageSize();

        return Mono.zip(
                        databaseClient.sql(countSql)
                                .fetch()
                                .one()
                                .map(row -> (long) row.get("cnt")),
                        databaseClient.sql(contentSql)
                                .fetch()
                                .all()
                                .map(row -> Tuples.of(
                                        objectMapper.convertValue(row, Notification.class),
                                        objectMapper.convertValue(row, NotificationReception.class)
                                ))
                                .collectList()
                )
                .map(TupleUtils.function((count, content) -> new PageImpl<>(content, pageable, count)));
    }
}
