package com.clinica.pagos.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long appointmentId;
    private BigDecimal amount;
    private String type;
}
