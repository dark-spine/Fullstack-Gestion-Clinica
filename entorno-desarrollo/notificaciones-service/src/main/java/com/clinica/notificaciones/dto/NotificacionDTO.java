package com.clinica.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long id;
    private Long destinatarioId;
    private String tipo;
    private String asunto;
    private String mensaje;
    private String estado;
    private LocalDateTime createdAt;
}