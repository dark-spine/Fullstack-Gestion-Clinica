# Arquitectura de Microservicios - Documentación Técnica

## 🏗️ Visión General de la Arquitectura

El proyecto implementa una **arquitectura de microservicios distribuida** para un sistema de gestión de clínicas médicas, utilizando **Spring Boot 3.2.5**, **Spring Cloud 2023.0.1** y patrones modernos de desarrollo.

```
┌─────────────────────────────────────────────────────────┐
│                    Cliente / Frontend                    │
└──────────────────────┬──────────────────────────────────┘
                       │ HTTP/REST
                       ▼
┌─────────────────────────────────────────────────────────┐
│              API Gateway (Spring Cloud Gateway)          │
│                       :8080                              │
│  • Enrutamiento inteligente                              │
│  • Load Balancing                                        │
│  • Descubrimiento de servicios (Eureka)                  │
└──────────────────────┬──────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    ┌────────┐    ┌────────┐    ┌────────┐
    │ Eureka │    │Service │    │Service │
    │ Server │    │  Pool  │    │  Pool  │
    │ :8761  │    │        │    │        │
    └────────┘    └────────┘    └────────┘

┌──────────────────────────────────────────────────────────┐
│        Servicios de Negocio (Microservicios)             │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │   Citas     │  │Cancelaciones│  │  Paciente   │      │
│  │  Service    │  │  Service    │  │  Service    │      │
│  │    :8084    │  │    :8085    │  │    :8086    │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
│                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │  Médico     │  │   Usuario   │  │   Agenda    │      │
│  │  Service    │  │  Service    │  │   Service   │      │
│  │    :8087    │  │    :8088    │  │    :8089    │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
│                                                           │
│  ┌──────────────────────────────────────────────────┐   │
│  │ + Dashboard, Facturación, Notificaciones, Pagos │   │
│  └──────────────────────────────────────────────────┘   │
│                                                           │
└──────────────────────────────────────────────────────────┘
         │                │                │
         ▼                ▼                ▼
    ┌─────────┐      ┌─────────┐      ┌─────────┐
    │ MySQL   │      │ MySQL   │      │ MySQL   │
    │ Citas   │      │Cancelac │      │Paciente │
    │   DB    │      │   DB    │      │   DB    │
    └─────────┘      └─────────┘      └─────────┘
```

---

## 🎯 Componentes Principales

### 1. API Gateway

**Responsabilidad**: Punto de entrada único para todas las solicitudes

**Tecnología**: Spring Cloud Gateway

**Características**:
- ✅ Enrutamiento por URL path
- ✅ Load balancing automático
- ✅ Service discovery integrado
- ✅ Reescritura de URLs
- ✅ Filtros personalizados

