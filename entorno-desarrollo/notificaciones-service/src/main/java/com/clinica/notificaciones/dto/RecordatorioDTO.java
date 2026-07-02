package com.clinica.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordatorioDTO {
    private Long citaId;
    private Long destinatarioId;
    private LocalDateTime fechaCita;
    private String medicoNombre;
}