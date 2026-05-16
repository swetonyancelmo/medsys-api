package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.appointment.AppointmentRequestDTO;
import com.devsolutions.medsys.dto.appointment.AppointmentResponseDTO;
import com.devsolutions.medsys.enums.AppointmentStatus;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.AppointmentMapper;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.repository.AppointmentRepository;
import com.devsolutions.medsys.repository.DoctorRepository;
import com.devsolutions.medsys.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;


    @Transactional
    public AppointmentResponseDTO schedule(AppointmentRequestDTO dto){
        Doctor doctor = doctorRepository.findById(dto.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com o ID fornecido."));

        Patient patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID fornecido."));


        boolean hasConclict = repository.findByDoctorIdAndScheduledAt(dto.doctorId(), dto.scheduledAt()).isPresent();

        if(hasConclict){
            throw new BusinessException("O médico já possui um agendamento para esse horário.");
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(dto.scheduledAt())
                .reason(dto.reason())
                .notes(dto.notes())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = repository.save(appointment);
        return mapper.toDTO(saved);
    }

    @Transactional
    public AppointmentResponseDTO findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));
    }

    @Transactional
    public List<AppointmentResponseDTO> findByDoctorAndDateRange(UUID id, LocalDateTime start, LocalDateTime end){
        return repository.findByDoctorIdAndScheduledAtBetween(id,start,end)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentResponseDTO> findByPatient(UUID id){
        return repository.findByPatientIdOrderByScheduledAtDesc(id)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentResponseDTO> findByDateRange(LocalDateTime start, LocalDateTime end){
        return repository.findByScheduledAtBetweenOrderByScheduledAtAsc(start,end)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public AppointmentResponseDTO cancel(UUID id){
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        if(appointment.getStatus() != AppointmentStatus.SCHEDULED){
            throw new BusinessException("Apenas Agendamento com status de AGENDADO pode ser cancelado.");
        }

        if(appointment.getScheduledAt().isBefore(LocalDateTime.now())){
            throw new BusinessException("Não é possível cancelar um agendamento que já ocorreu.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = repository.save(appointment);

        return mapper.toDTO(saved);
    }
}
