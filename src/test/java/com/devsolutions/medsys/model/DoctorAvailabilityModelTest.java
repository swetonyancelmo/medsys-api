package com.devsolutions.medsys.model;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorAvailabilityModelTest {

    @Test
    void isScheduleValid_comHorariosValidos_retornaTrue() {
        DoctorAvailability availability = DoctorAvailability.builder()
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(17, 0))
                .build();

        assertThat(availability.isScheduleValid()).isTrue();
    }

    @Test
    void isScheduleValid_comStartTimeNulo_retornaFalse() {
        DoctorAvailability availability = DoctorAvailability.builder()
                .endTime(LocalTime.of(17, 0))
                .build();

        assertThat(availability.isScheduleValid()).isFalse();
    }

    @Test
    void isScheduleValid_comEndTimeNulo_retornaFalse() {
        DoctorAvailability availability = DoctorAvailability.builder()
                .startTime(LocalTime.of(8, 0))
                .build();

        assertThat(availability.isScheduleValid()).isFalse();
    }

    @Test
    void isScheduleValid_comEndTimeAntesDaInicio_retornaFalse() {
        DoctorAvailability availability = DoctorAvailability.builder()
                .startTime(LocalTime.of(17, 0))
                .endTime(LocalTime.of(8, 0))
                .build();

        assertThat(availability.isScheduleValid()).isFalse();
    }

    @Test
    void isScheduleValid_comEndTimeIgualAInicio_retornaFalse() {
        DoctorAvailability availability = DoctorAvailability.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 0))
                .build();

        assertThat(availability.isScheduleValid()).isFalse();
    }
}