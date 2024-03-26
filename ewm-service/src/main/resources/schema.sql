DROP TABLE IF EXISTS events cascade ;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS users (
   id       BIGSERIAL PRIMARY KEY,
   name     VARCHAR(250) NOT NULL,
   email    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
   id       BIGSERIAL PRIMARY KEY,
   name     VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
   id                 BIGSERIAL PRIMARY KEY,
   annotation         VARCHAR(300),
   category_id        BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
   confirmed_requests INTEGER,
   created_on         TIMESTAMP NOT NULL,
   description        VARCHAR(600),
   event_date         TIMESTAMP NOT NULL,
   initiator_id       BIGINT NOT NULL REFERENCES users(id),
   lat                REAL,
   lon                REAL,
   paid               BOOLEAN,
   participant_limit  INTEGER,
   published_on       TIMESTAMP,
   request_moderation BOOLEAN,
   state              VARCHAR(30),
   title              VARCHAR(100)
);