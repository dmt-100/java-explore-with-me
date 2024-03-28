DROP TABLE IF EXISTS requests cascade;
DROP TABLE IF EXISTS events cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS categories cascade;
DROP TABLE IF EXISTS compilations_events cascade;
DROP TABLE IF EXISTS compilations cascade;

CREATE TABLE IF NOT EXISTS users (
   id       BIGSERIAL PRIMARY KEY,
   name     VARCHAR(250) NOT NULL UNIQUE,
   email    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
   id       BIGSERIAL PRIMARY KEY ,
   name     VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
   id                 BIGSERIAL PRIMARY KEY,
   annotation         VARCHAR(2000),
   category_id        BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
   confirmed_requests INTEGER,
   created_on         TIMESTAMP NOT NULL,
   description        VARCHAR(7000),
   event_date         TIMESTAMP NOT NULL,
   initiator_id       BIGINT NOT NULL REFERENCES users(id),
   lat                REAL,
   lon                REAL,
   paid               BOOLEAN,
   participant_limit  INTEGER,
   published_on       TIMESTAMP,
   request_moderation BOOLEAN,
   state              VARCHAR(30),
   title              VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGSERIAL PRIMARY KEY,
    created      TIMESTAMP,
    event_id     BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status       VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS compilations (
    id        BIGSERIAL PRIMARY KEY,
    pinned    BOOLEAN,
    title     VARCHAR(210) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilations_id  BIGINT REFERENCES compilations(id) ON DELETE CASCADE,
    events_id        BIGINT REFERENCES events(id) ON DELETE CASCADE,
    PRIMARY KEY(compilations_id, events_id)
);