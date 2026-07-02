package com.clinica.pagos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange("appointments.exchange");
    }

    @Bean
    public Queue appointmentCreatedQueue() {
        return new Queue("appointment.created.queue", true);
    }

    @Bean
    public Queue appointmentCancelledQueue() {
        return new Queue("appointment.cancelled.queue", true);
    }

    @Bean
    public Binding bindingCreated(@Qualifier("appointmentCreatedQueue") Queue appointmentCreatedQueue,
                                  TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentCreatedQueue).to(appointmentExchange).with("appointment.created");
    }

    @Bean
    public Binding bindingCancelled(@Qualifier("appointmentCancelledQueue") Queue appointmentCancelledQueue,
                                    TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentCancelledQueue).to(appointmentExchange).with("appointment.cancelled");
    }
}
