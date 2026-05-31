package com.devsolutions.medsys.service;

import java.time.LocalDate;
import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.PrescriptionMapper;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.model.Prescription;
import com.devsolutions.medsys.repository.AppointmentRepository;
import com.devsolutions.medsys.repository.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @InjectMocks
    private PrescriptionService service;

    @Mock
    private PrescriptionRepository repository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PrescriptionMapper mapper;

    @Test
    void deveCriarPrescricaoComSucesso() {
        UUID appointmentId = UUID.randomUUID();

        PrescriptionRequestDTO dto = mock(PrescriptionRequestDTO.class);
        when(dto.appointmentId()).thenReturn(appointmentId);
        when(dto.description()).thenReturn("Dor de cabeça");
        when(dto.medications()).thenReturn("Dipirona");
        Appointment appointment = new Appointment();

        Prescription saved = new Prescription();
        PrescriptionResponseDTO response = mock(PrescriptionResponseDTO.class);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(repository.findByAppointmentId(appointmentId))
                .thenReturn(Optional.empty());

        when(repository.save(any(Prescription.class)))
                .thenReturn(saved);

        when(mapper.toDTO(saved))
                .thenReturn(response);

        PrescriptionResponseDTO result = service.createPrescription(dto);

        assertNotNull(result);
        verify(repository).save(any(Prescription.class));
    }

    @Test
    void deveFalharQuandoConsultaNaoExiste() {
        UUID appointmentId = UUID.randomUUID();

        PrescriptionRequestDTO dto = mock(PrescriptionRequestDTO.class);
        when(dto.appointmentId()).thenReturn(appointmentId);

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createPrescription(dto));
    }

    @Test
    void deveFalharQuandoJaExistePrescricao() {
        UUID appointmentId = UUID.randomUUID();

        PrescriptionRequestDTO dto = mock(PrescriptionRequestDTO.class);
        when(dto.appointmentId()).thenReturn(appointmentId);

        Appointment appointment = new Appointment();

        Prescription existing = new Prescription();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        when(repository.findByAppointmentId(appointmentId))
                .thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class,
                () -> service.createPrescription(dto));
    }

    @Test
    void deveBuscarPrescricaoPorId() {
        UUID id = UUID.randomUUID();

        Prescription prescription = new Prescription();
        PrescriptionResponseDTO response = mock(PrescriptionResponseDTO.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(prescription));

        when(mapper.toDTO(prescription))
                .thenReturn(response);

        PrescriptionResponseDTO result = service.findById(id);

        assertNotNull(result);
    }

    @Test
    void deveFalharAoBuscarPrescricaoInexistente() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(id));
    }

    @Test
    void deveBuscarPorAppointmentId() {
        UUID appointmentId = UUID.randomUUID();

        Prescription prescription = new Prescription();
        PrescriptionResponseDTO response = mock(PrescriptionResponseDTO.class);

        when(repository.findByAppointmentId(appointmentId))
                .thenReturn(Optional.of(prescription));

        when(mapper.toDTO(prescription))
                .thenReturn(response);

        PrescriptionResponseDTO result = service.findByAppointmentId(appointmentId);

        assertNotNull(result);
    }

    @Test
    void deveFalharAoBuscarPorAppointmentIdInexistente() {
        UUID appointmentId = UUID.randomUUID();

        when(repository.findByAppointmentId(appointmentId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByAppointmentId(appointmentId));
    }
}