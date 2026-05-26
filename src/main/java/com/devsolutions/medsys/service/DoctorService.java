package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.DoctorRegisterRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorRequestDTO;
import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.DoctorMapper;
import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.DoctorRepository;
import com.devsolutions.medsys.repository.SpecialtyRepository;
import com.devsolutions.medsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional
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

        return doctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> findAll() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public DoctorResponseDTO findById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
        return doctorMapper.toDTO(doctor);
    }

    @Transactional
    public void delete(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
        doctorRepository.delete(doctor);
    }
}
