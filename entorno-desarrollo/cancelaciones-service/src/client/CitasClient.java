package com.clinica.cancelaciones.client;

import com.clinica.cancelaciones.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "citas-client", url = "${citas.service.url}")
public interface CitasClient {

    @GetMapping("/api/citas/{id}")
    CitaDTO obtenerCitaPorId(@PathVariable("id") Long id);

    @PatchMapping("/api/citas/{id}/estado")
    void cambiarEstadoCita(@PathVariable("id") Long id, @RequestParam("estado") String estado);
}