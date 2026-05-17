package com.clinica.cancelaciones.event;

import com.clinica.cancelaciones.model.CancellationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancellationEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishCancellationProcessed(CancellationRecord record) {
        rabbitTemplate.convertAndSend("cancellations.exchange", "cancellation.processed", record);
    }
}
