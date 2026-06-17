package com.clinica.agenda.controller;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.service.AgendaService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
@DisplayName("Agenda Controller Tests")
class AgendaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;

    private SlotRequestDTO slotRequestDTO;
    private SlotResponseDTO slotResponseDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        slotRequestDTO = SlotRequestDTO.builder()
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .durationMinutes(60)
                .build();

        slotResponseDTO = SlotResponseDTO.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();
    }

    @Test
    @DisplayName("POST /api/agenda/slots should return 201 Created")
    void testCreateSlotReturns201() throws Exception {
        // Arrange
        when(agendaService.createSlot(any(SlotRequestDTO.class))).thenReturn(slotResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/agenda/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.doctorId", is(100)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));

        verify(agendaService, times(1)).createSlot(any(SlotRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/agenda/slots should return 400 Bad Request with invalid data")
    void testCreateSlotReturns400WithInvalidData() throws Exception {
        // Arrange
        SlotRequestDTO invalidRequest = SlotRequestDTO.builder()
                .doctorId(null)
                .startTime(null)
                .endTime(null)
                .durationMinutes(null)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/agenda/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/agenda/slots/reserve should return 200 OK")
    void testReserveSlotReturns200() throws Exception {
        // Arrange
        when(agendaService.reserveSlot(100L, startTime)).thenReturn(slotResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/agenda/slots/reserve")
                .param("doctorId", "100")
                .param("startTime", "2025-06-20T10:00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.doctorId", is(100)));

        verify(agendaService, times(1)).reserveSlot(100L, startTime);
    }

    @Test
    @DisplayName("POST /api/agenda/slots/reserve should return 404 when slot not found")
    void testReserveSlotReturns404() throws Exception {
        // Arrange
        when(agendaService.reserveSlot(any(), any())).thenThrow(new IllegalStateException("Slot no encontrado"));

        // Act & Assert
        mockMvc.perform(post("/api/agenda/slots/reserve")
                .param("doctorId", "100")
                .param("startTime", "2025-06-20T10:00:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/agenda/slots/available should return 200 OK with list")
    void testGetAvailableSlotsReturns200() throws Exception {
        // Arrange
        SlotResponseDTO slot2 = SlotResponseDTO.builder()
                .id(2L)
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 6, 20, 15, 0))
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        List<SlotResponseDTO> slots = Arrays.asList(slotResponseDTO, slot2);
        when(agendaService.findAvailable(100L)).thenReturn(slots);

        // Act & Assert
        mockMvc.perform(get("/api/agenda/slots/available")
                .param("doctorId", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].doctorId", is(100)))
                .andExpect(jsonPath("$[1].doctorId", is(100)));

        verify(agendaService, times(1)).findAvailable(100L);
    }

    @Test
    @DisplayName("GET /api/agenda/slots/available should return 200 OK with empty list")
    void testGetAvailableSlotsReturns200EmptyList() throws Exception {
        // Arrange
        when(agendaService.findAvailable(100L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/agenda/slots/available")
                .param("doctorId", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(agendaService, times(1)).findAvailable(100L);
    }

    @Test
    @DisplayName("POST /api/agenda/slots should verify service is called with correct DTO")
    void testCreateSlotCallsServiceWithCorrectDTO() throws Exception {
        // Arrange
        when(agendaService.createSlot(any(SlotRequestDTO.class))).thenReturn(slotResponseDTO);

        // Act
        mockMvc.perform(post("/api/agenda/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isCreated());

        // Assert
        verify(agendaService).createSlot(any(SlotRequestDTO.class));
    }
}
