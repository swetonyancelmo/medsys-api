package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.model.DoctorAvailability;

import static org.mockito.ArgumentMatchers.any;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.AppointmentMapper;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.repository.AppointmentRepository;
import com.devsolutions.medsys.repository.DoctorAvailabilityRepository;
import com.devsolutions.medsys.repository.DoctorRepository;
import com.devsolutions.medsys.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository repository;

    @Mock
    private AppointmentMapper mapper;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Test
    void deveRetornarAgendamentoPorId() {

        UUID id = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(id)
                .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        id,
                        null,
                        null,
                        null,
                        null,
                        LocalDateTime.now(),
                        null,
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(repository.findById(id))
                .thenReturn(Optional.of(appointment));

        when(mapper.toDTO(appointment))
                .thenReturn(responseDTO);

        AppointmentResponseDTO resultado =
                appointmentService.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
    }

    @Test
    void deveLancarExcecaoQuandoAgendamentoNaoExistir() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.findById(id)
        );
    }
    @Test
    void deveCancelarAgendamento() {

        UUID id = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(id)
                .status(AppointmentStatus.SCHEDULED)
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .build();

        Appointment appointmentCancelado = Appointment.builder()
                .id(id)
                .status(AppointmentStatus.CANCELLED)
                .scheduledAt(appointment.getScheduledAt())
                .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        id,
                        null,
                        null,
                        null,
                        null,
                        appointment.getScheduledAt(),
                        AppointmentStatus.CANCELLED,
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(repository.findById(id))
                .thenReturn(Optional.of(appointment));

        when(repository.save(appointment))
                .thenReturn(appointmentCancelado);

        when(mapper.toDTO(appointmentCancelado))
                .thenReturn(responseDTO);

        AppointmentResponseDTO resultado =
                appointmentService.cancel(id);

        assertEquals(AppointmentStatus.CANCELLED, resultado.status());
    }
    @Test
    void deveLancarExcecaoAoCancelarAgendamentoJaCancelado() {

        UUID id = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(id)
                .status(AppointmentStatus.CANCELLED)
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                BusinessException.class,
                () -> appointmentService.cancel(id)
        );
    }
    @Test
    void deveLancarExcecaoAoCancelarAgendamentoJaRealizado() {

        UUID id = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(id)
                .status(AppointmentStatus.SCHEDULED)
                .scheduledAt(LocalDateTime.now().minusDays(1))
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                BusinessException.class,
                () -> appointmentService.cancel(id)
        );
    }
    @Test
    void deveRetornarConsultasPorPaciente() {

        UUID patientId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        appointment.getId(),
                        null,
                        null,
                        patientId,
                        null,
                        LocalDateTime.now(),
                        null,
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(repository.findByPatientIdOrderByScheduledAtDesc(patientId))
                .thenReturn(java.util.List.of(appointment));

        when(mapper.toDTO(appointment))
                .thenReturn(responseDTO);

        var resultado = appointmentService.findByPatient(patientId);

        assertEquals(1, resultado.size());
    }
    @Test
    void deveRetornarConsultasPorPeriodo() {

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusDays(7);

        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        appointment.getId(),
                        null,
                        null,
                        null,
                        null,
                        inicio,
                        null,
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(repository.findByScheduledAtBetweenOrderByScheduledAtAsc(inicio, fim))
                .thenReturn(java.util.List.of(appointment));

        when(mapper.toDTO(appointment))
                .thenReturn(responseDTO);

        var resultado = appointmentService.findByDateRange(inicio, fim);

        assertEquals(1, resultado.size());
    }
    @Test
    void deveLancarExcecaoQuandoDataInicialForMaiorQueDataFinal() {

        LocalDateTime inicio = LocalDateTime.now().plusDays(5);
        LocalDateTime fim = LocalDateTime.now();

        assertThrows(
                BusinessException.class,
                () -> appointmentService.findByDateRange(inicio, fim)
        );
    }
    @Test
    void deveRetornarConsultasPorMedicoEPeriodo() {

        UUID doctorId = UUID.randomUUID();

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusDays(7);

        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        appointment.getId(),
                        doctorId,
                        null,
                        null,
                        null,
                        inicio,
                        null,
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(repository.findByDoctorIdAndScheduledAtBetween(
                doctorId,
                inicio,
                fim))
                .thenReturn(java.util.List.of(appointment));

        when(mapper.toDTO(appointment))
                .thenReturn(responseDTO);

        var resultado = appointmentService.findByDoctorAndDateRange(
                doctorId,
                inicio,
                fim);

        assertEquals(1, resultado.size());
    }
    @Test
    void deveLancarExcecaoAoBuscarPorMedicoQuandoPeriodoForInvalido() {

        UUID doctorId = UUID.randomUUID();

        LocalDateTime inicio = LocalDateTime.now().plusDays(10);
        LocalDateTime fim = LocalDateTime.now();

        assertThrows(
                BusinessException.class,
                () -> appointmentService.findByDoctorAndDateRange(
                        doctorId,
                        inicio,
                        fim)
        );
    }
    @Test
    void deveAgendarConsultaComSucesso() {

        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        LocalDateTime dataConsulta =
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);

        AppointmentRequestDTO request =
                new AppointmentRequestDTO(
                        doctorId,
                        patientId,
                        dataConsulta,
                        "Consulta de rotina",
                        "Observações"
                );

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Dr. João")
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .name("Maria")
                .build();

        DoctorAvailability availability =
                DoctorAvailability.builder()
                        .doctor(doctor)
                        .dayOfWeek(dataConsulta.getDayOfWeek().getValue())
                        .startTime(java.time.LocalTime.of(8, 0))
                        .endTime(java.time.LocalTime.of(18, 0))
                        .active(true)
                        .build();

        Appointment appointment =
                Appointment.builder()
                        .doctor(doctor)
                        .patient(patient)
                        .scheduledAt(dataConsulta)
                        .status(AppointmentStatus.SCHEDULED)
                        .build();

        AppointmentResponseDTO responseDTO =
                new AppointmentResponseDTO(
                        UUID.randomUUID(),
                        doctorId,
                        "Dr. João",
                        patientId,
                        "Maria",
                        dataConsulta,
                        AppointmentStatus.SCHEDULED,
                        "Consulta de rotina",
                        "Observações",
                        LocalDateTime.now()
                );

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(doctorAvailabilityRepository
                .findByDoctorIdAndDayOfWeekAndActiveTrue(
                        doctorId,
                        dataConsulta.getDayOfWeek().getValue()))
                .thenReturn(Optional.of(availability));

        when(repository.findByDoctorIdAndScheduledAtAndStatusNot(
                doctorId,
                dataConsulta,
                AppointmentStatus.CANCELLED))
                .thenReturn(Optional.empty());

        when(repository.findByPatientIdAndScheduledAtAndStatusNot(
                patientId,
                dataConsulta,
                AppointmentStatus.CANCELLED))
                .thenReturn(Optional.empty());

        when(repository.saveAndFlush(any(Appointment.class)))
                .thenReturn(appointment);

        when(mapper.toDTO(appointment))
                .thenReturn(responseDTO);

        AppointmentResponseDTO resultado =
                appointmentService.schedule(request);

        assertNotNull(resultado);
        assertEquals(AppointmentStatus.SCHEDULED, resultado.status());
    }
    @Test
    void deveLancarExcecaoQuandoMedicoNaoExistir() {

        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        AppointmentRequestDTO request =
                new AppointmentRequestDTO(
                        doctorId,
                        patientId,
                        LocalDateTime.now().plusDays(1),
                        "Consulta",
                        "Observação"
                );

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.schedule(request)
        );
    }
    @Test
    void deveLancarExcecaoQuandoPacienteNaoExistir() {

        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        AppointmentRequestDTO request =
                new AppointmentRequestDTO(
                        doctorId,
                        patientId,
                        LocalDateTime.now().plusDays(1),
                        "Consulta",
                        "Observação"
                );

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Dr. João")
                .build();

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentService.schedule(request)
        );
    }
    @Test
    void deveLancarExcecaoQuandoMedicoNaoPossuirDisponibilidade() {

        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        LocalDateTime dataConsulta =
                LocalDateTime.now().plusDays(1);

        AppointmentRequestDTO request =
                new AppointmentRequestDTO(
                        doctorId,
                        patientId,
                        dataConsulta,
                        "Consulta",
                        "Observação"
                );

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Dr. João")
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .name("Maria")
                .build();

        when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(doctor));

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        when(doctorAvailabilityRepository
                .findByDoctorIdAndDayOfWeekAndActiveTrue(
                        doctorId,
                        dataConsulta.getDayOfWeek().getValue()))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> appointmentService.schedule(request)
        );
    }

}