package com.clinica.citas.event;

import com.clinica.citas.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishAppointmentCreated(Appointment appointment) {
        rabbitTemplate.convertAndSend("appointments.exchange", "appointment.created", appointment);
    }

    public void publishAppointmentCancelled(Appointment appointment) {
        rabbitTemplate.convertAndSend("appointments.exchange", "appointment.cancelled", appointment);
    }
}
