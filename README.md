<<<<<<< HEAD
# Fullstack Gestión Clínica - Microservicios

Proyecto de microservicios para la gestión de un sistema clínico, desarrollado con **Spring Boot 3.2.12**, **PostgreSQL 15**, **RabbitMQ** y **Docker Compose**.

## Integrantes del Equipo

- Daniel Simms | Pablo Toro | Luis Reyes

## 📋 Descripción del Proyecto

Sistema modular de gestión clínica con 10 microservicios independientes que manejan:
- Gestión de usuarios y autenticación
- Administración de médicos y especialidades
- Gestión de agendas y disponibilidad de citas
- Reserva y gestión de citas médicas
- Procesamiento de pagos
- Facturación electrónica
- Cancelación de citas
- Notificaciones a usuarios
- Datos de pacientes
- Dashboards analíticos

## 🏗️ Arquitectura

### Microservicios (10 servicios)

| Servicio | Puerto | Descripción | Status |
|----------|--------|-------------|--------|
| **usuario-service** | 8001 | Gestión de usuarios y roles | ✅ |
| **medico-service** | 8002 | Registro y gestión de médicos | ✅ |
| **agenda-service** | 8003 | Slots de disponibilidad médica | ✅ |
| **citas-service** | 8004 | Reserva y gestión de citas | ✅ |
| **paciente-service** | 8005 | Datos de pacientes | ✅ |
| **pagos-service** | 8006 | Procesamiento de pagos | ✅ |
| **facturacion-service** | 8007 | Facturación electrónica | ✅ |
| **notificaciones-service** | 8008 | Envío de notificaciones | ✅ |
| **cancelaciones-service** | 8009 | Gestión de cancelaciones | ✅ |
| **dashboard-service** | 8010 | Dashboards y reportes | ✅ |

### Infraestructura

- **Bases de Datos**: PostgreSQL 15 (10 instancias, puertos 5432-5441)
- **Message Broker**: RabbitMQ 3 (puerto 5672, management 15672)
- **Orquestación**: Docker Compose con 22 contenedores

## 🚀 Iniciando el Proyecto

### Requisitos

- Docker & Docker Compose
- Java 21+
- Maven 3.9.7 (opcional, Docker maneja la compilación)

### Ejecución

```bash
# Clonar el repositorio
git clone [repository-url]
cd microservicios-clinica

# Iniciar todos los servicios
docker compose up -d

# Verificar estado
docker compose ps

# Ver logs de un servicio específico
docker compose logs -f pagos-service
```

### Detener los servicios

```bash
docker compose down
docker compose down -v  # Incluye volúmenes de datos
```

## 📚 Documentación API (Swagger/OpenAPI 3.0)

Todos los microservicios incluyen documentación interactiva con Swagger UI:

### Links Swagger por Servicio

| Servicio | URL Swagger | OpenAPI JSON |
|----------|------------|--------------|
| usuario-service | http://localhost:8001/swagger-ui.html | http://localhost:8001/v3/api-docs |
| medico-service | http://localhost:8002/swagger-ui.html | http://localhost:8002/v3/api-docs |
| agenda-service | http://localhost:8003/swagger-ui.html | http://localhost:8003/v3/api-docs |
| citas-service | http://localhost:8004/swagger-ui.html | http://localhost:8004/v3/api-docs |
| paciente-service | http://localhost:8005/swagger-ui.html | http://localhost:8005/v3/api-docs |
| **pagos-service** | http://localhost:8006/doc/swagger-ui.html | http://localhost:8006/v3/api-docs |
| facturacion-service | http://localhost:8007/swagger-ui.html | http://localhost:8007/v3/api-docs |
| notificaciones-service | http://localhost:8008/swagger-ui.html | http://localhost:8008/v3/api-docs |
| cancelaciones-service | http://localhost:8009/swagger-ui.html | http://localhost:8009/v3/api-docs |
| dashboard-service | http://localhost:8010/swagger-ui.html | http://localhost:8010/v3/api-docs |

**Nota:** El servicio `pagos-service` está completamente documentado y es el modelo de referencia para los demás servicios.

## 🧪 Pruebas Unitarias

Se han implementado tests en **4 capas** para 5 microservicios:

### Estructura de Tests

Cada servicio contiene:
```
src/test/java/
├── model/          # Tests del modelo (DoctorTest.java)
├── service/        # Tests de lógica de negocio (DoctorServiceTest.java)
├── repository/     # Tests de acceso a datos con H2 (DoctorRepositoryTest.java)
└── controller/     # Tests de endpoints REST con MockMvc (DoctorControllerTest.java)
```

