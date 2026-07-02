package com.clinica.notificaciones.repository;

import com.clinica.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioId(Long destinatarioId);
    List<Notificacion> findByEstado(String estado);
}