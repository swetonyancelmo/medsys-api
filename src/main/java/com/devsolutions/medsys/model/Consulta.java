package com.devsolutions.medsys.model;

import com.devsolutions.medsys.enums.StatusConsulta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consulta", uniqueConstraints = {
        @UniqueConstraint(name = "uq_medico_data_hora", columnNames = {"medico_id", "data_hora"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false, foreignKey = @ForeignKey(name = "fk_consulta_medico"))
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_consulta_paciente"))
    private Paciente paciente;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @Column(length = 500)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Receita receita;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dataCriado;
}
