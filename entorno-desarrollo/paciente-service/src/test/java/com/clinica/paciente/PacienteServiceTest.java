package com.clinica.paciente.service;

import com.clinica.paciente.dto.PacienteCreateDTO;
import com.clinica.paciente.dto.PacienteDTO;
import com.clinica.paciente.model.Paciente;
import com.clinica.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {
    
    @Mock
    private PacienteRepository pacienteRepository;
    
    @InjectMocks
    private PacienteService pacienteService;
    
    private Paciente paciente;
    private PacienteCreateDTO pacienteCreateDTO;
    
    @BeforeEach
    public void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setEmail("juan@example.com");
        paciente.setTelefono("123456789");
        paciente.setRut("12.345.678-9");
        
        pacienteCreateDTO = new PacienteCreateDTO();
        pacienteCreateDTO.setNombre("Juan");
        pacienteCreateDTO.setApellido("Pérez");
        pacienteCreateDTO.setEmail("juan@example.com");
        pacienteCreateDTO.setTelefono("123456789");
        pacienteCreateDTO.setRut("12.345.678-9");
    }
    
    @Test
    public void testCrearPaciente() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        
        PacienteDTO resultado = pacienteService.crearPaciente(pacienteCreateDTO);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
    
    @Test
    public void testObtenerPacientePorId() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        
        PacienteDTO resultado = pacienteService.obtenerPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pacienteRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testListarTodos() {
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.findAll()).thenReturn(pacientes);
        
        List<PacienteDTO> resultado = pacienteService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pacienteRepository, times(1)).findAll();
    }
    
    @Test
    public void testActualizarPaciente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        
        PacienteDTO resultado = pacienteService.actualizar(1L, pacienteCreateDTO);
        
        assertNotNull(resultado);
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
    
    @Test
    public void testEliminarPaciente() {
        doNothing().when(pacienteRepository).deleteById(1L);
        
        pacienteService.eliminar(1L);
        
        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}
