package com.devsolutions.medsys.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "role_id")
    private UUID roleId;
}
