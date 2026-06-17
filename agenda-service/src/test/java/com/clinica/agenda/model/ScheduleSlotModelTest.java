package com.clinica.agenda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ScheduleSlot Model Tests")
class ScheduleSlotModelTest {
    private ScheduleSlot slot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.of(2025, 6, 20, 10, 0);
        endTime = LocalDateTime.of(2025, 6, 20, 11, 0);

        slot = ScheduleSlot.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();
    }

    @Test
    @DisplayName("Should create ScheduleSlot with all fields")
    void testCreateScheduleSlotWithAllFields() {
        // Act
        ScheduleSlot testSlot = new ScheduleSlot(2L, 101L, startTime, endTime, "BOOKED", 60);

        // Assert
        assertNotNull(testSlot);
        assertEquals(2L, testSlot.getId());
        assertEquals(101L, testSlot.getDoctorId());
        assertEquals(startTime, testSlot.getStartTime());
        assertEquals(endTime, testSlot.getEndTime());
        assertEquals("BOOKED", testSlot.getStatus());
        assertEquals(60, testSlot.getDurationMinutes());
    }

    @Test
    @DisplayName("Should set and get doctorId")
    void testSetAndGetDoctorId() {
        // Arrange
        Long newDoctorId = 200L;

        // Act
        slot.setDoctorId(newDoctorId);

        // Assert
        assertEquals(newDoctorId, slot.getDoctorId());
    }

    @Test
    @DisplayName("Should set and get startTime")
    void testSetAndGetStartTime() {
        // Arrange
        LocalDateTime newStartTime = LocalDateTime.of(2025, 6, 21, 14, 0);

        // Act
        slot.setStartTime(newStartTime);

        // Assert
        assertEquals(newStartTime, slot.getStartTime());
    }

    @Test
    @DisplayName("Should set and get status")
    void testSetAndGetStatus() {
        // Arrange
        String newStatus = "BOOKED";

        // Act
        slot.setStatus(newStatus);

        // Assert
        assertEquals(newStatus, slot.getStatus());
    }

    @Test
    @DisplayName("Should set and get durationMinutes")
    void testSetAndGetDurationMinutes() {
        // Arrange
        Integer newDuration = 90;

        // Act
        slot.setDurationMinutes(newDuration);

        // Assert
        assertEquals(newDuration, slot.getDurationMinutes());
    }

    @Test
    @DisplayName("Should test equality between two identical slots")
    void testEqualityBetweenIdenticalSlots() {
        // Arrange
        ScheduleSlot slot1 = ScheduleSlot.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        ScheduleSlot slot2 = ScheduleSlot.builder()
                .id(1L)
                .doctorId(100L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        // Assert
        assertEquals(slot1, slot2);
    }

    @Test
    @DisplayName("Should test inequality between different slots")
    void testInequalityBetweenDifferentSlots() {
        // Arrange
        ScheduleSlot differentSlot = ScheduleSlot.builder()
                .id(2L)
                .doctorId(101L)
                .startTime(LocalDateTime.of(2025, 6, 21, 10, 0))
                .endTime(LocalDateTime.of(2025, 6, 21, 11, 0))
                .status("BOOKED")
                .durationMinutes(60)
                .build();

        // Assert
        assertNotEquals(slot, differentSlot);
    }

    @Test
    @DisplayName("Should verify all fields are non-null when using Builder")
    void testAllFieldsNonNullWithBuilder() {
        // Arrange
        ScheduleSlot builtSlot = ScheduleSlot.builder()
                .id(3L)
                .doctorId(103L)
                .startTime(startTime)
                .endTime(endTime)
                .status("AVAILABLE")
                .durationMinutes(60)
                .build();

        // Assert
        assertAll(
                () -> assertNotNull(builtSlot.getId()),
                () -> assertNotNull(builtSlot.getDoctorId()),
                () -> assertNotNull(builtSlot.getStartTime()),
                () -> assertNotNull(builtSlot.getEndTime()),
                () -> assertNotNull(builtSlot.getStatus()),
                () -> assertNotNull(builtSlot.getDurationMinutes())
        );
    }
}
