package com.clinica.cancelaciones.repository;

import com.clinica.cancelaciones.model.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {
}
