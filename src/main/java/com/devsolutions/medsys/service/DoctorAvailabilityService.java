package com.devsolutions.medsys.service;

import com.devsolutions.medsys.repository.DoctorAvailabilityRepository;
import com.devsolutions.medsys.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;
    private final DoctorRepository doctorRepository;
}
