package com.devsolutions.medsys.model;

import com.devsolutions.medsys.enums.TipoTelefone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "telefones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(
            regexp = "^\\+?55?\\s?\\(?[1-9]{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
            message = "Telefone inválido"
    )
    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false)
    private Boolean principal = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTelefone tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
}
