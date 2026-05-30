package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.DoctorAvailability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorAvailabilityMapperTest {

    private DoctorAvailabilityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DoctorAvailabilityMapper();
    }

    @Test
    void toDTO_comEntidadeNula_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    @Test
    void toDTO_comEntidadeValida_mapeiaCorretamente() {
        UUID doctorId = UUID.randomUUID();
        UUID availabilityId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Dr. Pedro")
                .build();

        DoctorAvailability availability = DoctorAvailability.builder()
                .id(availabilityId)
                .doctor(doctor)
                .dayOfWeek(2)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(17, 0))
                .active(true)
                .build();

        DoctorAvailabilityResponseDTO dto = mapper.toDTO(availability);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(availabilityId);
        assertThat(dto.doctorId()).isEqualTo(doctorId);
        assertThat(dto.dayOfWeek()).isEqualTo(2);
        assertThat(dto.startTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(dto.endTime()).isEqualTo(LocalTime.of(17, 0));
        assertThat(dto.active()).isTrue();
    }
}
