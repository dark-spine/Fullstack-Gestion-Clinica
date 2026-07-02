package com.clinica.citas.client;

import com.clinica.citas.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "paciente-client", url = "${pacientes.service.url}")
public interface PacienteClient {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO getPacienteById(@PathVariable("id") Long id);
}