CREATE TABLE prescription (
    id             UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    appointment_id UUID      NOT NULL UNIQUE REFERENCES appointment(id) ON DELETE CASCADE,
    description    TEXT      NOT NULL,
    medications    TEXT      NOT NULL,
    expires_at     DATE,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW()
);
