package com.clinica.citas.service;

import com.clinica.citas.client.AgendaClient;
import com.clinica.citas.client.MedicoClient;
import com.clinica.citas.client.PacienteClient;
import com.clinica.citas.dto.CitaCreateDTO;
import com.clinica.citas.dto.CitaDTO;
import com.clinica.citas.model.Cita;
import com.clinica.citas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {
    
    @Mock
    private CitaRepository citaRepository;
    
    @Mock
    private PacienteClient pacienteClient;
    
    @Mock
    private MedicoClient medicoClient;
    
    @Mock
    private AgendaClient agendaClient;
    
    @InjectMocks
    private CitaService citaService;
    
    private Cita cita;
    private CitaCreateDTO citaCreateDTO;
    
    @BeforeEach
    public void setUp() {
        cita = new Cita();
        cita.setId(1L);
        cita.setPacienteId(1L);
        cita.setMedicoId(1L);
        cita.setSlotAgendaId(1L);
        cita.setMotivoConsulta("Consulta general");
        cita.setEstado("CONFIRMADA");
        cita.setCreatedAt(LocalDateTime.now());
        
        citaCreateDTO = new CitaCreateDTO();
        citaCreateDTO.setPacienteId(1L);
        citaCreateDTO.setMedicoId(1L);
        citaCreateDTO.setSlotAgendaId(1L);
        citaCreateDTO.setMotivoConsulta("Consulta general");
    }
    
    @Test
    public void testCrearCita() {
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);
        
        CitaDTO resultado = citaService.crearCita(citaCreateDTO);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }
    
    @Test
    public void testObtenerCitaPorId() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        
        CitaDTO resultado = citaService.obtenerPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(citaRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testListarTodos() {
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findAll()).thenReturn(citas);
        
        List<CitaDTO> resultado = citaService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findAll();
    }
    
    @Test
    public void testListarPorPaciente() {
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByPacienteId(1L)).thenReturn(citas);
        
        List<CitaDTO> resultado = citaService.listarPorPaciente(1L);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByPacienteId(1L);
    }
    
    @Test
    public void testListarPorMedico() {
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByMedicoId(1L)).thenReturn(citas);
        
        List<CitaDTO> resultado = citaService.listarPorMedico(1L);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByMedicoId(1L);
    }
    
    @Test
    public void testActualizarCita() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);
        
        CitaDTO resultado = citaService.actualizar(1L, citaCreateDTO);
        
        assertNotNull(resultado);
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }
    
    @Test
    public void testEliminarCita() {
        when(citaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(citaRepository).deleteById(1L);
        
        citaService.eliminar(1L);
        
        verify(citaRepository, times(1)).deleteById(1L);
    }
}
