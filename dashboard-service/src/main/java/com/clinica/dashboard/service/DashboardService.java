package com.clinica.dashboard.service;

import com.clinica.dashboard.client.CitasClient;
import com.clinica.dashboard.client.MedicoClient;
import com.clinica.dashboard.client.PagosClient;
import com.clinica.dashboard.dto.DashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final CitasClient citasClient;
    private final PagosClient pagosClient;
    private final MedicoClient medicoClient;

    public DashboardResponseDTO getDashboard() {
        Long totalAppointments = ((Number) citasClient.getAppointmentsCount()).longValue();
        Long totalPayments = ((Number) pagosClient.getPaymentsCount()).longValue();
        Double totalRevenue = ((Number) pagosClient.getRevenue()).doubleValue();
        Double noShowRate = ((Number) citasClient.getNoShowRate()).doubleValue();
        return DashboardResponseDTO.builder()
                .totalAppointments(totalAppointments)
                .totalPayments(totalPayments)
                .totalRevenue(totalRevenue)
                .noShowRate(noShowRate)
                .build();
    }
}
