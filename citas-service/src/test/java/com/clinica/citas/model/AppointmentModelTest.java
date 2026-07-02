package com.clinica.citas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Appointment Model Tests")
class AppointmentModelTest {
    private Appointment appointment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        appointment = Appointment.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();
    }

    @Test
    @DisplayName("Should create Appointment with all fields")
    void testCreateAppointmentWithAllFields() {
        // Act
        Appointment testAppointment = new Appointment(2L, 201L, 101L, startTime, endTime, "PENDING", null);

        // Assert
        assertNotNull(testAppointment);
        assertEquals(2L, testAppointment.getId());
        assertEquals(201L, testAppointment.getPatientId());
        assertEquals(101L, testAppointment.getDoctorId());
        assertEquals(startTime, testAppointment.getStartTime());
        assertEquals(endTime, testAppointment.getEndTime());
        assertEquals("PENDING", testAppointment.getStatus());
    }

    @Test
    @DisplayName("Should set and get patientId")
    void testSetAndGetPatientId() {
        // Arrange
        Long newPatientId = 300L;

        // Act
        appointment.setPatientId(newPatientId);

        // Assert
        assertEquals(newPatientId, appointment.getPatientId());
    }

    @Test
    @DisplayName("Should set and get doctorId")
    void testSetAndGetDoctorId() {
        // Arrange
        Long newDoctorId = 150L;

        // Act
        appointment.setDoctorId(newDoctorId);

        // Assert
        assertEquals(newDoctorId, appointment.getDoctorId());
    }

    @Test
    @DisplayName("Should set and get startTime")
    void testSetAndGetStartTime() {
        // Arrange
        LocalDateTime newStartTime = LocalDateTime.of(2025, 6, 21, 14, 0);

        // Act
        appointment.setStartTime(newStartTime);

        // Assert
        assertEquals(newStartTime, appointment.getStartTime());
    }

    @Test
    @DisplayName("Should set and get status")
    void testSetAndGetStatus() {
        // Arrange
        String newStatus = "CANCELLED";

        // Act
        appointment.setStatus(newStatus);

        // Assert
        assertEquals(newStatus, appointment.getStatus());
    }

    @Test
    @DisplayName("Should test equality between two identical appointments")
    void testEqualityBetweenIdenticalAppointments() {
        // Arrange
        Appointment appointment1 = Appointment.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        Appointment appointment2 = Appointment.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        // Assert
        assertEquals(appointment1, appointment2);
    }

    @Test
    @DisplayName("Should test inequality between different appointments")
    void testInequalityBetweenDifferentAppointments() {
        // Arrange
        Appointment differentAppointment = Appointment.builder()
                .id(2L)
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CANCELLED")
                .recurrenceRule(null)
                .build();

        // Assert
        assertNotEquals(appointment, differentAppointment);
    }

    @Test
    @DisplayName("Should verify all fields are non-null when using Builder")
    void testAllFieldsNonNullWithBuilder() {
        // Arrange
        Appointment builtAppointment = Appointment.builder()
                .id(3L)
                .patientId(202L)
                .doctorId(102L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        // Assert
        assertAll(
                () -> assertNotNull(builtAppointment.getId()),
                () -> assertNotNull(builtAppointment.getPatientId()),
                () -> assertNotNull(builtAppointment.getDoctorId()),
                () -> assertNotNull(builtAppointment.getStartTime()),
                () -> assertNotNull(builtAppointment.getEndTime()),
                () -> assertNotNull(builtAppointment.getStatus())
        );
    }
}
