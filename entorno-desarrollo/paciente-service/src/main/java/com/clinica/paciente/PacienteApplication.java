package com.clinica.paciente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PacienteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacienteApplication.class, args);
    }
}

server.port=8085
spring.application.name=paciente-service
spring.datasource.url=jdbc:mysql://db-paciente:3306/pacientedb
...