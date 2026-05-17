package com.clinica.facturacion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pagos-service", url = "http://pagos-service:8006")
public interface PagosClient {
    @GetMapping("/api/payments/summary")
    Object getPaymentSummary(@RequestParam("appointmentId") Long appointmentId);
}
