package com.clinica.cancelaciones.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationResponseDTO {
    private Long id;
    private Long appointmentId;
    private LocalDateTime cancelledAt;
    private Double refundAmount;
    private String reason;
}
