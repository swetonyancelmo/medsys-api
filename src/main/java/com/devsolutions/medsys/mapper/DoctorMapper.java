package com.devsolutions.medsys.mapper;

import com.devsolutions.medsys.dto.doctor.DoctorResponseDTO;
import com.devsolutions.medsys.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponseDTO toDTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getEmail(),
                doctor.getSpecialty().getId(),
                doctor.getSpecialty().getName(),
                doctor.getName(),
                doctor.getCrm(),
                doctor.getPhone(),
                doctor.getAppointmentDurationMin(),
                doctor.getActive(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt()
        );
    }
}
