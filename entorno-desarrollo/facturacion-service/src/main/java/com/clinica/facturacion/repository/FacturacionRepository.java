package com.clinica.facturacion.repository;

import com.clinica.facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByPacienteId(Long pacienteId);
    List<Factura> findByMedicoId(Long medicoId);
    List<Factura> findByMedicoIdAndCreatedAtBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fin);
    List<Factura> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
}