package com.clinica.pagos.repository;

import com.clinica.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByNumeroOrden(String numeroOrden);
    List<Pago> findByPacienteId(Long pacienteId);
    List<Pago> findByCitaId(Long citaId);
    List<Pago> findByEstado(String estado);
    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
}