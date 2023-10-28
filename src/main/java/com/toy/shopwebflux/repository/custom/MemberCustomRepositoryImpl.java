package com.toy.shopwebflux.repository.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebflux.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<Member> findAllWithDatabaseClient(long offset, int pageSize, String account, String name) {
        String sql = "SELECT m.* FROM member m";

        List<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(account)) whereCondition.add("m.account LIKE CONCAT('%', '" + account + "', '%')");
        if (StringUtils.hasText(name)) whereCondition.add("m.name LIKE CONCAT('%', '" + name + "', '%')");

        if (!whereCondition.isEmpty()) {
            sql += " WHERE ";
            sql += String.join(" AND ", whereCondition);
        }

        sql += " LIMIT " + offset + ", " + pageSize;

        return databaseClient.sql(sql)
                .fetch()
                .all()
                .map(row -> objectMapper.convertValue(row, Member.class));
    }
}
