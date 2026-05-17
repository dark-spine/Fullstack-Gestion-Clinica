package com.clinica.cancelaciones.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public TopicExchange cancellationsExchange() {
        return new TopicExchange("cancellations.exchange");
    }

    @Bean
    public Queue cancellationProcessedQueue() {
        return new Queue("cancellation.processed.queue", true);
    }

    @Bean
    public Binding bindingCancellationProcessed(Queue cancellationProcessedQueue, TopicExchange cancellationsExchange) {
        return BindingBuilder.bind(cancellationProcessedQueue).to(cancellationsExchange).with("cancellation.processed");
    }
}
