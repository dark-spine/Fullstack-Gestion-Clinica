package com.clinica.citas.mapper;

import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toEntity(AppointmentRequestDTO dto);
    AppointmentResponseDTO toResponse(Appointment entity);
}
