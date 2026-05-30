package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorMapperTest {

    private DoctorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DoctorMapper();
    }

    @Test
    void toDTO_comDoctorNulo_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_comDoctorValido_mapeiaCorretamente() {
        UUID userId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(userId)
                .email("medico@email.com")
                .password("senha")
                .build();

        Specialty specialty = Specialty.builder()
                .id(specialtyId)
                .name("Cardiologia")
                .build();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .user(user)
                .specialty(specialty)
                .name("Dr. Carlos")
                .crm("CRM-12345")
                .phone("11988887777")
                .appointmentDurationMin(30)
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        DoctorResponseDTO dto = mapper.toDTO(doctor);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(doctorId);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.email()).isEqualTo("medico@email.com");
        assertThat(dto.specialtyId()).isEqualTo(specialtyId);
        assertThat(dto.specialtyName()).isEqualTo("Cardiologia");
        assertThat(dto.name()).isEqualTo("Dr. Carlos");
        assertThat(dto.crm()).isEqualTo("CRM-12345");
        assertThat(dto.phone()).isEqualTo("11988887777");
        assertThat(dto.appointmentDurationMin()).isEqualTo(30);
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(now);
        assertThat(dto.updatedAt()).isEqualTo(now);
    }
}
