package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.clinic.ClinicRequestDTO;
import com.devsolutions.medsys.dto.clinic.ClinicResponseDTO;
import com.devsolutions.medsys.dto.clinic.ClinicUpdateDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.model.Clinic;
import com.devsolutions.medsys.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository repository;

    @Transactional
    public ClinicResponseDTO create(ClinicRequestDTO dto) {
        repository.findByCnpj(dto.cnpj()).ifPresent(c -> {
            throw new BusinessException("CNPJ já cadastrado: " + dto.cnpj());
        });

        Clinic clinic = Clinic.builder()
                .name(dto.name())
                .cnpj(dto.cnpj())
                .phone(dto.phone())
                .address(dto.address())
                .email(dto.email())
                .build();

        return toDTO(repository.saveAndFlush(clinic));
    }

    @Transactional(readOnly = true)
    public Page<ClinicResponseDTO> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return repository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
        }
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "clinics", key = "#id")
    public ClinicResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));
    }

    @Transactional
    @CachePut(value = "clinics", key = "#id")
    public ClinicResponseDTO update(UUID id, ClinicUpdateDTO dto) {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));

        if (dto.name() != null && !dto.name().isBlank()) clinic.setName(dto.name());
        if (dto.phone() != null) clinic.setPhone(dto.phone());
        if (dto.address() != null) clinic.setAddress(dto.address());
        if (dto.email() != null) clinic.setEmail(dto.email());

        return toDTO(repository.save(clinic));
    }

    @Transactional
    @CacheEvict(value = "clinics", key = "#id")
    public void delete(UUID id) {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));
        repository.delete(clinic);
    }

    private ClinicResponseDTO toDTO(Clinic clinic) {
        return new ClinicResponseDTO(
                clinic.getId(),
                clinic.getName(),
                clinic.getCnpj(),
                clinic.getPhone(),
                clinic.getAddress(),
                clinic.getEmail(),
                clinic.getActive(),
                clinic.getCreatedAt(),
                clinic.getUpdatedAt()
        );
    }
}
