CREATE TYPE appointment_status AS ENUM (
    'SCHEDULED',
    'CONFIRMED',
    'COMPLETED',
    'CANCELLED'
);

CREATE TABLE appointment (
    id           UUID               DEFAULT gen_random_uuid() PRIMARY KEY,
    doctor_id    UUID               NOT NULL REFERENCES doctor(id)  ON DELETE RESTRICT,
    patient_id   UUID               NOT NULL REFERENCES patient(id) ON DELETE RESTRICT,
    scheduled_at TIMESTAMP          NOT NULL,
    status       appointment_status NOT NULL DEFAULT 'SCHEDULED',
    reason       VARCHAR(500),
    notes        TEXT,
    created_at   TIMESTAMP          NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_doctor_scheduled_at UNIQUE (doctor_id, scheduled_at)
);
