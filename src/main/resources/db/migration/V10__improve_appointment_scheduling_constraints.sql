ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS uq_doctor_scheduled_at;

CREATE UNIQUE INDEX IF NOT EXISTS uq_doctor_scheduled_at_active
    ON appointment (doctor_id, scheduled_at)
    WHERE status <> 'CANCELLED';

CREATE UNIQUE INDEX IF NOT EXISTS uq_patient_scheduled_at_active
    ON appointment (patient_id, scheduled_at)
    WHERE status <> 'CANCELLED';