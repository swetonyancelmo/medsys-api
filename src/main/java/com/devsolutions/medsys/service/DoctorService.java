package com.devsolutions.medsys.service;

import com.devsolutions.medsys.model.Doctor;
import com.devsolutions.medsys.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor create(Doctor doctor) {

        return doctorRepository.save(doctor);
    }

    public List<Doctor> findAll() {

        return doctorRepository.findAll();
    }
    public Doctor findById(UUID id) {

        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    public void delete(UUID id) {

        Doctor doctor = findById(id);

        doctorRepository.delete(doctor);
    }
}