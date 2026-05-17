package com.clinica.cancelaciones.repository;

import com.clinica.cancelaciones.model.CancellationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationRecordRepository extends JpaRepository<CancellationRecord, Long> {
}
