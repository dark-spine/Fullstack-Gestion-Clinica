package com.clinica.pagos.event;

import com.clinica.pagos.dto.PaymentRequestDTO;
import com.clinica.pagos.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentService service;

    @RabbitListener(queues = "appointment.created.queue")
    public void onAppointmentCreated(Map<String, Object> payload) {
        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .appointmentId(payload.get("id") instanceof Number ? ((Number) payload.get("id")).longValue() : null)
                .amount(BigDecimal.valueOf(100.00))
                .type("DEPOSIT")
                .build();
        service.processPayment(request);
    }

    @RabbitListener(queues = "appointment.cancelled.queue")
    public void onAppointmentCancelled(Map<String, Object> payload) {
        Long appointmentId = payload.get("id") instanceof Number ? ((Number) payload.get("id")).longValue() : null;
        if (appointmentId != null) {
            service.refundByAppointment(appointmentId);
        }
    }
}
