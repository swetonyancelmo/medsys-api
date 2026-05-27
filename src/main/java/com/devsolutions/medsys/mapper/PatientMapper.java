package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.patient.PatientResponseDTO;
import com.devsolutions.medsys.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponseDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        return new PatientResponseDTO(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getEmail(),
                patient.getName(),
                patient.getCpf(),
                patient.getPhone(),
                patient.getBirthDate(),
                patient.getAddress(),
                patient.getActive(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
