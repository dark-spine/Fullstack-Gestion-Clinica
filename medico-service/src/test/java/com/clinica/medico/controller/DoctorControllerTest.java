package com.clinica.medico.controller;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para la capa Controlador (DoctorController).
 * Utiliza MockMvc para testing de endpoints REST sin servidor.
 * Valida status HTTP 201 (Created), 200 (OK) y 404 (Not Found).
 */
@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorRequestDTO requestDTO;
    private DoctorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = DoctorRequestDTO.builder()
                .name("Dr. Test Doctor")
                .specialty("Especialidad Test")
                .office("Consultorio Test")
                .baseSchedule("L-V: 09:00-17:00")
                .build();

        responseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .name("Dr. Test Doctor")
                .specialty("Especialidad Test")
                .office("Consultorio Test")
                .baseSchedule("L-V: 09:00-17:00")
                .build();
    }

    @Test
    void testCreateDoctorReturnsStatus201() throws Exception {
        // Arrange
        when(service.createDoctor(any(DoctorRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Test Doctor"))
                .andExpect(jsonPath("$.specialty").value("Especialidad Test"));
    }

    @Test
    void testGetAllDoctorsReturnsStatus200() throws Exception {
        // Arrange
        DoctorResponseDTO doctor1 = DoctorResponseDTO.builder()
                .id(1L).name("Dr. 1").specialty("Esp 1").office("Of 1").baseSchedule("L-V").build();
        DoctorResponseDTO doctor2 = DoctorResponseDTO.builder()
                .id(2L).name("Dr. 2").specialty("Esp 2").office("Of 2").baseSchedule("L-V").build();

        when(service.listDoctors()).thenReturn(Arrays.asList(doctor1, doctor2));

        // Act & Assert
        mockMvc.perform(get("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Dr. 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Dr. 2"));
    }

    @Test
    void testCountDoctorsReturnsStatus200() throws Exception {
        // Arrange
        when(service.countDoctors()).thenReturn(5L);

        // Act & Assert
        mockMvc.perform(get("/api/doctors/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testCreateDoctorWithInvalidDataReturnsStatus400() throws Exception {
        // Arrange
        DoctorRequestDTO invalidDTO = DoctorRequestDTO.builder()
                .name("")  // Nombre vacío
                .specialty("Esp")
                .office("Of")
                .baseSchedule("L-V")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}
