CREATE TABLE patient (
    id         UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id    UUID         NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    cpf        VARCHAR(14)  NOT NULL UNIQUE,
    phone      VARCHAR(20),
    birth_date DATE,
    address    VARCHAR(255),
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);
