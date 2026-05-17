package com.clinica.facturacion.mapper;

import com.clinica.facturacion.dto.InvoiceRequestDTO;
import com.clinica.facturacion.dto.InvoiceResponseDTO;
import com.clinica.facturacion.model.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toEntity(InvoiceRequestDTO dto);
    InvoiceResponseDTO toResponse(Invoice entity);
}
