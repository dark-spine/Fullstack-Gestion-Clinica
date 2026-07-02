package com.clinica.pagos.event;

import com.clinica.pagos.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentCompleted(Payment payment) {
        rabbitTemplate.convertAndSend("payments.exchange", "payment.completed", payment);
    }

    public void publishPaymentRefunded(Payment payment) {
        rabbitTemplate.convertAndSend("payments.exchange", "payment.refunded", payment);
    }
}
