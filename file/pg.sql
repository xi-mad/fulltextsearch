CREATE TABLE articles (
    id SERIAL PRIMARY KEY,
    title TEXT,
    content TEXT,
    tokens TSVECTOR
);

CREATE EXTENSION pg_trgm;
CREATE INDEX idx_tokens ON articles USING GIN (tokens);


