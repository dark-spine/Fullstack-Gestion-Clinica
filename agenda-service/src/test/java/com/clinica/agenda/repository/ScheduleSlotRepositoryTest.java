package com.clinica.agenda.repository;

import com.clinica.agenda.model.ScheduleSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ScheduleSlot Repository Tests")
class ScheduleSlotRepositoryTest {
    @Autowired
    private ScheduleSlotRepository repository;

    private ScheduleSlot slot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        slot = ScheduleSlot.builder()
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();
    }

    @Test
    @DisplayName("Should save slot successfully")
    void testSaveSlotSuccess() {
        // Act
        ScheduleSlot savedSlot = repository.save(slot);

        // Assert
        assertNotNull(savedSlot.getId());
        assertEquals(100L, savedSlot.getDoctorId());
        assertEquals("AVAILABLE", savedSlot.getStatus());
    }

    @Test
    @DisplayName("Should find slot by doctorId and startTime")
    void testFindByDoctorIdAndStartTime() {
        // Arrange
        repository.save(slot);

        // Act
        Optional<ScheduleSlot> foundSlot = repository.findByDoctorIdAndStartTime(100L, startTime);

        // Assert
        assertTrue(foundSlot.isPresent());
        assertEquals(100L, foundSlot.get().getDoctorId());
        assertEquals(startTime, foundSlot.get().getStartTime());
    }

    @Test
    @DisplayName("Should return empty Optional when slot not found")
    void testFindByDoctorIdAndStartTimeNotFound() {
        // Act
        Optional<ScheduleSlot> foundSlot = repository.findByDoctorIdAndStartTime(100L, startTime);

        // Assert
        assertTrue(foundSlot.isEmpty());
    }

    @Test
    @DisplayName("Should find slots by doctorId and status")
    void testFindByDoctorIdAndStatus() {
        // Arrange
        ScheduleSlot slot2 = ScheduleSlot.builder()
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 6, 20, 15, 0))
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        repository.save(slot);
        repository.save(slot2);

        // Act
        List<ScheduleSlot> foundSlots = repository.findByDoctorIdAndStatus(100L, "AVAILABLE");

        // Assert
        assertEquals(2, foundSlots.size());
        assertTrue(foundSlots.stream().allMatch(s -> "AVAILABLE".equals(s.getStatus())));
    }

    @Test
    @DisplayName("Should return empty list when no slots with status found")
    void testFindByDoctorIdAndStatusReturnsEmptyList() {
        // Arrange
        repository.save(slot);

        // Act
        List<ScheduleSlot> foundSlots = repository.findByDoctorIdAndStatus(100L, "BOOKED");

        // Assert
        assertTrue(foundSlots.isEmpty());
    }

    @Test
    @DisplayName("Should find overlapping slots")
    void testFindOverlapping() {
        // Arrange
        ScheduleSlot overlappingSlot = ScheduleSlot.builder()
                .doctorId(100L)
                .startTime(LocalDateTime.of(2025, 6, 20, 10, 30))
                .endTime(LocalDateTime.of(2025, 6, 20, 11, 30))
                .status("BOOKED")
                .durationMinutes(60)
                .build();

        repository.save(slot);
        repository.save(overlappingSlot);

        // Act
        List<ScheduleSlot> overlapping = repository.findOverlapping(100L, startTime, endTime);

        // Assert
        assertTrue(overlapping.size() > 0);
    }

    @Test
    @DisplayName("Should return empty list when no overlapping slots found")
    void testFindOverlappingReturnsEmptyList() {
        // Arrange
        repository.save(slot);
        LocalDateTime nonOverlappingStart = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime nonOverlappingEnd = LocalDateTime.of(2025, 6, 20, 13, 0);

        // Act
        List<ScheduleSlot> overlapping = repository.findOverlapping(100L, nonOverlappingStart, nonOverlappingEnd);

        // Assert
        assertTrue(overlapping.isEmpty());
    }

    @Test
    @DisplayName("Should update slot status successfully")
    void testUpdateSlotStatus() {
        // Arrange
        ScheduleSlot savedSlot = repository.save(slot);
        savedSlot.setStatus("BOOKED");

        // Act
        ScheduleSlot updatedSlot = repository.save(savedSlot);

        // Assert
        assertEquals("BOOKED", updatedSlot.getStatus());
    }

    @Test
    @DisplayName("Should delete slot successfully")
    void testDeleteSlotSuccess() {
        // Arrange
        ScheduleSlot savedSlot = repository.save(slot);
        Long slotId = savedSlot.getId();

        // Act
        repository.deleteById(slotId);

        // Assert
        Optional<ScheduleSlot> deletedSlot = repository.findById(slotId);
        assertTrue(deletedSlot.isEmpty());
    }
}
