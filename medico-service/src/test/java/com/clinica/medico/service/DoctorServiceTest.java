package com.clinica.medico.service;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.mapper.DoctorMapper;
import com.clinica.medico.model.Doctor;
import com.clinica.medico.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa Servicio (DoctorService).
 * Verifica la lógica de negocio utilizando mocks para las dependencias.
 */
@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository repository;

    @Mock
    private DoctorMapper mapper;

    private DoctorService service;

    @BeforeEach
    void setUp() {
        service = new DoctorService(repository, mapper);
    }

    @Test
    void testCreateDoctorSuccessfully() {
        // Arrange
        DoctorRequestDTO requestDTO = DoctorRequestDTO.builder()
                .name("Dr. Ana López")
                .specialty("Oftalmología")
                .office("Consultorio 404")
                .baseSchedule("L-S: 09:00-15:00")
                .build();

        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Dr. Ana López")
                .specialty("Oftalmología")
                .office("Consultorio 404")
                .baseSchedule("L-S: 09:00-15:00")
                .build();

        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .name("Dr. Ana López")
                .specialty("Oftalmología")
                .office("Consultorio 404")
                .baseSchedule("L-S: 09:00-15:00")
                .build();

        when(mapper.toEntity(requestDTO)).thenReturn(doctor);
        when(repository.save(doctor)).thenReturn(doctor);
        when(mapper.toResponse(doctor)).thenReturn(responseDTO);

        // Act
        DoctorResponseDTO result = service.createDoctor(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Dr. Ana López", result.getName());
        assertEquals("Oftalmología", result.getSpecialty());
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(doctor);
        verify(mapper).toResponse(doctor);
    }

    @Test
    void testListDoctorsSuccessfully() {
        // Arrange
        Doctor doctor1 = Doctor.builder().id(1L).name("Dr. 1").specialty("Esp 1").office("Of 1").baseSchedule("L-V").build();
        Doctor doctor2 = Doctor.builder().id(2L).name("Dr. 2").specialty("Esp 2").office("Of 2").baseSchedule("L-V").build();
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        DoctorResponseDTO response1 = DoctorResponseDTO.builder().id(1L).name("Dr. 1").specialty("Esp 1").office("Of 1").baseSchedule("L-V").build();
        DoctorResponseDTO response2 = DoctorResponseDTO.builder().id(2L).name("Dr. 2").specialty("Esp 2").office("Of 2").baseSchedule("L-V").build();

        when(repository.findAll()).thenReturn(doctors);
        when(mapper.toResponse(doctor1)).thenReturn(response1);
        when(mapper.toResponse(doctor2)).thenReturn(response2);

        // Act
        List<DoctorResponseDTO> result = service.listDoctors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dr. 1", result.get(0).getName());
        assertEquals("Dr. 2", result.get(1).getName());
        verify(repository).findAll();
    }

    @Test
    void testCountDoctorsSuccessfully() {
        // Arrange
        when(repository.count()).thenReturn(5L);

        // Act
        Long result = service.countDoctors();

        // Assert
        assertEquals(5L, result);
        verify(repository).count();
    }

    @Test
    void testCountDoctorsReturnsZero() {
        // Arrange
        when(repository.count()).thenReturn(0L);

        // Act
        Long result = service.countDoctors();

        // Assert
        assertEquals(0L, result);
        verify(repository).count();
    }
}
