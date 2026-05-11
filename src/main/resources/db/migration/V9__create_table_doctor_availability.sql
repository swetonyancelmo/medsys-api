CREATE TABLE doctor_availability (
    id          UUID    DEFAULT gen_random_uuid() PRIMARY KEY,
    doctor_id   UUID    NOT NULL REFERENCES doctor(id) ON DELETE CASCADE,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
    start_time  TIME    NOT NULL,
    end_time    TIME    NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_doctor_day_of_week  UNIQUE (doctor_id, day_of_week),
    CONSTRAINT chk_schedule_valid     CHECK  (end_time > start_time)
);
