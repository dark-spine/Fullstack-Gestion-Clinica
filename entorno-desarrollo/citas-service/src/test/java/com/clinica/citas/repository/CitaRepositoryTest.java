package com.clinica.citas.repository;

import com.clinica.citas.model.Cita;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CitaRepositoryTest {
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Test
    public void testGuardarCita() {
        Cita cita = new Cita();
        cita.setPacienteId(1L);
        cita.setMedicoId(1L);
        cita.setSlotAgendaId(1L);
        cita.setMotivoConsulta("Consulta");
        cita.setEstado("CONFIRMADA");
        cita.setCreatedAt(LocalDateTime.now());
        
        Cita saved = citaRepository.save(cita);
        assertNotNull(saved.getId());
        assertEquals("CONFIRMADA", saved.getEstado());
    }
    
    @Test
    public void testObtenerCitaPorId() {
        Cita cita = new Cita();
        cita.setPacienteId(2L);
        cita.setMedicoId(2L);
        cita.setSlotAgendaId(2L);
        cita.setMotivoConsulta("Chequeo");
        cita.setEstado("PENDIENTE");
        cita.setCreatedAt(LocalDateTime.now());
        
        Cita saved = citaRepository.save(cita);
        Cita found = citaRepository.findById(saved.getId()).orElse(null);
        
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("PENDIENTE", found.getEstado());
    }
    
    @Test
    public void testObtenerCitasPorPaciente() {
        Cita cita1 = new Cita();
        cita1.setPacienteId(3L);
        cita1.setMedicoId(1L);
        cita1.setMotivoConsulta("Consulta 1");
        cita1.setEstado("CONFIRMADA");
        cita1.setCreatedAt(LocalDateTime.now());
        
        Cita cita2 = new Cita();
        cita2.setPacienteId(3L);
        cita2.setMedicoId(2L);
        cita2.setMotivoConsulta("Consulta 2");
        cita2.setEstado("PENDIENTE");
        cita2.setCreatedAt(LocalDateTime.now());
        
        citaRepository.save(cita1);
        citaRepository.save(cita2);
        
        List<Cita> citas = citaRepository.findByPacienteId(3L);
        assertEquals(2, citas.size());
    }
    
    @Test
    public void testObtenerCitasPorMedico() {
        Cita cita = new Cita();
        cita.setPacienteId(4L);
        cita.setMedicoId(5L);
        cita.setMotivoConsulta("Consulta");
        cita.setEstado("CONFIRMADA");
        cita.setCreatedAt(LocalDateTime.now());
        
        citaRepository.save(cita);
        List<Cita> citas = citaRepository.findByMedicoId(5L);
        
        assertTrue(citas.size() > 0);
    }
    
    @Test
    public void testActualizarCita() {
        Cita cita = new Cita();
        cita.setPacienteId(5L);
        cita.setMedicoId(1L);
        cita.setMotivoConsulta("Consulta");
        cita.setEstado("PENDIENTE");
        cita.setCreatedAt(LocalDateTime.now());
        
        Cita saved = citaRepository.save(cita);
        saved.setEstado("CONFIRMADA");
        Cita updated = citaRepository.save(saved);
        
        assertEquals("CONFIRMADA", updated.getEstado());
    }
    
    @Test
    public void testEliminarCita() {
        Cita cita = new Cita();
        cita.setPacienteId(6L);
        cita.setMedicoId(1L);
        cita.setMotivoConsulta("Consulta");
        cita.setEstado("PENDIENTE");
        cita.setCreatedAt(LocalDateTime.now());
        
        Cita saved = citaRepository.save(cita);
        Long id = saved.getId();
        
        citaRepository.deleteById(id);
        assertFalse(citaRepository.existsById(id));
    }
}
