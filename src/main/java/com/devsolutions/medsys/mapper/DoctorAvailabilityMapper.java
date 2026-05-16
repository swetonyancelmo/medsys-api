package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.doctorAvailability.DoctorAvailabilityResponseDTO;
import com.devsolutions.medsys.model.DoctorAvailability;
import org.springframework.stereotype.Component;

@Component
public class DoctorAvailabilityMapper {
    public DoctorAvailabilityResponseDTO toDTO(DoctorAvailability entity) {
        if (entity == null) {
            return null;
        }
        return new DoctorAvailabilityResponseDTO(
                entity.getId(),
                entity.getDoctor().getId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.isActive()
        );
    }
}
