package com.clinica.agenda.mapper;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.model.ScheduleSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    ScheduleSlot toEntity(SlotRequestDTO dto);
    SlotResponseDTO toResponse(ScheduleSlot entity);
}
