package com.devsolutions.medsys.service;
import java.util.List;
import java.util.UUID;
import com.devsolutions.medsys.model.Patient;
import com.devsolutions.medsys.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }
    public List<Patient> findAll() {

        return patientRepository.findAll();
    }
    public Patient findById(UUID id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
    public void delete(UUID id) {

        Patient patient = findById(id);

        patientRepository.delete(patient);
    }
}