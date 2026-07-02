package com.clinica.citas.service;

import com.clinica.citas.client.AgendaClient;
import com.clinica.citas.dto.AppointmentRequestDTO;
import com.clinica.citas.dto.AppointmentResponseDTO;
import com.clinica.citas.event.AppointmentEventPublisher;
import com.clinica.citas.mapper.AppointmentMapper;
import com.clinica.citas.model.Appointment;
import com.clinica.citas.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Appointment Service Tests")
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository repository;

    @Mock
    private AppointmentMapper mapper;

    @Mock
    private AgendaClient agendaClient;

    @Mock
    private AppointmentEventPublisher eventPublisher;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequestDTO appointmentRequestDTO;
    private Appointment appointment;
    private AppointmentResponseDTO appointmentResponseDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        appointmentRequestDTO = AppointmentRequestDTO.builder()
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        appointmentResponseDTO = AppointmentResponseDTO.builder()
                .id(1L)
                .patientId(200L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("CONFIRMED")
                .build();
    }

    @Test
    @DisplayName("Should create appointment successfully")
    void testCreateAppointmentSuccess() {
        // Arrange
        when(mapper.toEntity(appointmentRequestDTO)).thenReturn(appointment);
        when(repository.save(any(Appointment.class))).thenReturn(appointment);
        when(mapper.toResponse(appointment)).thenReturn(appointmentResponseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.createAppointment(appointmentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200L, result.getPatientId());
        assertEquals("CONFIRMED", result.getStatus());
        verify(agendaClient, times(1)).reserveSlot(100L, startTime.toString());
        verify(eventPublisher, times(1)).publishAppointmentCreated(appointment);
    }

    @Test
    @DisplayName("Should cancel appointment successfully")
    void testCancelAppointmentSuccess() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        when(repository.save(any(Appointment.class))).thenReturn(appointment);
        when(mapper.toResponse(any(Appointment.class))).thenReturn(appointmentResponseDTO);

        // Act
        AppointmentResponseDTO result = appointmentService.cancelAppointment(1L);

        // Assert
        assertNotNull(result);
        verify(eventPublisher, times(1)).publishAppointmentCancelled(any(Appointment.class));
    }

    @Test
    @DisplayName("Should list appointments successfully")
    void testListAppointmentsSuccess() {
        // Arrange
        Appointment appointment2 = Appointment.builder()
                .id(2L)
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CONFIRMED")
                .recurrenceRule(null)
                .build();

        AppointmentResponseDTO responseDTO2 = AppointmentResponseDTO.builder()
                .id(2L)
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("CONFIRMED")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(appointment, appointment2));
        when(mapper.toResponse(appointment)).thenReturn(appointmentResponseDTO);
        when(mapper.toResponse(appointment2)).thenReturn(responseDTO2);

        // Act
        List<AppointmentResponseDTO> result = appointmentService.listAppointments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should count appointments successfully")
    void testCountAppointmentsSuccess() {
        // Arrange
        when(repository.count()).thenReturn(5L);

        // Act
        Long count = appointmentService.countAppointments();

        // Assert
        assertEquals(5L, count);
        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Should calculate no-show rate correctly")
    void testNoShowRateCalculation() {
        // Arrange
        Appointment noShowAppointment = Appointment.builder()
                .id(2L)
                .patientId(201L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("NO_SHOW")
                .recurrenceRule(null)
                .build();

        when(repository.count()).thenReturn(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(appointment, noShowAppointment));

        // Act
        Double rate = appointmentService.noShowRate();

        // Assert
        assertEquals(0.5, rate);
        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Should return 0.0 no-show rate when no appointments exist")
    void testNoShowRateReturnsZeroWhenNoAppointments() {
        // Arrange
        when(repository.count()).thenReturn(0L);
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        Double rate = appointmentService.noShowRate();

        // Assert
        assertEquals(0.0, rate);
    }
}
