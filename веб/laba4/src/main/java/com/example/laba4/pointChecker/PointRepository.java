package com.example.laba4.pointChecker;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class PointRepository {
    private final DSLContext dsl;

    public PointRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void savePoint(Long userId, Double x, Double y, Integer r, Boolean hit) {
        dsl.insertInto(
                table("points"),
                field("user_id"),
                field("x"),
                field("y"),
                field("r"),
                field("hit")
        )
        .values(userId, x, y, r, hit)
        .execute();
    }
    
    public List<Map<String, Object>> findByUserId(Long userId) {
        return dsl.select()
                .from(table("points"))
                .where(field("user_id").eq(userId))
                .fetch()
                .intoMaps();
    }

}
