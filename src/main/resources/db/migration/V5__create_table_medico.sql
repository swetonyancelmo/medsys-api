CREATE TABLE medico (
    id                   UUID                  DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id           UUID         NOT NULL UNIQUE REFERENCES usuario (id) ON DELETE CASCADE,
    especialidade_id     UUID         NOT NULL REFERENCES especialidade (id) ON DELETE RESTRICT,
    nome                 VARCHAR(255) NOT NULL,
    crm                  VARCHAR(20)  NOT NULL UNIQUE,
    telefone             VARCHAR(20),
    duracao_consulta_min INTEGER      NOT NULL DEFAULT 30,
    ativo                BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW()
);