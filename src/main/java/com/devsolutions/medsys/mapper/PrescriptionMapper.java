package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.prescription.PrescriptionResponseDTO;
import com.devsolutions.medsys.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public PrescriptionResponseDTO toDTO(Prescription prescription){
        if(prescription == null){
            return null;
        }
        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getAppointment().getId(),
                prescription.getDescription(),
                prescription.getMedications(),
                prescription.getExpiresAt(),
                prescription.getCreatedAt()
        );
    }
}
