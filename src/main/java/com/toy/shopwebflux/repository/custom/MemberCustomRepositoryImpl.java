package com.toy.shopwebflux.repository.custom;

import com.toy.shopwebflux.constant.Role;
import com.toy.shopwebflux.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

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
                                .map(row -> Member.create(
                                        (Long) row.get("member_id"),
                                        (String) row.get("account"),
                                        (String) row.get("password"),
                                        (String) row.get("name"),
                                        Role.valueOf((String) row.get("role")),
                                        (String) row.get("city"),
                                        (String) row.get("street"),
                                        (String) row.get("zipcode")
                                ))
                                .collectList()
                )
                .flatMap(tuple -> Mono.just(new PageImpl<>(tuple.getT2(), pageable, tuple.getT1())));
    }

}
