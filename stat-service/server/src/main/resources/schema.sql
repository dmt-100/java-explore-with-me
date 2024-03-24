DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE IF NOT EXISTS endpoint_hits (
    hit_id            BIGSERIAL PRIMARY KEY,
    app               VARCHAR(255) NOT NULL,
    uri               VARCHAR(255) NOT NULL,
    ip                VARCHAR(15) NOT NULL,
    request_timestamp TIMESTAMP
);