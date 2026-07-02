package com.clinica.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@FeignClient(name = "pagos-client", url = "${pagos.service.url}")
public interface PagosClient {
    @GetMapping("/api/pagos/ingresos/totales")
    Double ingresosTotales(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin);
}