package com.clinica.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "medico-service", url = "http://medico-service:8002")
public interface MedicoClient {
    @GetMapping("/api/doctors/count")
    Object getDoctorsCount();
}
