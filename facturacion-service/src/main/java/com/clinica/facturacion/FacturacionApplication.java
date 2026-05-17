package com.clinica.facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableFeignClients
@EnableRabbit
public class FacturacionApplication {
    public static void main(String[] args) {
        SpringApplication.run(FacturacionApplication.class, args);
    }
}
