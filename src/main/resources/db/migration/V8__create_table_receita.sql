CREATE TABLE receita (
    id           UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    consulta_id  UUID      NOT NULL UNIQUE REFERENCES consulta(id) ON DELETE CASCADE,
    descricao    TEXT      NOT NULL,
    medicamentos TEXT      NOT NULL,
    validade     DATE,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW()
);