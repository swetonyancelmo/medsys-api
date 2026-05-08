CREATE TABLE disponibilidade_medico (
    id          UUID    DEFAULT gen_random_uuid() PRIMARY KEY,
    medico_id   UUID    NOT NULL REFERENCES medico(id) ON DELETE CASCADE,
    dia_semana  INTEGER NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio TIME    NOT NULL,
    hora_fim    TIME    NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_medico_dia        UNIQUE (medico_id, dia_semana),
    CONSTRAINT chk_horario_valido   CHECK  (hora_fim > hora_inicio)
);
