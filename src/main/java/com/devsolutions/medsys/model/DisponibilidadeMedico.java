package com.devsolutions.medsys.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "disponibilidade_medico",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_medico_dia", columnNames = {"medico_id", "dia_semana"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadeMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    public boolean isHorarioValido() {
        return horaInicio != null && horaFim != null && horaFim.isAfter(horaInicio);
    }
}
