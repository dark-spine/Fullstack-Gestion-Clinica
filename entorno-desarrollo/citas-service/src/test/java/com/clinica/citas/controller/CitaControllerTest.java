package com.clinica.citas.controller;

import com.clinica.citas.dto.CitaCreateDTO;
import com.clinica.citas.dto.CitaDTO;
import com.clinica.citas.service.CitaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
public class CitaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CitaService citaService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private CitaDTO citaDTO;
    private CitaCreateDTO citaCreateDTO;
    
    @BeforeEach
    public void setUp() {
        citaDTO = new CitaDTO();
        citaDTO.setId(1L);
        citaDTO.setPacienteId(1L);
        citaDTO.setMedicoId(1L);
        citaDTO.setSlotAgendaId(1L);
        citaDTO.setMotivoConsulta("Consulta");
        citaDTO.setEstado("CONFIRMADA");
        
        citaCreateDTO = new CitaCreateDTO();
        citaCreateDTO.setPacienteId(1L);
        citaCreateDTO.setMedicoId(1L);
        citaCreateDTO.setSlotAgendaId(1L);
        citaCreateDTO.setMotivoConsulta("Consulta");
    }
    
    @Test
    public void testCrearCita() throws Exception {
        when(citaService.crearCita(any(CitaCreateDTO.class))).thenReturn(citaDTO);
        
        mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(citaCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
        
        verify(citaService, times(1)).crearCita(any(CitaCreateDTO.class));
    }
    
    @Test
    public void testListarTodos() throws Exception {
        List<CitaDTO> citas = Arrays.asList(citaDTO);
        when(citaService.listarTodos()).thenReturn(citas);
        
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(citaService, times(1)).listarTodos();
    }
    
    @Test
    public void testObtenerPorId() throws Exception {
        when(citaService.obtenerPorId(1L)).thenReturn(citaDTO);
        
        mockMvc.perform(get("/api/citas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        
        verify(citaService, times(1)).obtenerPorId(1L);
    }
    
    @Test
    public void testListarPorPaciente() throws Exception {
        List<CitaDTO> citas = Arrays.asList(citaDTO);
        when(citaService.listarPorPaciente(1L)).thenReturn(citas);
        
        mockMvc.perform(get("/api/citas/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(citaService, times(1)).listarPorPaciente(1L);
    }
    
    @Test
    public void testListarPorMedico() throws Exception {
        List<CitaDTO> citas = Arrays.asList(citaDTO);
        when(citaService.listarPorMedico(1L)).thenReturn(citas);
        
        mockMvc.perform(get("/api/citas/medico/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(citaService, times(1)).listarPorMedico(1L);
    }
    
    @Test
    public void testActualizarCita() throws Exception {
        when(citaService.actualizar(eq(1L), any(CitaCreateDTO.class))).thenReturn(citaDTO);
        
        mockMvc.perform(put("/api/citas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(citaCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        
        verify(citaService, times(1)).actualizar(eq(1L), any(CitaCreateDTO.class));
    }
    
    @Test
    public void testEliminarCita() throws Exception {
        doNothing().when(citaService).eliminar(1L);
        
        mockMvc.perform(delete("/api/citas/1"))
                .andExpect(status().isNoContent());
        
        verify(citaService, times(1)).eliminar(1L);
    }
}
