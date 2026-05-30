package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentMapperTest {

    private AppointmentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AppointmentMapper();
    }

    @Test
    void toDTO_comAppointmentNulo_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_comAppointmentValido_mapeiaCorretamente() {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 6, 10, 9, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Dra. Ana")
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .name("Maria Souza")
                .build();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(scheduledAt)
                .status(AppointmentStatus.SCHEDULED)
                .reason("Consulta de rotina")
                .notes("Paciente com histórico de hipertensão")
                .createdAt(createdAt)
                .build();

        AppointmentResponseDTO dto = mapper.toDTO(appointment);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(appointmentId);
        assertThat(dto.doctorId()).isEqualTo(doctorId);
        assertThat(dto.doctorName()).isEqualTo("Dra. Ana");
        assertThat(dto.patientId()).isEqualTo(patientId);
        assertThat(dto.patientName()).isEqualTo("Maria Souza");
        assertThat(dto.scheduledAt()).isEqualTo(scheduledAt);
        assertThat(dto.status()).isEqualTo(AppointmentStatus.SCHEDULED);
        assertThat(dto.reason()).isEqualTo("Consulta de rotina");
        assertThat(dto.notes()).isEqualTo("Paciente com histórico de hipertensão");
        assertThat(dto.createdAt()).isEqualTo(createdAt);
    }
}
