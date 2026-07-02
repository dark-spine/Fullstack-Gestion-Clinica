package com.clinica.cancelaciones.service;

import com.clinica.cancelaciones.client.CitasClient;
import com.clinica.cancelaciones.dto.CancelacionCreateDTO;
import com.clinica.cancelaciones.dto.CancelacionDTO;
import com.clinica.cancelaciones.model.Cancelacion;
import com.clinica.cancelaciones.repository.CancelacionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelacionServiceTest {
    
    @Mock
    private CancelacionRepository cancelacionRepository;
    
    @Mock
    private CitasClient citasClient;
    
    @InjectMocks
    private CancelacionService cancelacionService;
    
    private Cancelacion cancelacion;
    private CancelacionCreateDTO cancelacionCreateDTO;
    
    @BeforeEach
    public void setUp() {
        cancelacion = new Cancelacion();
        cancelacion.setId(1L);
        cancelacion.setCitaId(1L);
        cancelacion.setMotivo("Emergencia personal");
        cancelacion.setEstado("CANCELADA");
        cancelacion.setFechaCancelacion(LocalDateTime.now());
        
        cancelacionCreateDTO = new CancelacionCreateDTO();
        cancelacionCreateDTO.setCitaId(1L);
        cancelacionCreateDTO.setMotivo("Emergencia personal");
    }
    
    @Test
    public void testCrearCancelacion() {
        when(cancelacionRepository.save(any(Cancelacion.class))).thenReturn(cancelacion);
        
        CancelacionDTO resultado = cancelacionService.crearCancelacion(cancelacionCreateDTO);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(cancelacionRepository, times(1)).save(any(Cancelacion.class));
    }
    
    @Test
    public void testObtenerCancelacionPorId() {
        when(cancelacionRepository.findById(1L)).thenReturn(Optional.of(cancelacion));
        
        CancelacionDTO resultado = cancelacionService.obtenerPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(cancelacionRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testListarTodas() {
        List<Cancelacion> cancelaciones = Arrays.asList(cancelacion);
        when(cancelacionRepository.findAll()).thenReturn(cancelaciones);
        
        List<CancelacionDTO> resultado = cancelacionService.listarTodas();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cancelacionRepository, times(1)).findAll();
    }
    
    @Test
    public void testActualizarCancelacion() {
        when(cancelacionRepository.findById(1L)).thenReturn(Optional.of(cancelacion));
        when(cancelacionRepository.save(any(Cancelacion.class))).thenReturn(cancelacion);
        
        CancelacionDTO resultado = cancelacionService.actualizar(1L, cancelacionCreateDTO);
        
        assertNotNull(resultado);
        verify(cancelacionRepository, times(1)).findById(1L);
        verify(cancelacionRepository, times(1)).save(any(Cancelacion.class));
    }
    
    @Test
    public void testEliminarCancelacion() {
        doNothing().when(cancelacionRepository).deleteById(1L);
        
        cancelacionService.eliminar(1L);
        
        verify(cancelacionRepository, times(1)).deleteById(1L);
    }
}
