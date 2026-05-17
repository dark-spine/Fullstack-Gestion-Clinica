package com.clinica.cancelaciones.service;

import com.clinica.cancelaciones.dto.CancellationRequestDTO;
import com.clinica.cancelaciones.dto.CancellationResponseDTO;
import com.clinica.cancelaciones.event.CancellationEventPublisher;
import com.clinica.cancelaciones.mapper.CancellationMapper;
import com.clinica.cancelaciones.model.CancellationRecord;
import com.clinica.cancelaciones.model.CancellationPolicy;
import com.clinica.cancelaciones.repository.CancellationPolicyRepository;
import com.clinica.cancelaciones.repository.CancellationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CancellationService {
    private final CancellationRecordRepository recordRepository;
    private final CancellationPolicyRepository policyRepository;
    private final CancellationMapper mapper;
    private final CancellationEventPublisher eventPublisher;

    @Transactional
    public CancellationResponseDTO processCancellation(CancellationRequestDTO dto) {
        CancellationPolicy policy = policyRepository.findAll().stream().findFirst().orElse(CancellationPolicy.builder()
                .hoursBefore(24)
                .noShowFeePercentage(50.0)
                .allowRefund(true)
                .build());
        double refundAmount = policy.getAllowRefund() ? 100.0 * (1 - policy.getNoShowFeePercentage() / 100.0) : 0.0;
        CancellationRecord record = CancellationRecord.builder()
                .appointmentId(dto.getAppointmentId())
                .cancelledAt(LocalDateTime.now())
                .refundAmount(refundAmount)
                .reason(dto.getReason())
                .build();
        CancellationRecord saved = recordRepository.save(record);
        eventPublisher.publishCancellationProcessed(saved);
        return mapper.toResponse(saved);
    }
}
