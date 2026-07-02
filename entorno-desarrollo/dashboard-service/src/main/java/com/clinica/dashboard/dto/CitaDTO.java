package com.clinica.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String estado;
    private LocalDateTime createdAt;
}