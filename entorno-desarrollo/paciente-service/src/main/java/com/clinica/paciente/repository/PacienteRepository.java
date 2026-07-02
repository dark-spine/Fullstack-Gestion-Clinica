package com.clinica.paciente.repository;
import com.clinica.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByRut(String rut);
    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByUserId(Long userId);
}