### Servicios con Tests Completos

1. **medico-service** - 4 test files, 20+ tests
2. **usuario-service** - 4 test files, 15+ tests
3. **agenda-service** - 4 test files, 18+ tests
4. **citas-service** - 4 test files, 18+ tests
5. **pagos-service** - 4 test files, 25+ tests

### Ejecutar Tests

```bash
# Dentro de un servicio
./mvnw test

# Ver reporte de tests
./mvnw test report

# Ejecutar tests de un servicio específico (Docker)
docker exec medico-service mvn test
```

### Cobertura de Tests

| Aspecto | Cobertura |
|---------|-----------|
| Modelo (CRUD) | ✅ Completa |
| Servicio (Lógica) | ✅ Completa |
| Repositorio (JPA) | ✅ Con H2 en memoria |
| Controlador (REST) | ✅ Status 200, 201, 400, 404 |

## 🔐 Configuración de Swagger/OpenAPI

### Dependencia

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### Configuración en application.yml

```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    display-operation-id: false
    operations-sorter: method
```

### Anotaciones en Controllers

```java
@Tag(name = "Médicos", description = "Gestión de médicos")
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    
    @Operation(summary = "Registrar médico", description = "...")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Médico creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDTO create(@RequestBody DoctorRequestDTO dto) { ... }
}
```

## 🗄️ Base de Datos

### Configuración

Cada microservicio tiene:
- Base de datos independiente en PostgreSQL
- Esquema autogenerado con Hibernate (ddl-auto: update)
- Conexión vía jdbc:postgresql://db-{service}:{port}/{database}

### Acceso Directo (Opcional)

```bash
# Conectar a PostgreSQL de usuario-service (puerto 5432)
psql -h localhost -U postgres -d usuariodb

# Conectar a pagos-service (puerto 5437)
psql -h localhost -p 5437 -U postgres -d pagosdb
```

## 📦 Tecnologías

- **Framework**: Spring Boot 3.2.12
- **Lenguaje**: Java 21
- **Build**: Maven 3.9.7
- **BD Principal**: PostgreSQL 15
- **Message Broker**: RabbitMQ 3
- **API Docs**: SpringDoc OpenAPI 2.1.0 / 2.6.0
- **Testing**: JUnit 5, Mockito, Spring Test
- **ORM**: Hibernate 6.4.10
- **Mapping**: MapStruct 1.5.5
- **Utilidades**: Lombok 1.18.32
- **Contenedorización**: Docker, Docker Compose

## 📝 Convenciones de Código

### Nomenclatura

- **Controllers**: `*Controller.java` (ej: DoctorController)
- **Services**: `*Service.java` (ej: DoctorService)
- **Repositories**: `*Repository.java` (ej: DoctorRepository)
- **Models**: `*.java` (ej: Doctor.java)
- **DTOs**: `*RequestDTO.java`, `*ResponseDTO.java`
- **Mappers**: `*Mapper.java` (ej: DoctorMapper)

### Anotaciones Obligatorias

- `@RestController` en controllers
- `@Service` en servicios
- `@Repository` en repositorios
- `@Entity` en modelos JPA
- `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` (Lombok)

## 🔗 Comunicación Entre Microservicios

- **Síncrona**: REST con OpenFeign (`@FeignClient`)
- **Asíncrona**: RabbitMQ con Spring AMQP

## 📊 Próximos Pasos (Semana 16)

- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Autenticación centralizada (OAuth2/JWT)
- [ ] Circuit Breaker (Resilience4j)
- [ ] Trazabilidad distribuida (Spring Cloud Sleuth)
- [ ] Métricas (Micrometer)

## 🤝 Contribución

Para agregar nuevas funcionalidades:

1. Crear rama: `git checkout -b feature/nombre-feature`
2. Hacer commits: `git commit -am 'Agrega feature'`
3. Push: `git push origin feature/nombre-feature`
4. Pull Request

## 📄 Licencia

Apache 2.0

## 👥 Soporte

Para consultas o problemas:
- Revisar logs: `docker compose logs -f [service-name]`
- Verificar estado: `docker compose ps`
- Reiniciar servicio: `docker compose restart [service-name]`

---

**Última actualización**: Junio 2026  
**Semana 15** - Tests (4 capas) + Swagger/OpenAPI completados ✅
