package com.devsolutions.medsys.controller;

import com.devsolutions.medsys.config.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @MockitoBean
    private JwtUtil jwtUtil;

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