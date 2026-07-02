package com.clinica.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "pagos-service", url = "http://pagos-service:8006")
public interface PagosClient {
    @GetMapping("/api/payments/count")
    Object getPaymentsCount();

    @GetMapping("/api/payments/revenue")
    Object getRevenue();
}
