package com.clinica.facturacion.controller;

import com.clinica.facturacion.dto.InvoiceRequestDTO;
import com.clinica.facturacion.dto.InvoiceResponseDTO;
import com.clinica.facturacion.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponseDTO create(@RequestBody InvoiceRequestDTO dto) {
        return service.generateInvoice(dto);
    }
}
