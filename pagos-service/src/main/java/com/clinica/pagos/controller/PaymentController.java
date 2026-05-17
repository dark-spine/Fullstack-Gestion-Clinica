package com.clinica.pagos.controller;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.dto.PaymentResponseDTO;
import com.clinica.pagos.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDTO process(@RequestBody PaymentRequestDTO dto) {
        return service.processPayment(dto);
    }

    @PostMapping("/{id}/refund")
    public PaymentResponseDTO refund(@PathVariable Long id) {
        return service.refundPayment(id);
    }

    @GetMapping
    public List<PaymentResponseDTO> list() {
        return service.listPayments();
    }

    @GetMapping("/count")
    public Long count() {
        return service.countPayments();
    }

    @GetMapping("/revenue")
    public Double revenue() {
        return service.totalRevenue();
    }

    @GetMapping("/summary")
    public Object summary(@RequestParam("appointmentId") Long appointmentId) {
        return service.paymentSummary(appointmentId);
    }
}
