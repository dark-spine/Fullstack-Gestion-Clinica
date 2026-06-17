package com.clinica.medico.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la capa Modelo (Doctor).
 * Verifica la creación de instancias y validación de datos.
 */
class DoctorTest {

    @Test
    void testDoctorCreationWithAllFields() {
        // Arrange & Act
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Dr. Juan Pérez")
                .specialty("Cardiología")
                .office("Consultorio 101")
                .baseSchedule("L-V: 09:00-17:00")
                .build();

        // Assert
        assertNotNull(doctor);
        assertEquals(1L, doctor.getId());
        assertEquals("Dr. Juan Pérez", doctor.getName());
        assertEquals("Cardiología", doctor.getSpecialty());
        assertEquals("Consultorio 101", doctor.getOffice());
        assertEquals("L-V: 09:00-17:00", doctor.getBaseSchedule());
    }

    @Test
    void testDoctorCreationWithNoArgsConstructor() {
        // Arrange & Act
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setName("Dr. María García");
        doctor.setSpecialty("Pediatría");
        doctor.setOffice("Consultorio 202");
        doctor.setBaseSchedule("L-J: 10:00-18:00");

        // Assert
        assertEquals(2L, doctor.getId());
        assertEquals("Dr. María García", doctor.getName());
        assertEquals("Pediatría", doctor.getSpecialty());
        assertEquals("Consultorio 202", doctor.getOffice());
        assertEquals("L-J: 10:00-18:00", doctor.getBaseSchedule());
    }

    @Test
    void testDoctorEquality() {
        // Arrange
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .name("Dr. Carlos López")
                .specialty("Dermatología")
                .office("Consultorio 303")
                .baseSchedule("M-V: 08:00-16:00")
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(1L)
                .name("Dr. Carlos López")
                .specialty("Dermatología")
                .office("Consultorio 303")
                .baseSchedule("M-V: 08:00-16:00")
                .build();

        // Act & Assert
        assertEquals(doctor1, doctor2);
    }
}
