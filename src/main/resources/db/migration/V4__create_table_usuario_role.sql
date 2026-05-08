CREATE TABLE usuario_role (
    usuario_id UUID NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, role_id)
);