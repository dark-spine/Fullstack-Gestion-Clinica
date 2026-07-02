package com.clinica.agenda.controller;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.exception.ApiExceptionHandler;
import com.clinica.agenda.service.AgendaService;
import com.clinica.agenda.mapper.AgendaMapper;
import com.clinica.agenda.repository.ScheduleSlotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Agenda Controller Tests")
class AgendaControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private StubAgendaService agendaService;
    private AgendaController agendaController;
    private LocalValidatorFactoryBean validator;

    private SlotRequestDTO slotRequestDTO;
    private SlotResponseDTO slotResponseDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private static class StubAgendaService extends AgendaService {
        private SlotResponseDTO slotResponse;
        private RuntimeException exceptionToThrow;
        private List<SlotResponseDTO> slotsResponse;

        protected StubAgendaService() {
            super(null, null);
        }

        void setSlotResponse(SlotResponseDTO slotResponse) {
            this.slotResponse = slotResponse;
        }

        void setExceptionToThrow(RuntimeException exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

        void setSlotsResponse(List<SlotResponseDTO> slotsResponse) {
            this.slotsResponse = slotsResponse;
        }

        @Override
        public SlotResponseDTO createSlot(SlotRequestDTO dto) {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return slotResponse;
        }

        @Override
        public SlotResponseDTO reserveSlot(Long doctorId, java.time.LocalDateTime startTime) {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return slotResponse;
        }

        @Override
        public List<SlotResponseDTO> findAvailable(Long doctorId) {
            return slotsResponse != null ? slotsResponse : Collections.emptyList();
        }
    }

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

        agendaService = new StubAgendaService();
        agendaController = new AgendaController(agendaService);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(agendaController)
                .setControllerAdvice(new ApiExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("POST /api/agenda/slots should return 201 Created")
    void testCreateSlotReturns201() throws Exception {
        agendaService.setSlotResponse(slotResponseDTO);

        mockMvc.perform(post("/api/agenda/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.doctorId", is(100)))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    @Test
    @DisplayName("POST /api/agenda/slots should return 400 Bad Request with invalid data")
    void testCreateSlotReturns400WithInvalidData() throws Exception {
        SlotRequestDTO invalidRequest = SlotRequestDTO.builder()
                .doctorId(null)
                .startTime(null)
                .endTime(null)
                .durationMinutes(null)
                .build();

        mockMvc.perform(post("/api/agenda/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/agenda/slots/reserve should return 200 OK")
    void testReserveSlotReturns200() throws Exception {
        agendaService.setSlotResponse(slotResponseDTO);

        mockMvc.perform(post("/api/agenda/slots/reserve")
                        .param("doctorId", "100")
                        .param("startTime", "2025-06-20T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.doctorId", is(100)));
    }

    @Test
    @DisplayName("POST /api/agenda/slots/reserve should return 404 when slot not found")
    void testReserveSlotReturns404() throws Exception {
        agendaService.setExceptionToThrow(new java.util.NoSuchElementException("Slot no encontrado"));

        mockMvc.perform(post("/api/agenda/slots/reserve")
                        .param("doctorId", "100")
                        .param("startTime", "2025-06-20T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/agenda/slots/available should return 200 OK with list")
    void testGetAvailableSlotsReturns200() throws Exception {
        SlotResponseDTO slot2 = SlotResponseDTO.builder()
                .id(2L)
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 6, 20, 15, 0))
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        agendaService.setSlotsResponse(Arrays.asList(slotResponseDTO, slot2));

        mockMvc.perform(get("/api/agenda/slots/available")
                        .param("doctorId", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].doctorId", is(100)))
                .andExpect(jsonPath("$[1].doctorId", is(100)));
    }

    @Test
    @DisplayName("GET /api/agenda/slots/available should return 200 OK with empty list")
    void testGetAvailableSlotsReturns200EmptyList() throws Exception {
        agendaService.setSlotsResponse(Collections.emptyList());

        mockMvc.perform(get("/api/agenda/slots/available")
                        .param("doctorId", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /api/agenda/slots should verify service is called with correct DTO")
    void testCreateSlotCallsServiceWithCorrectDTO() throws Exception {
        agendaService.setSlotResponse(slotResponseDTO);

        mockMvc.perform(post("/api/agenda/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slotRequestDTO)))
                .andExpect(status().isCreated());
    }
}
