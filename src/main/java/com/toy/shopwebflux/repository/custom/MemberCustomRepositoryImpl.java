package com.toy.shopwebflux.repository.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebflux.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

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

        contentSql += " LIMIT :offset, :pageSize";

        return Mono.zip(
                        databaseClient.sql(countSql)
                                .fetch()
                                .one()
                                .map(row -> (long) row.get("cnt")),
                        databaseClient.sql(contentSql)
                                .bind("offset", pageable.getOffset())
                                .bind("pageSize", pageable.getPageSize())
                                .fetch()
                                .all()
                                .map(row -> objectMapper.convertValue(row, Member.class))
                                .collectList()
                )
                .flatMap(tuple -> Mono.just(new PageImpl<>(tuple.getT2(), pageable, tuple.getT1())));
    }

}
