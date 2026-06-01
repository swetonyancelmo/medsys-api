package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.prescription.PrescriptionRequestDTO;
import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.PrescriptionMapper;
import com.devsolutions.medsys.model.Appointment;
import com.devsolutions.medsys.model.Prescription;
import com.devsolutions.medsys.repository.AppointmentRepository;
import com.devsolutions.medsys.repository.PrescriptionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository repository;
    private final PrescriptionMapper mapper;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));

        repository.findByAppointmentId(dto.appointmentId()).ifPresent(p -> {
            throw new BusinessException("Já existe uma receita cadastrada para essa consulta.");
        });

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDescription(dto.description());
        prescription.setMedications(dto.medications());
        prescription.setExpiresAt(dto.expiresAt());
        prescription.setCreatedAt(LocalDateTime.now());

        Prescription saved = repository.save(prescription);

        return mapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "prescriptions", key = "#id")
    public PrescriptionResponseDTO findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada."));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "prescriptions", key = "'appt::' + #id")
    public PrescriptionResponseDTO findByAppointmentId (UUID id){
        return repository.findByAppointmentId(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada para a consulta informada."));
    }
}
