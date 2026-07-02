package com.clinica.medico.service;

import com.clinica.medico.dto.MedicoCreateDTO;
import com.clinica.medico.dto.MedicoDTO;
import com.clinica.medico.model.Medico;
import com.clinica.medico.repository.MedicoRepository;
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
public class MedicoServiceTest {
    
    @Mock
    private MedicoRepository medicoRepository;
    
    @InjectMocks
    private MedicoService medicoService;
    
    private Medico medico;
    private MedicoCreateDTO medicoCreateDTO;
    
    @BeforeEach
    public void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Dr. Carlos");
        medico.setApellido("García");
        medico.setEspecialidad("Cardiología");
        medico.setEmail("carlos@example.com");
        medico.setTelefono("987654321");
        medico.setMatricula("MED12345");
        
        medicoCreateDTO = new MedicoCreateDTO();
        medicoCreateDTO.setNombre("Dr. Carlos");
        medicoCreateDTO.setApellido("García");
        medicoCreateDTO.setEspecialidad("Cardiología");
        medicoCreateDTO.setEmail("carlos@example.com");
        medicoCreateDTO.setTelefono("987654321");
        medicoCreateDTO.setMatricula("MED12345");
    }
    
    @Test
    public void testCrearMedico() {
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);
        
        MedicoDTO resultado = medicoService.crearMedico(medicoCreateDTO);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cardiología", resultado.getEspecialidad());
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }
    
    @Test
    public void testObtenerMedicoPorId() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        
        MedicoDTO resultado = medicoService.obtenerPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(medicoRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testListarTodos() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findAll()).thenReturn(medicos);
        
        List<MedicoDTO> resultado = medicoService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(medicoRepository, times(1)).findAll();
    }
    
    @Test
    public void testListarPorEspecialidad() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findByEspecialidad("Cardiología")).thenReturn(medicos);
        
        List<MedicoDTO> resultado = medicoService.listarPorEspecialidad("Cardiología");
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(medicoRepository, times(1)).findByEspecialidad("Cardiología");
    }
    
    @Test
    public void testActualizarMedico() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);
        
        MedicoDTO resultado = medicoService.actualizar(1L, medicoCreateDTO);
        
        assertNotNull(resultado);
        verify(medicoRepository, times(1)).findById(1L);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }
    
    @Test
    public void testEliminarMedico() {
        doNothing().when(medicoRepository).deleteById(1L);
        
        medicoService.eliminar(1L);
        
        verify(medicoRepository, times(1)).deleteById(1L);
    }
}
