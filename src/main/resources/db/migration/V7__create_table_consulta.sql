CREATE TYPE status_consulta AS ENUM (
    'AGENDADA',
    'CONFIRMADA',
    'REALIZADA',
    'CANCELADA'
);

CREATE TABLE consulta (
    id          UUID            DEFAULT gen_random_uuid() PRIMARY KEY,
    medico_id   UUID            NOT NULL REFERENCES medico(id)   ON DELETE RESTRICT,
    paciente_id UUID            NOT NULL REFERENCES paciente(id) ON DELETE RESTRICT,
    data_hora   TIMESTAMP       NOT NULL,
    status      status_consulta NOT NULL DEFAULT 'AGENDADA',
    motivo      VARCHAR(500),
    observacoes TEXT,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_medico_data_hora UNIQUE (medico_id, data_hora)
);