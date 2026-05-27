DROP INDEX IF EXISTS uq_doctor_scheduled_at_active;
DROP INDEX IF EXISTS uq_patient_scheduled_at_active;

ALTER TABLE appointment
    ALTER COLUMN status TYPE VARCHAR(255) USING status::VARCHAR;

CREATE UNIQUE INDEX uq_doctor_scheduled_at_active
    ON appointment (doctor_id, scheduled_at)
    WHERE status <> 'CANCELLED';

CREATE UNIQUE INDEX uq_patient_scheduled_at_active
    ON appointment (patient_id, scheduled_at)
    WHERE status <> 'CANCELLED';
