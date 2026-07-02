package com.clinica.cancelaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelacionDTO {
    private Long id;
    private Long citaId;
    private Long pacienteId;
    private String motivo;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private String motivoRespuesta;
}