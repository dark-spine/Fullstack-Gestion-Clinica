package com.clinica.citas.repository;

import com.clinica.citas.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Appointment Repository Tests")
class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository repository;

    private Appointment appointment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        appointment = Appointment.builder()
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();
    }

    @Test
    @DisplayName("Should save appointment successfully")
    void testSaveAppointmentSuccess() {
        // Act
        Appointment savedAppointment = repository.save(appointment);

        // Assert
        assertNotNull(savedAppointment.getId());
        assertEquals(200L, savedAppointment.getPatientId());
        assertEquals("CONFIRMED", savedAppointment.getStatus());
    }

    @Test
    @DisplayName("Should find appointment by id")
    void testFindById() {
        // Arrange
        Appointment savedAppointment = repository.save(appointment);

        // Act
        Optional<Appointment> foundAppointment = repository.findById(savedAppointment.getId());

        // Assert
        assertTrue(foundAppointment.isPresent());
        assertEquals(savedAppointment.getId(), foundAppointment.get().getId());
        assertEquals(200L, foundAppointment.get().getPatientId());
    }

    @Test
    @DisplayName("Should return empty Optional when appointment not found")
    void testFindByIdNotFound() {
        // Act
        Optional<Appointment> foundAppointment = repository.findById(999L);

        // Assert
        assertTrue(foundAppointment.isEmpty());
    }

    @Test
    @DisplayName("Should find all appointments")
    void testFindAll() {
        // Arrange
        Appointment appointment2 = Appointment.builder()
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        repository.save(appointment);
        repository.save(appointment2);

        // Act
        List<Appointment> allAppointments = repository.findAll();

        // Assert
        assertEquals(2, allAppointments.size());
    }

    @Test
    @DisplayName("Should update appointment status successfully")
    void testUpdateAppointmentStatus() {
        // Arrange
        Appointment savedAppointment = repository.save(appointment);
        savedAppointment.setStatus("CANCELLED");

        // Act
        Appointment updatedAppointment = repository.save(savedAppointment);

        // Assert
        assertEquals("CANCELLED", updatedAppointment.getStatus());
    }

    @Test
    @DisplayName("Should delete appointment successfully")
    void testDeleteAppointmentSuccess() {
        // Arrange
        Appointment savedAppointment = repository.save(appointment);
        Long appointmentId = savedAppointment.getId();

        // Act
        repository.deleteById(appointmentId);

        // Assert
        Optional<Appointment> deletedAppointment = repository.findById(appointmentId);
        assertTrue(deletedAppointment.isEmpty());
    }

    @Test
    @DisplayName("Should count all appointments")
    void testCountAllAppointments() {
        // Arrange
        Appointment appointment2 = Appointment.builder()
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        repository.save(appointment);
        repository.save(appointment2);

        // Act
        long count = repository.count();

        // Assert
        assertEquals(2, count);
    }
}
