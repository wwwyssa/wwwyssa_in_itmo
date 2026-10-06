BEGIN;
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    CONSTRAINT ck_user_username CHECK (username ~ '^[a-z0-9_]{3,32}$')
);
COMMIT;
