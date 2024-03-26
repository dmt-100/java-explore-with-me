DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS users (
   id       BIGSERIAL PRIMARY KEY,
   name     VARCHAR(255) NOT NULL,
   email    VARCHAR(512) NOT NULL UNIQUE
);