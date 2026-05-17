package com.clinica.citas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "agenda-service", url = "http://agenda-service:8003")
public interface AgendaClient {
    @PostMapping("/api/agenda/slots/reserve")
    void reserveSlot(@RequestParam("doctorId") Long doctorId, @RequestParam("startTime") String startTime);
}
