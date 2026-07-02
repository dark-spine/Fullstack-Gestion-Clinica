package com.clinica.citas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class CitaTest {
    
    @Test
    public void testCrearCita() {
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setPacienteId(100L);
        cita.setMedicoId(50L);
        cita.setSlotAgendaId(10L);
        cita.setMotivoConsulta("Consulta general");
        cita.setEstado("CONFIRMADA");
        cita.setCreatedAt(LocalDateTime.now());
        
        assertEquals(1L, cita.getId());
        assertEquals(100L, cita.getPacienteId());
        assertEquals(50L, cita.getMedicoId());
        assertEquals(10L, cita.getSlotAgendaId());
        assertEquals("Consulta general", cita.getMotivoConsulta());
        assertEquals("CONFIRMADA", cita.getEstado());
        assertNotNull(cita.getCreatedAt());
    }
    
    @Test
    public void testCitaDefaultValues() {
        Cita cita = new Cita();
        assertNull(cita.getId());
        assertNull(cita.getPacienteId());
        assertNull(cita.getMedicoId());
    }
    
    @Test
    public void testCitaEstadoValido() {
        Cita cita = new Cita();
        cita.setEstado("PENDIENTE");
        assertEquals("PENDIENTE", cita.getEstado());
        
        cita.setEstado("CONFIRMADA");
        assertEquals("CONFIRMADA", cita.getEstado());
        
        cita.setEstado("CANCELADA");
        assertEquals("CANCELADA", cita.getEstado());
    }
    
    @Test
    public void testCitaMotivoConsultaNoVacio() {
        Cita cita = new Cita();
        cita.setMotivoConsulta("Dolor de cabeza");
        assertNotNull(cita.getMotivoConsulta());
        assertFalse(cita.getMotivoConsulta().isEmpty());
    }
}