**Configuración**:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: citas-service
          uri: lb://citas-service
          predicates:
            - Path=/api/citas/**
```

**Puerto**: 8080

**Ejemplo de Rutas**:
```
GET  http://localhost:8080/api/citas          → citas-service:8084/api/citas
POST http://localhost:8080/api/citas          → citas-service:8084/api/citas
GET  http://localhost:8080/api/pacientes      → paciente-service:8086/api/pacientes
```

---

### 2. Eureka Server (Service Registry)

**Responsabilidad**: Descubrimiento y registro de servicios

**Tecnología**: Spring Cloud Netflix Eureka

**Características**:
- ✅ Registro automático de servicios
- ✅ Health checks periódicos
- ✅ Deregistro automático en caso de fallo
- ✅ Dashboard web de monitoreo
- ✅ Tolerancia a fallos

**Puerto**: 8761

**Dashboard**: http://localhost:8761/

**Registro de Servicio**:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    instance-id: ${spring.application.name}:${server.port}
    prefer-ip-address: true
```

---

### 3. Microservicios de Negocio

Cada microservicio implementa el patrón **CSR (Controller-Service-Repository)**:

#### Estructura de Capas

```
microservicio/
├── src/main/java/com/clinica/[service]/
│   ├── [Service]Application.java          # Boot entry point
│   ├── controller/
│   │   └── [Entity]Controller.java         # REST Endpoints
│   ├── service/
│   │   └── [Entity]Service.java            # Lógica de negocio
│   ├── repository/
│   │   └── [Entity]Repository.java         # Acceso a datos (JPA)
│   ├── model/
│   │   └── [Entity].java                   # Entidad JPA
│   └── dto/
│       ├── [Entity]DTO.java                # DTO para respuestas
│       └── [Entity]CreateDTO.java          # DTO para solicitudes
│
├── src/test/java/
│   ├── model/        # Tests de entidad
│   ├── repository/   # Tests de persistencia
│   ├── service/      # Tests de lógica (Mockito)
│   └── controller/   # Tests de endpoints (MockMvc)
│
├── pom.xml                                  # Dependencias Maven
├── Dockerfile                               # Containerización
└── src/main/resources/application.yml       # Configuración
```

---

## 📡 Patrones de Comunicación

### Comunicación Sincrónica: REST + Feign Client

**Uso**: Cuando un servicio necesita información de otro en tiempo real

**Ejemplo: Citas-Service → Paciente-Service**

```java
// 1. Definir cliente Feign
@FeignClient(name = "paciente-service")
public interface PacienteClient {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO obtenerPaciente(@PathVariable Long id);
}

// 2. Inyectar en servicio
@Service
public class CitaService {
    @Autowired
    private PacienteClient pacienteClient;
    
    public CitaDTO crearCita(CitaCreateDTO request) {
        // Llamada al servicio remoto
        PacienteDTO paciente = pacienteClient.obtenerPaciente(
            request.getPacienteId()
        );
        
        if (paciente == null) {
            throw new PacienteNoEncontradoException();
        }
        
        // Crear cita...
        return citaDTO;
    }
}
```

**Ventajas**:
- ✅ Sincrónica y predecible
- ✅ Integración directa con Eureka
- ✅ Manejo automático de resiliencia

**Desventajas**:
- ❌ Acoplamiento temporal
- ❌ Fallos en cascada

---

## 🗄️ Patrón de Datos: Database per Service

Cada microservicio tiene su propia base de datos:

```
Citas Service          → citasdb
Cancelaciones Service  → cancelacionesdb
Paciente Service       → pacientesdb
Médico Service         → medicosdb
Usuario Service        → usuariosdb
```

**Beneficios**:
- ✅ Escalabilidad independiente
- ✅ Autonomía en cambios de schema
- ✅ Tecnología heterogénea (si es necesario)

**Desafíos**:
- ❌ Consistencia distribuida
- ❌ Transacciones ACID limitadas

---

## 🔐 Configuración de Seguridad

### Validación de Datos

**Bean Validation** en DTOs:
```java
@Data
public class CitaCreateDTO {
    @NotNull(message = "Paciente ID no puede ser nulo")
    private Long pacienteId;
    
    @NotNull(message = "Médico ID no puede ser nulo")
    private Long medicoId;
    
    @NotBlank(message = "Motivo consulta es requerido")
    private String motivoConsulta;
}
```

### Manejo de Excepciones Global

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException e) {
        
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Validación fallida");
        error.setErrors(e.getBindingResult().getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList()));
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

---

## 🧪 Pruebas

### Cobertura de 4 Capas

1. **Model Tests** (5-10 tests)
   - Validación de getters/setters
   - Comportamiento de entidad

2. **Repository Tests** (7-10 tests)
   - CRUD completo
   - Consultas personalizadas
   - Transacciones

3. **Service Tests** (8-15 tests)
   - Lógica de negocio
   - Mocks con Mockito
   - Manejo de excepciones

4. **Controller Tests** (6-10 tests)
   - Endpoints REST
   - Códigos HTTP
   - Validación de request/response

**Cobertura Total**: ≥ 80%

---

## 📊 Diagrama de Secuencia: Crear Cita

```
Cliente
  │
  │ POST /api/citas
  ▼
┌──────────────────┐
│  API Gateway     │ → Descubre citas-service en Eureka
│     :8080        │
└────────┬─────────┘
         │
         │ Forward al CitasController
         ▼
┌──────────────────────────┐
│  Citas Service           │
│  :8084                   │
│                          │
│  CitasController         │
│  ├─ validates request    │
│  │                       │
│  └─> CitasService       │
│      ├─ calls PacienteClient
│      │  ▼ (REST a Paciente Service)
│      ├─ calls MedicoClient
│      │  ▼ (REST a Médico Service)
│      ├─ validates business logic
│      │                   │
│      └─> CitasRepository │
│         └─> MySQL        │
│            (INSERT)      │
│                          │
└────────┬─────────────────┘
         │
         │ Return CitaDTO
         ▼
    200 OK
```

---

## 🚀 Escalabilidad

### Horizontal Scaling con Docker Compose

Para escalar un servicio, aumentar el número de instancias:

```yaml
# docker-compose.yml
citas-service:
  deploy:
    replicas: 3  # 3 instancias
```

Eureka distribuye automáticamente el tráfico entre ellas.

### Load Balancing

API Gateway usa **Round Robin** por defecto:

```
Request 1 → citas-service:1
Request 2 → citas-service:2
Request 3 → citas-service:3
Request 4 → citas-service:1
```

---

## 🔍 Monitoreo

### Endpoints de Salud

```bash
# API Gateway
curl http://localhost:8080/actuator/health

# Eureka
curl http://localhost:8761/eureka/status

# Servicio individual
curl http://localhost:8084/actuator/health
```

### Logs Centralizados

Cada servicio escribe logs en:
```
level: DEBUG para com.clinica
level: INFO para el resto
```

---

## 🌐 Flujo de Solicitud End-to-End

### Crear una Cita

1. **Cliente hace solicitud**:
   ```bash
   POST http://localhost:8080/api/citas
   {
     "pacienteId": 1,
     "medicoId": 1,
     "slotAgendaId": 1,
     "motivoConsulta": "Consulta general"
   }
   ```

2. **API Gateway**:
   - Recibe solicitud
   - Consulta Eureka por "citas-service"
   - Forwarda a una instancia
   - Maneja load balancing

3. **Citas-Service**:
   - CitasController valida entrada
   - CitasService ejecuta lógica:
     - Llama PacienteClient
     - Llama MedicoClient
     - Valida disponibilidad
   - CitasRepository persiste
   - Retorna CitaDTO

4. **Respuesta**:
   ```json
   {
     "id": 1,
     "pacienteId": 1,
     "medicoId": 1,
     "estado": "CONFIRMADA"
   }
   ```

---

## 📚 Tecnologías Stack

| Layer | Tecnología | Versión |
|-------|-----------|---------|
| **Runtime** | Java | 21 LTS |
| **Framework** | Spring Boot | 3.2.5 |
| **Cloud** | Spring Cloud | 2023.0.1 |
| **Discovery** | Eureka | Integrated |
| **Gateway** | Spring Cloud Gateway | Integrated |
| **Communication** | OpenFeign | Integrated |
| **Data** | JPA/Hibernate | Integrated |
| **Database** | MySQL | 8.0 |
| **Testing** | JUnit 5 + Mockito | Latest |
| **Documentation** | Swagger/OpenAPI | 2.1.0 |
| **Container** | Docker | Latest |
| **Orchestration** | Docker Compose | 3.8 |

---

## 📖 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Microservices Patterns](https://microservices.io/)
- [12 Factor App](https://12factor.net/)
