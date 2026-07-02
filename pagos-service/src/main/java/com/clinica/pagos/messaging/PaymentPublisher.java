package com.clinica.pagos.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PaymentPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPayment(Map<String, Object> event) {
        rabbitTemplate.convertAndSend(RabbitConfig.PAYMENT_QUEUE, event);
    }
}
