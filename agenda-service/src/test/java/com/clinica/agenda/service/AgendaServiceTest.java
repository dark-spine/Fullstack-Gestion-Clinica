package com.clinica.agenda.service;

import com.clinica.agenda.dto.SlotRequestDTO;
import com.clinica.agenda.dto.SlotResponseDTO;
import com.clinica.agenda.mapper.AgendaMapper;
import com.clinica.agenda.model.ScheduleSlot;
import com.clinica.agenda.repository.ScheduleSlotRepository;
import jakarta.persistence.EntityExistsException;
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
@DisplayName("Agenda Service Tests")
class AgendaServiceTest {
    @Mock
    private ScheduleSlotRepository repository;

    @Mock
    private AgendaMapper mapper;

    @InjectMocks
    private AgendaService agendaService;

    private SlotRequestDTO slotRequestDTO;
    private ScheduleSlot slot;
    private SlotResponseDTO slotResponseDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        slotRequestDTO = SlotRequestDTO.builder()
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .durationMinutes(60)
                .build();

        slot = ScheduleSlot.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        slotResponseDTO = SlotResponseDTO.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();
    }

    @Test
    @DisplayName("Should create slot successfully")
    void testCreateSlotSuccess() {
        // Arrange
        when(repository.findOverlapping(100L, startTime, endTime)).thenReturn(Arrays.asList());
        when(mapper.toEntity(slotRequestDTO)).thenReturn(slot);
        when(repository.save(any(ScheduleSlot.class))).thenReturn(slot);
        when(mapper.toResponse(slot)).thenReturn(slotResponseDTO);

        // Act
        SlotResponseDTO result = agendaService.createSlot(slotRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getDoctorId());
        assertEquals("AVAILABLE", result.getStatus());
        verify(repository, times(1)).save(any(ScheduleSlot.class));
    }

    @Test
    @DisplayName("Should throw exception when slot overlaps")
    void testCreateSlotThrowsExceptionWhenOverlaps() {
        // Arrange
        ScheduleSlot overlappingSlot = ScheduleSlot.builder()
                .id(2L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("BOOKED")
                .durationMinutes(60)
                .build();

        when(repository.findOverlapping(100L, startTime, endTime)).thenReturn(Arrays.asList(overlappingSlot));

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> agendaService.createSlot(slotRequestDTO));
        verify(repository, never()).save(any(ScheduleSlot.class));
    }

    @Test
    @DisplayName("Should reserve slot successfully")
    void testReserveSlotSuccess() {
        // Arrange
        when(repository.findByDoctorIdAndStartTime(100L, startTime)).thenReturn(Optional.of(slot));
        when(repository.save(any(ScheduleSlot.class))).thenReturn(slot);
        when(mapper.toResponse(any(ScheduleSlot.class))).thenReturn(slotResponseDTO);

        // Act
        SlotResponseDTO result = agendaService.reserveSlot(100L, startTime);

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).save(any(ScheduleSlot.class));
    }

    @Test
    @DisplayName("Should throw exception when slot not found for reservation")
    void testReserveSlotThrowsExceptionWhenNotFound() {
        // Arrange
        when(repository.findByDoctorIdAndStartTime(100L, startTime)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> agendaService.reserveSlot(100L, startTime));
        verify(repository, never()).save(any(ScheduleSlot.class));
    }

    @Test
    @DisplayName("Should find available slots successfully")
    void testFindAvailableSuccess() {
        // Arrange
        ScheduleSlot slot2 = ScheduleSlot.builder()
                .id(2L)
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 6, 20, 15, 0))
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        SlotResponseDTO responseDTO2 = SlotResponseDTO.builder()
                .id(2L)
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 6, 20, 15, 0))
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        when(repository.findByDoctorIdAndStatus(100L, "AVAILABLE")).thenReturn(Arrays.asList(slot, slot2));
        when(mapper.toResponse(slot)).thenReturn(slotResponseDTO);
        when(mapper.toResponse(slot2)).thenReturn(responseDTO2);

        // Act
        List<SlotResponseDTO> result = agendaService.findAvailable(100L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findByDoctorIdAndStatus(100L, "AVAILABLE");
    }

    @Test
    @DisplayName("Should find no available slots when doctor has none")
    void testFindAvailableReturnsEmptyList() {
        // Arrange
        when(repository.findByDoctorIdAndStatus(100L, "AVAILABLE")).thenReturn(Arrays.asList());

        // Act
        List<SlotResponseDTO> result = agendaService.findAvailable(100L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByDoctorIdAndStatus(100L, "AVAILABLE");
    }
}
