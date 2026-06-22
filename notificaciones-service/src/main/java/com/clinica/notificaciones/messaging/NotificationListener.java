package com.clinica.notificaciones.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationListener {

    @RabbitListener(queues = "payments")
    public void handlePayment(Map<String, Object> event) {
        // Minimal handler: in real app send email/push
        System.out.println("[notifications] payment event received: " + event);
    }
}
