CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE especialidade (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);