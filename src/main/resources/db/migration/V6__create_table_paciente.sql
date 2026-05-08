CREATE TABLE paciente (
    id              UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id      UUID         NOT NULL UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    nome            VARCHAR(255) NOT NULL,
    cpf             VARCHAR(14)  NOT NULL UNIQUE,
    telefone        VARCHAR(20),
    data_nascimento DATE,
    endereco        VARCHAR(255),
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);