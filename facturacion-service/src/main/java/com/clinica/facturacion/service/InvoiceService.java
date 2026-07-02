package com.clinica.facturacion.service;

import com.clinica.facturacion.client.PagosClient;
import com.clinica.facturacion.dto.InvoiceRequestDTO;
import com.clinica.facturacion.dto.InvoiceResponseDTO;
import com.clinica.facturacion.mapper.InvoiceMapper;
import com.clinica.facturacion.model.Invoice;
import com.clinica.facturacion.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;
    private final PagosClient pagosClient;

    @Transactional
    public InvoiceResponseDTO generateInvoice(InvoiceRequestDTO dto) {
        pagosClient.getPaymentSummary(dto.getAppointmentId());
        Invoice invoice = mapper.toEntity(dto);
        invoice.setInvoiceNumber("INV-" + dto.getAppointmentId() + "-" + System.currentTimeMillis());
        invoice.setIssueDate(LocalDate.now());
        return mapper.toResponse(repository.save(invoice));
    }
}
