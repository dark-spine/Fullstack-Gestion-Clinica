package com.clinica.notificaciones.config;

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
    public TopicExchange paymentsExchange() {
        return new TopicExchange("payments.exchange");
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
    public Queue paymentCompletedQueue() {
        return new Queue("payment.completed.queue", true);
    }

    @Bean
    public Binding bindingAppointmentCreated(@Qualifier("appointmentCreatedQueue") Queue appointmentCreatedQueue,
                                             @Qualifier("appointmentExchange") TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentCreatedQueue).to(appointmentExchange).with("appointment.created");
    }

    @Bean
    public Binding bindingAppointmentCancelled(@Qualifier("appointmentCancelledQueue") Queue appointmentCancelledQueue,
                                               @Qualifier("appointmentExchange") TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentCancelledQueue).to(appointmentExchange).with("appointment.cancelled");
    }

    @Bean
    public Binding bindingPaymentCompleted(@Qualifier("paymentCompletedQueue") Queue paymentCompletedQueue,
                                           @Qualifier("paymentsExchange") TopicExchange paymentsExchange) {
        return BindingBuilder.bind(paymentCompletedQueue).to(paymentsExchange).with("payment.completed");
    }
}
