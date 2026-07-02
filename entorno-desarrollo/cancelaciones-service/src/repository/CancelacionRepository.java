package com.clinica.citas.repository;
import com.clinica.citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CancelacionRepository extends JpaRepository<Cancelacion, Long> {
    List<Cancelacion> findByCitaId(Long citaId);
    List<Cancelacion> findByPacienteId(Long pacienteId);
    List<Cancelacion> findByEstado(String estado);
}