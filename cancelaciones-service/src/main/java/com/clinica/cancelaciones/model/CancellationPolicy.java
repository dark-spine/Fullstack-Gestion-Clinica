package com.clinica.cancelaciones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cancellation_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer hoursBefore;
    private Double noShowFeePercentage;
    private Boolean allowRefund;
}
