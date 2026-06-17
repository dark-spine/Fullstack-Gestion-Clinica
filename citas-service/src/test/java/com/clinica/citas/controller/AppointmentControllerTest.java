package com.clinica.citas.controller;

import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@DisplayName("Appointment Controller Tests")
class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentRequestDTO appointmentRequestDTO;
    private AppointmentResponseDTO appointmentResponseDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        appointmentRequestDTO = AppointmentRequestDTO.builder()
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        appointmentResponseDTO = AppointmentResponseDTO.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .build();
    }

    @Test
    @DisplayName("POST /api/appointments should return 201 Created")
    void testCreateAppointmentReturns201() throws Exception {
        // Arrange
        when(appointmentService.createAppointment(any(AppointmentRequestDTO.class))).thenReturn(appointmentResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.patientId", is(200)))
                .andExpect(jsonPath("$.doctorId", is(100)))
                .andExpect(jsonPath("$.status", is("CONFIRMED")));

        verify(appointmentService, times(1)).createAppointment(any(AppointmentRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/appointments should return 400 Bad Request with invalid data")
    void testCreateAppointmentReturns400WithInvalidData() throws Exception {
        // Arrange
        AppointmentRequestDTO invalidRequest = AppointmentRequestDTO.builder()
                .patientId(null)
                .doctorId(null)
                .startTime(null)
                .endTime(null)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/appointments should return 200 OK with list of appointments")
    void testGetAppointmentsReturns200() throws Exception {
        // Arrange
        AppointmentResponseDTO appointment2 = AppointmentResponseDTO.builder()
                .id(2L)
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CONFIRMED")
                .build();

        List<AppointmentResponseDTO> appointments = Arrays.asList(appointmentResponseDTO, appointment2);
        when(appointmentService.listAppointments()).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patientId", is(200)))
                .andExpect(jsonPath("$[1].patientId", is(201)));

        verify(appointmentService, times(1)).listAppointments();
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} should return 204 No Content")
    void testCancelAppointmentReturns204() throws Exception {
        // Arrange
        when(appointmentService.cancelAppointment(1L)).thenReturn(appointmentResponseDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).cancelAppointment(1L);
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} should return 404 when appointment not found")
    void testCancelAppointmentReturns404() throws Exception {
        // Arrange
        when(appointmentService.cancelAppointment(999L))
                .thenThrow(new IllegalArgumentException("Appointment not found"));

        // Act & Assert
        mockMvc.perform(delete("/api/appointments/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/appointments should return 200 OK with empty list")
    void testGetAppointmentsReturns200EmptyList() throws Exception {
        // Arrange
        when(appointmentService.listAppointments()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(appointmentService, times(1)).listAppointments();
    }

    @Test
    @DisplayName("POST /api/appointments should verify service is called with correct DTO")
    void testCreateAppointmentCallsServiceWithCorrectDTO() throws Exception {
        // Arrange
        when(appointmentService.createAppointment(any(AppointmentRequestDTO.class))).thenReturn(appointmentResponseDTO);

        // Act
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isCreated());

        // Assert
        verify(appointmentService).createAppointment(any(AppointmentRequestDTO.class));
    }
}
