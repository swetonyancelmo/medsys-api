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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityMapper mapper;

    @Transactional
    public DoctorAvailabilityResponseDTO registerAvailability(DoctorAvailabilityRequestDTO dto) {
        // 1. Valida se o médico existe
        Doctor doctor = doctorRepository.findById(dto.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        // 2. Instancia a entidade para executar a validação interna de consistência horária
        DoctorAvailability availability = repository.findByDoctorIdAndDayOfWeek(dto.doctorId(), dto.dayOfWeek())
                .orElse(new DoctorAvailability());

        availability.setDoctor(doctor);
        availability.setDayOfWeek(dto.dayOfWeek());
        availability.setStartTime(dto.startTime());
        availability.setEndTime(dto.endTime());
        availability.setActive(true);

        if (!availability.isScheduleValid()) {
            throw new BusinessException("Horário de término deve ser posterior ao horário de início.");
        }

        DoctorAvailability saved = repository.save(availability);
        return mapper.toDTO(saved);
    }

    @Transactional
    public List<DoctorAvailabilityResponseDTO> findActiveAvailabilitiesByDoctor(UUID doctorId) {
        return repository.findByDoctorIdAndActiveTrue(doctorId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void disableAvailability(UUID id) {
        DoctorAvailability availability = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilidade não encontrada."));
        availability.setActive(false);
        repository.save(availability);
    }
}
