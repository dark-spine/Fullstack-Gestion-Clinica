package com.clinica.citas.client;

import com.clinica.citas.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medico-client", url = "${medicos.service.url}")
public interface MedicoClient {
    @GetMapping("/api/medicos/{id}")
    MedicoDTO getMedicoById(@PathVariable("id") Long id);
}