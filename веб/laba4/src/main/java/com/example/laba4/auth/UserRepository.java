package com.example.laba4.auth;

import com.example.laba4.db.tables.Users;
import com.example.laba4.db.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class   UserRepository {
    private final DSLContext dsl;
    private final Users U = Users.USERS;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<UsersRecord> findByUsername(String username) {
        UsersRecord rec = dsl.selectFrom(U)
                .where(U.USERNAME.eq(username))
                .fetchOneInto(UsersRecord.class);
        return Optional.ofNullable(rec);
    }

    public UsersRecord create(String username, String passwordHash) {
        return dsl.insertInto(U)
                .set(U.USERNAME, username)
                .set(U.PASSWORD_HASH, passwordHash)
                .returning()
                .fetchOne();
    }
}
