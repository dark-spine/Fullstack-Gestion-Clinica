package com.clinica.dashboard.mapper;

import com.clinica.dashboard.dto.DashboardResponseDTO;
import com.clinica.dashboard.model.ReportEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DashboardMapper {
    DashboardResponseDTO toDto(ReportEntry entity);
}
