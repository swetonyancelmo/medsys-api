package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityRequestDTO;
import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.DoctorAvailabilityMapper;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.DoctorAvailability;
import com.devsolutions.medsys.repository.DoctorAvailabilityRepository;
import com.devsolutions.medsys.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorAvailabilityServiceTest {

    @InjectMocks
    private DoctorAvailabilityService service;

    @Mock
    private DoctorAvailabilityRepository repository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorAvailabilityMapper mapper;

    // ---------------- CREATE / REGISTER ----------------

    @Test
    void deveRegistrarDisponibilidadeComSucesso() {

        UUID doctorId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .build();

        DoctorAvailabilityRequestDTO dto =
                mock(DoctorAvailabilityRequestDTO.class);

        when(dto.doctorId()).thenReturn(doctorId);
        when(dto.dayOfWeek()).thenReturn(1);
        when(dto.startTime()).thenReturn(LocalTime.of(8, 0));
        when(dto.endTime()).thenReturn(LocalTime.of(12, 0));

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        when(repository.findByDoctorIdAndDayOfWeek(doctorId, 1))
                .thenReturn(Optional.empty());

        DoctorAvailability saved = DoctorAvailability.builder()
                .id(UUID.randomUUID())
                .doctor(doctor)
                .dayOfWeek(1)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .active(true)
                .build();

        when(repository.save(any())).thenReturn(saved);

        DoctorAvailabilityResponseDTO response =
                mock(DoctorAvailabilityResponseDTO.class);

        when(mapper.toDTO(saved)).thenReturn(response);

        DoctorAvailabilityResponseDTO result =
                service.registerAvailability(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any());
    }

    @Test
    void deveLancarErroSeMedicoNaoExistir() {

        DoctorAvailabilityRequestDTO dto =
                mock(DoctorAvailabilityRequestDTO.class);

        when(dto.doctorId()).thenReturn(UUID.randomUUID());

        when(doctorRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.registerAvailability(dto));
    }

    @Test
    void deveLancarErroSeHorarioInvalido() {

        UUID doctorId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .build();

        DoctorAvailabilityRequestDTO dto =
                mock(DoctorAvailabilityRequestDTO.class);

        when(dto.doctorId()).thenReturn(doctorId);
        when(dto.dayOfWeek()).thenReturn(1);
        when(dto.startTime()).thenReturn(LocalTime.of(12, 0));
        when(dto.endTime()).thenReturn(LocalTime.of(8, 0)); // inválido

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        when(repository.findByDoctorIdAndDayOfWeek(doctorId, 1))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.registerAvailability(dto));
    }

    // ---------------- FIND ACTIVE ----------------

    @Test
    void deveBuscarDisponibilidadesAtivas() {

        UUID doctorId = UUID.randomUUID();

        when(repository.findByDoctorIdAndActiveTrue(doctorId))
                .thenReturn(List.of());


        List<DoctorAvailabilityResponseDTO> result =
                service.findActiveAvailabilitiesByDoctor(doctorId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------- DISABLE ----------------

    @Test
    void deveDesativarDisponibilidade() {

        UUID id = UUID.randomUUID();

        DoctorAvailability availability =
                DoctorAvailability.builder()
                        .id(id)
                        .active(true)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(availability));

        when(repository.save(any()))
                .thenReturn(availability);

        assertDoesNotThrow(() ->
                service.disableAvailability(id));

        assertFalse(availability.isActive());
        verify(repository, times(1)).save(availability);
    }
}