package com.clinica.notificaciones.event;

import com.clinica.notificaciones.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @RabbitListener(queues = "appointment.created.queue")
    public void onAppointmentCreated(Map<String, Object> payload) {
        notificationService.sendNotification("patient-" + payload.get("patientId"), "EMAIL", "Su cita ha sido confirmada.");
    }

    @RabbitListener(queues = "appointment.cancelled.queue")
    public void onAppointmentCancelled(Map<String, Object> payload) {
        notificationService.sendNotification("patient-" + payload.get("patientId"), "SMS", "Su cita ha sido cancelada.");
    }

    @RabbitListener(queues = "payment.completed.queue")
    public void onPaymentCompleted(Map<String, Object> payload) {
        notificationService.sendNotification("patient-" + payload.get("appointmentId"), "WHATSAPP", "Su pago fue procesado correctamente.");
    }
}
