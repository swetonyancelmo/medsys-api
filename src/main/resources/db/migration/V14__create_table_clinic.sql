CREATE TABLE clinic (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    cnpj        VARCHAR(18)  NOT NULL UNIQUE,
    phone       VARCHAR(20),
    address     VARCHAR(255),
    email       VARCHAR(255),
    active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP
);
