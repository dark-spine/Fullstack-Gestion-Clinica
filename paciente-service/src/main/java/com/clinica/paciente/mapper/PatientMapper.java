package com.clinica.paciente.mapper;

import com.clinica.paciente.dto.PatientRequestDTO;
import com.clinica.paciente.dto.PatientResponseDTO;
import com.clinica.paciente.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(PatientRequestDTO dto);
    PatientResponseDTO toResponse(Patient entity);
}
