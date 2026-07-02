package com.clinica.dashboard.service;

import com.clinica.dashboard.client.CitasClient;
import com.clinica.dashboard.client.MedicoClient;
import com.clinica.dashboard.client.PagosClient;
import com.clinica.dashboard.dto.CitaDTO;
import com.clinica.dashboard.dto.MedicoDTO;
import com.clinica.dashboard.model.LogConsulta;
import com.clinica.dashboard.repository.LogConsultaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {
    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);
    private final CitasClient citasClient;
    private final PagosClient pagosClient;
    private final MedicoClient medicoClient;
    private final LogConsultaRepository logRepo;

    public DashboardService(CitasClient citasClient, PagosClient pagosClient, MedicoClient medicoClient, LogConsultaRepository logRepo) {
        this.citasClient = citasClient;
        this.pagosClient = pagosClient;
        this.medicoClient = medicoClient;
        this.logRepo = logRepo;
    }

    private void registrarLog(String endpoint) {
        LogConsulta log = new LogConsulta();
        log.setEndpoint(endpoint);
        log.setFechaConsulta(LocalDateTime.now());
        logRepo.save(log);
    }

    public List<CitaDTO> citasHoy() {
        registrarLog("/citas/hoy");
        return citasClient.citasHoy();
    }

    public Double ingresosTotales(LocalDateTime inicio, LocalDateTime fin) {
        registrarLog("/ingresos/totales");
        return pagosClient.ingresosTotales(inicio, fin);
    }

    public List<MedicoDTO> medicosActivos() {
        registrarLog("/medicos/activos");
        return medicoClient.medicosActivos();
    }
}