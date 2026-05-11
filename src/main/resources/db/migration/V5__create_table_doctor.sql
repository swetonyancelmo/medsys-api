CREATE TABLE doctor (
    id                       UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id                  UUID         NOT NULL UNIQUE REFERENCES users(id)     ON DELETE CASCADE,
    specialty_id             UUID         NOT NULL REFERENCES specialty(id)        ON DELETE RESTRICT,
    name                     VARCHAR(255) NOT NULL,
    crm                      VARCHAR(20)  NOT NULL UNIQUE,
    phone                    VARCHAR(20),
    appointment_duration_min INTEGER      NOT NULL DEFAULT 30,
    active                   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at               TIMESTAMP    NOT NULL DEFAULT NOW()
);
