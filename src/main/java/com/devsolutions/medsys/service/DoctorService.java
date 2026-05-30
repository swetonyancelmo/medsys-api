package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.dto.doctor.DoctorUpdateDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.DoctorMapper;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.DoctorRepository;
import com.devsolutions.medsys.repository.SpecialtyRepository;
import com.devsolutions.medsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    @CacheEvict(value = "doctors-by-specialty", key = "#dto.specialtyId")
    public DoctorResponseDTO create(DoctorRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Specialty specialty = specialtyRepository.findById(dto.specialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada"));

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(specialty)
                .name(dto.name())
                .crm(dto.crm())
                .phone(dto.phone())
                .appointmentDurationMin(dto.appointmentDurationMin() != null ? dto.appointmentDurationMin() : 30)
                .build();

        return doctorMapper.toDTO(doctorRepository.saveAndFlush(doctor));
    }

    @Transactional
    @CacheEvict(value = "doctors-by-specialty", key = "#dto.specialtyId")
    public DoctorResponseDTO createFromUser(User user, DoctorRegisterRequestDTO dto) {
        Specialty specialty = specialtyRepository.findById(dto.specialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada"));

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(specialty)
                .name(dto.name())
                .crm(dto.crm())
                .phone(dto.phone())
                .appointmentDurationMin(dto.appointmentDurationMin() != null ? dto.appointmentDurationMin() : 30)
                .build();

        return doctorMapper.toDTO(doctorRepository.saveAndFlush(doctor));
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponseDTO> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "doctors", key = "#id")
    public DoctorResponseDTO findById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
        return doctorMapper.toDTO(doctor);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "doctors-by-specialty", key = "#specialtyId")
    public List<DoctorResponseDTO> findBySpecialty(UUID specialtyId) {
        return doctorRepository.findBySpecialtyIdAndActiveTrue(specialtyId).stream()
                .map(doctorMapper::toDTO)
                .toList();
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "doctors", key = "#id"),
            evict = @CacheEvict(value = "doctors-by-specialty", allEntries = true)
    )
    public DoctorResponseDTO update(UUID id, DoctorUpdateDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) doctor.setName(dto.name());
        if (dto.phone() != null) doctor.setPhone(dto.phone());
        if (dto.appointmentDurationMin() != null) doctor.setAppointmentDurationMin(dto.appointmentDurationMin());
        if (dto.specialtyId() != null) {
            Specialty specialty = specialtyRepository.findById(dto.specialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada"));
            doctor.setSpecialty(specialty);
        }

        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "doctors", key = "#id"),
            @CacheEvict(value = "doctors-by-specialty", allEntries = true)
    })
    public void delete(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
        doctorRepository.delete(doctor);
    }
}
