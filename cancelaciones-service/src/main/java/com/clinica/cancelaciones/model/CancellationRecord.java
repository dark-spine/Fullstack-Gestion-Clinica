package com.clinica.cancelaciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cancellation_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long appointmentId;
    private LocalDateTime cancelledAt;
    private Double refundAmount;
    private String reason;
}
