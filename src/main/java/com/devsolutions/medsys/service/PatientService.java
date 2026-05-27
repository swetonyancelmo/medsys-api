package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.auth.PatientRegisterRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientRequestDTO;
import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.dto.patient.PatientUpdateDTO;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.mapper.PatientMapper;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.PatientRepository;
import com.devsolutions.medsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponseDTO create(PatientRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Patient patient = Patient.builder()
                .user(user)
                .name(dto.name())
                .cpf(dto.cpf())
                .phone(dto.phone())
                .birthDate(dto.birthDate())
                .address(dto.address())
                .build();

        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Transactional
    public PatientResponseDTO createFromUser(User user, PatientRegisterRequestDTO dto) {
        Patient patient = Patient.builder()
                .user(user)
                .name(dto.name())
                .cpf(dto.cpf())
                .phone(dto.phone())
                .birthDate(dto.birthDate())
                .address(dto.address())
                .createdAt(LocalDateTime.now())
                .build();

        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable).map(patientMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        return patientMapper.toDTO(patient);
    }

    @Transactional
    public PatientResponseDTO update(UUID id, PatientUpdateDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) patient.setName(dto.name());
        if (dto.phone() != null) patient.setPhone(dto.phone());
        if (dto.birthDate() != null) patient.setBirthDate(dto.birthDate());
        if (dto.address() != null) patient.setAddress(dto.address());

        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Transactional
    public void delete(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        patientRepository.delete(patient);
    }
}
