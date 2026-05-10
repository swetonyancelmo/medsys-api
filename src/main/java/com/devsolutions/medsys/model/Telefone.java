package com.devsolutions.medsys.model;

import com.devsolutions.medsys.enums.TipoTelefone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "telefone", indexes = {
        @Index(name = "idx_telefone_usuario", columnList = "usuario_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Pattern(
            regexp = "^\\+?55?\\s?\\(?[1-9]{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
            message = "Telefone inválido"
    )
    @Column(nullable = false, length = 20)
    private String numero;

    @Builder.Default
    @Column(nullable = false)
    private Boolean principal = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTelefone tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_telefone_usuario"))
    private User usuario;
}
