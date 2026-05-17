package com.clinica.dashboard.repository;

import com.clinica.dashboard.model.ReportEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportEntryRepository extends JpaRepository<ReportEntry, Long> {
}
