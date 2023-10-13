package com.toy.shopwebflux.repository.custom;

import com.toy.shopwebflux.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Member> findAllWithDatabaseClient(String name) {
        String sql = """
                SELECT m.* 
                FROM Member m 
                """;

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(name)) whereCondition.add("m.name like concat('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            sql += "WHERE ";
            sql += String.join(" and ", whereCondition);
        }

        DatabaseClient.GenericExecuteSpec createSql = databaseClient.sql(sql);

        if (StringUtils.hasText(name)) createSql.bind("name", name);

        return createSql
                .fetch()
                .all()
                .map(Member::createMember);
    }
}
