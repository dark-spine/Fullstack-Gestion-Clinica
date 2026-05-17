package com.clinica.citas.service;

import com.clinica.citas.client.AgendaClient;
import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.event.AppointmentEventPublisher;
import com.clinica.citas.mapper.AppointmentMapper;
import com.clinica.citas.model.Appointment;
import com.clinica.citas.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final AgendaClient agendaClient;
    private final AppointmentEventPublisher eventPublisher;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
        agendaClient.reserveSlot(dto.getDoctorId(), dto.getStartTime().toString());
        Appointment appointment = mapper.toEntity(dto);
        appointment.setStatus("CONFIRMED");
        Appointment saved = repository.save(appointment);
        eventPublisher.publishAppointmentCreated(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(Long id) {
        Appointment appointment = repository.findById(id).orElseThrow();
        appointment.setStatus("CANCELLED");
        Appointment updated = repository.save(appointment);
        eventPublisher.publishAppointmentCancelled(updated);
        return mapper.toResponse(updated);
    }

    public List<AppointmentResponseDTO> listAppointments() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public Long countAppointments() {
        return repository.count();
    }

    public Double noShowRate() {
        long total = repository.count();
        long noShows = repository.findAll().stream().filter(a -> "NO_SHOW".equals(a.getStatus())).count();
        return total == 0 ? 0.0 : (double) noShows / total;
    }
}
