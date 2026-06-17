package com.clinica.pagos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("API Pagos Service")
                .version("1.0.0")
                .description("Gestión de pagos y procesamiento de transacciones financieras para el sistema de clínica")
                .contact(new Contact()
                    .name("Clínica Desarrollo Fullstack")
                    .email("pagos@clinica.com")
                    .url("https://clinica.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8006")
                    .description("Servidor de Desarrollo"),
                new Server()
                    .url("http://api.clinica.local:8006")
                    .description("Servidor de Staging")
            ));
    }
}
