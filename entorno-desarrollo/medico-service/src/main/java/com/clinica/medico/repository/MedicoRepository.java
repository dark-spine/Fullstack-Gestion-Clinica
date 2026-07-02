package com.clinica.medico.repository;
import com.clinica.medico.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByActivoTrue();
    List<Medico> findByEspecialidadId(Long especialidadId);
}