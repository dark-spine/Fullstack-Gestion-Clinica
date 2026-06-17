package com.clinica.medico.repository;

import com.clinica.medico.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la capa Repositorio (DoctorRepository).
 * Utiliza @DataJpaTest con H2 en memoria para testing de acceso a datos.
 */
@DataJpaTest
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository repository;

    @Test
    void testSaveDoctorSuccessfully() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .name("Dr. Roberto Sánchez")
                .specialty("Neurología")
                .office("Consultorio 505")
                .baseSchedule("M-J: 10:00-18:00")
                .build();

        // Act
        Doctor saved = repository.save(doctor);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Dr. Roberto Sánchez", saved.getName());
        assertEquals("Neurología", saved.getSpecialty());
    }

    @Test
    void testFindByIdSuccessfully() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .name("Dr. Patricia Gómez")
                .specialty("Otorrinolaringología")
                .office("Consultorio 606")
                .baseSchedule("L-V: 08:00-16:00")
                .build();
        Doctor saved = repository.save(doctor);

        // Act
        Optional<Doctor> found = repository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Dr. Patricia Gómez", found.get().getName());
        assertEquals("Otorrinolaringología", found.get().getSpecialty());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Doctor> found = repository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllDoctors() {
        // Arrange
        Doctor doctor1 = Doctor.builder().name("Dr. A").specialty("Esp A").office("Of A").baseSchedule("L-V").build();
        Doctor doctor2 = Doctor.builder().name("Dr. B").specialty("Esp B").office("Of B").baseSchedule("L-V").build();
        repository.save(doctor1);
        repository.save(doctor2);

        // Act
        List<Doctor> doctors = repository.findAll();

        // Assert
        assertEquals(2, doctors.size());
    }

    @Test
    void testFindBySpecialty() {
        // Arrange
        Doctor cardiologist = Doctor.builder()
                .name("Dr. Cardiologo")
                .specialty("Cardiología")
                .office("Of 1")
                .baseSchedule("L-V")
                .build();
        Doctor neurologist = Doctor.builder()
                .name("Dr. Neurologo")
                .specialty("Neurología")
                .office("Of 2")
                .baseSchedule("L-V")
                .build();
        repository.save(cardiologist);
        repository.save(neurologist);

        // Act
        List<Doctor> cardiologists = repository.findBySpecialty("Cardiología");

        // Assert
        assertEquals(1, cardiologists.size());
        assertEquals("Cardiología", cardiologists.get(0).getSpecialty());
    }

    @Test
    void testUpdateDoctor() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .name("Dr. Original")
                .specialty("Original Spec")
                .office("Of Original")
                .baseSchedule("L-V")
                .build();
        Doctor saved = repository.save(doctor);

        // Act
        saved.setName("Dr. Actualizado");
        Doctor updated = repository.save(saved);

        // Assert
        assertEquals("Dr. Actualizado", updated.getName());
    }

    @Test
    void testDeleteDoctor() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .name("Dr. A Eliminar")
                .specialty("Esp")
                .office("Of")
                .baseSchedule("L-V")
                .build();
        Doctor saved = repository.save(doctor);

        // Act
        repository.delete(saved);
        Optional<Doctor> found = repository.findById(saved.getId());

        // Assert
        assertFalse(found.isPresent());
    }
}
