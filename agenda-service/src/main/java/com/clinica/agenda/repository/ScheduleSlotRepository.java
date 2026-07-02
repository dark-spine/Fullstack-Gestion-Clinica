package com.clinica.agenda.repository;

import com.clinica.agenda.model.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ScheduleSlot> findByDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime);

    List<ScheduleSlot> findByDoctorIdAndStatus(Long doctorId, String status);

    @Query("SELECT s FROM ScheduleSlot s WHERE s.doctorId = :doctorId AND :startTime < s.endTime AND :endTime > s.startTime")
    List<ScheduleSlot> findOverlapping(@Param("doctorId") Long doctorId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);
}
