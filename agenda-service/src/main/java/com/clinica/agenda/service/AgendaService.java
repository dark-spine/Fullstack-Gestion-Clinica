package com.clinica.agenda.service;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.mapper.AgendaMapper;
import com.clinica.agenda.model.ScheduleSlot;
import com.clinica.agenda.repository.ScheduleSlotRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final ScheduleSlotRepository repository;
    private final AgendaMapper mapper;

    @Transactional
    public SlotResponseDTO createSlot(SlotRequestDTO dto) {
        if (!repository.findOverlapping(dto.getDoctorId(), dto.getStartTime(), dto.getEndTime()).isEmpty()) {
            throw new EntityExistsException("El médico ya tiene un slot en ese horario");
        }
        ScheduleSlot slot = mapper.toEntity(dto);
        slot.setStatus("AVAILABLE");
        return mapper.toResponse(repository.save(slot));
    }

    @Transactional
    public SlotResponseDTO reserveSlot(Long doctorId, java.time.LocalDateTime startTime) {
        ScheduleSlot slot = repository.findByDoctorIdAndStartTime(doctorId, startTime)
                .orElseThrow(() -> new java.util.NoSuchElementException("Slot no encontrado"));
        if (!"AVAILABLE".equals(slot.getStatus())) {
            throw new IllegalStateException("Slot ya reservado o bloqueado");
        }
        slot.setStatus("BOOKED");
        return mapper.toResponse(repository.save(slot));
    }

    public List<SlotResponseDTO> findAvailable(Long doctorId) {
        return repository.findByDoctorIdAndStatus(doctorId, "AVAILABLE").stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
