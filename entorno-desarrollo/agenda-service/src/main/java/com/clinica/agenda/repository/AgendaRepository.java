package com.clinica.agenda.repository;
import com.clinica.agenda.model.SlotAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<SlotAgenda, Long> {
    List<SlotAgenda> findByMedicoIdAndFecha(Long medicoId, LocalDate fecha);
    List<SlotAgenda> findByMedicoIdAndEstado(Long medicoId, String estado);
}