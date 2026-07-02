package com.clinica.pagos.service;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.dto.PaymentResponseDTO;
import com.clinica.pagos.event.PaymentEventPublisher;
import com.clinica.pagos.mapper.PaymentMapper;
import com.clinica.pagos.model.Payment;
import com.clinica.pagos.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final PaymentEventPublisher eventPublisher;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        Payment payment = mapper.toEntity(dto);
        payment.setStatus("COMPLETED");
        payment.setProcessedAt(LocalDateTime.now());
        Payment saved = repository.save(payment);
        eventPublisher.publishPaymentCompleted(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    public PaymentResponseDTO refundPayment(Long paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow();
        payment.setStatus("REFUNDED");
        Payment refunded = repository.save(payment);
        eventPublisher.publishPaymentRefunded(refunded);
        return mapper.toResponse(refunded);
    }

    @Transactional
    public PaymentResponseDTO refundByAppointment(Long appointmentId) {
        Payment payment = repository.findByAppointmentId(appointmentId).stream().findFirst().orElseThrow();
        payment.setStatus("REFUNDED");
        Payment refunded = repository.save(payment);
        eventPublisher.publishPaymentRefunded(refunded);
        return mapper.toResponse(refunded);
    }

    public List<PaymentResponseDTO> listPayments() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public Long countPayments() {
        return repository.count();
    }

    public Double totalRevenue() {
        return repository.findAll().stream()
                .filter(p -> "COMPLETED".equals(p.getStatus()))
                .map(p -> p.getAmount() == null ? java.math.BigDecimal.ZERO : p.getAmount())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                .doubleValue();
    }

    public Object paymentSummary(Long appointmentId) {
        return repository.findByAppointmentId(appointmentId);
    }
}
