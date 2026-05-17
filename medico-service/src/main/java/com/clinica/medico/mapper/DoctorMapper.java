package com.clinica.medico.mapper;

import com.clinica.medico.dto.DoctorRequestDTO;
import com.clinica.medico.dto.DoctorResponseDTO;
import com.clinica.medico.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toEntity(DoctorRequestDTO dto);
    DoctorResponseDTO toResponse(Doctor entity);
}
