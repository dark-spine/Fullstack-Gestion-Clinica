package com.clinica.dashboard.client;

import com.clinica.dashboard.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "medico-client", url = "${medicos.service.url}")
public interface MedicoClient {
    @GetMapping("/api/medicos/activos")
    List<MedicoDTO> medicosActivos();
}