package com.devsolutions.medsys.service;

import com.devsolutions.medsys.dto.specialty.SpecialtyRequestDTO;
import com.devsolutions.medsys.dto.specialty.SpecialtyResponseDTO;
import com.devsolutions.medsys.exception.BusinessException;
import com.devsolutions.medsys.exception.ResourceNotFoundException;
import com.devsolutions.medsys.model.Specialty;
import com.devsolutions.medsys.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Transactional
    @CacheEvict(value = "specialties", allEntries = true)
    public SpecialtyResponseDTO create(SpecialtyRequestDTO dto) {
        if (specialtyRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Especialidade já cadastrada com esse nome: " + dto.name());
        }

        Specialty specialty = Specialty.builder()
                .name(dto.name())
                .description(dto.description())
                .build();

        Specialty saved = specialtyRepository.save(specialty);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specialties", key = "'list::' + (#name != null ? #name.toLowerCase() : 'all')")
    public List<SpecialtyResponseDTO> findAll(String name) {
        List<Specialty> specialties = (name != null && !name.isBlank())
                ? specialtyRepository.findByNameContainingIgnoreCase(name)
                : specialtyRepository.findAll();
        return specialties.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specialties", key = "#id")
    public SpecialtyResponseDTO findById(UUID id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada."));
        return toDTO(specialty);
    }

    private SpecialtyResponseDTO toDTO(Specialty specialty) {
        return new SpecialtyResponseDTO(specialty.getId(), specialty.getName(), specialty.getDescription());
    }
}
