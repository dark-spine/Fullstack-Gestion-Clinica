package com.clinica.pagos.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long id;
    private Long appointmentId;
    private BigDecimal amount;
    private String status;
    private String type;
    private LocalDateTime processedAt;
}
