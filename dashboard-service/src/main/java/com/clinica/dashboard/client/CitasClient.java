package com.clinica.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "citas-service", url = "http://citas-service:8004")
public interface CitasClient {
    @GetMapping("/api/appointments/count")
    Object getAppointmentsCount();

    @GetMapping("/api/appointments/no-show-rate")
    Object getNoShowRate();
}
