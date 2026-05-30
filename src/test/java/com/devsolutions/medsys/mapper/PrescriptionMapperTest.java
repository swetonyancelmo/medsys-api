package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.model.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PrescriptionMapperTest {

    private PrescriptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PrescriptionMapper();
    }

    @Test
    void toDTO_comPrescriptionNula_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_comPrescriptionValida_mapeiaCorretamente() {
        UUID appointmentId = UUID.randomUUID();
        UUID prescriptionId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDate expiresAt = LocalDate.of(2026, 12, 31);

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .build();

        Prescription prescription = Prescription.builder()
                .id(prescriptionId)
                .appointment(appointment)
                .description("Repouso por 3 dias e hidratação constante.")
                .medications("Paracetamol 500mg - 1 comprimido a cada 8h")
                .expiresAt(expiresAt)
                .createdAt(createdAt)
                .build();

        PrescriptionResponseDTO dto = mapper.toDTO(prescription);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(prescriptionId);
        assertThat(dto.appointmentId()).isEqualTo(appointmentId);
        assertThat(dto.description()).isEqualTo("Repouso por 3 dias e hidratação constante.");
        assertThat(dto.medications()).isEqualTo("Paracetamol 500mg - 1 comprimido a cada 8h");
        assertThat(dto.expiresAt()).isEqualTo(expiresAt);
        assertThat(dto.createdAt()).isEqualTo(createdAt);
    }
}
