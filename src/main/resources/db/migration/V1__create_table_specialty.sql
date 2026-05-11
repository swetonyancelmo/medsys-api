CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE specialty (
    id          UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);
