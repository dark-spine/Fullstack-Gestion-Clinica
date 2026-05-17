package com.clinica.cancelaciones.mapper;

import com.clinica.cancelaciones.dto.CancellationResponseDTO;
import com.clinica.cancelaciones.model.CancellationRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CancellationMapper {
    CancellationResponseDTO toResponse(CancellationRecord entity);
}
