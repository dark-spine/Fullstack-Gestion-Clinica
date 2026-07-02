package com.clinica.cancelaciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationRequestDTO {
    private Long appointmentId;
    private String reason;
}
