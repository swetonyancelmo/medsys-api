package com.devsolutions.medsys.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Test
    void schedule() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByDoctorAndDateRange() {
    }

    @Test
    void findByPatient() {
    }

    @Test
    void findByDateRange() {
    }

    @Test
    void cancel() {
    }
}