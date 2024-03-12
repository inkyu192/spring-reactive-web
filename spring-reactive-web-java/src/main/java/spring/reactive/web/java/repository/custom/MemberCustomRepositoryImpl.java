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
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.domain.Member;

import java.util.List;

public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public MemberCustomRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        this.objectMapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Override
    public Mono<Page<Member>> findAllWithDatabaseClient(Pageable pageable, String account, String name) {
        String countSql = """
                SELECT COUNT(*) AS cnt
                FROM member
                WHERE 1 = 1
                """;

        String contentSql = """
                SELECT *
                FROM member
                WHERE 1 = 1
                """;


        if (StringUtils.hasText(account)) {
            countSql += " AND account LIKE CONCAT('%', '" + account + "', '%')";
            contentSql += " AND account LIKE CONCAT('%', '" + account + "', '%')";
        }

        if (StringUtils.hasText(name)) {
            countSql += " AND name LIKE CONCAT('%', '" + name + "', '%')";
            contentSql += " AND name LIKE CONCAT('%', '" + name + "', '%')";
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
                                .map(row -> objectMapper.convertValue(row, Member.class))
                                .collectList()
                )
                .map(tuple -> new PageImpl<>(tuple.getT2(), pageable, tuple.getT1()));
    }
}
