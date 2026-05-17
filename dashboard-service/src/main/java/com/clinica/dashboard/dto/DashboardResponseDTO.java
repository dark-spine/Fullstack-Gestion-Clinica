package com.clinica.dashboard.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDTO {
    private Long totalAppointments;
    private Long totalPayments;
    private Double totalRevenue;
    private Double noShowRate;
}
