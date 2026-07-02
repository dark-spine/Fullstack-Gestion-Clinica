package com.clinica.dashboard.client;

import com.clinica.dashboard.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "citas-client", url = "${citas.service.url}")
public interface CitasClient {
    @GetMapping("/api/citas/hoy")
    List<CitaDTO> citasHoy();
}