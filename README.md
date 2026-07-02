# Sistema de Gestión de Clínicas - Arquitectura de Microservicios

## 📋 Descripción del Dominio

Sistema integral para la gestión de clínicas médicas, basado en una arquitectura de microservicios. El sistema permite gestionar:
- **Pacientes**: Registro, actualización y gestión de historiales de pacientes
- **Médicos**: Administración de profesionales médicos, especialidades y calendarios
- **Citas Médicas**: Programación, cancelación y seguimiento de citas
- **Agenda**: Gestión de slots disponibles de médicos
- **Usuarios**: Autenticación y administración de usuarios del sistema
- **Pagos**: Procesamiento y seguimiento de pagos
- **Facturación**: Emisión y gestión de facturas
- **Notificaciones**: Alertas y recordatorios automáticos
- **Dashboard**: Visualización de métricas e indicadores clave
- **Cancelaciones**: Registro y control de cancelaciones de citas

## 👥 Equipo de Desarrollo

- Desarrolladores del proyecto EFT 2026
- Arquitectos de Microservicios
- Ingenieros de Calidad

## 🏗️ Arquitectura

### Patrón de Diseño: CSR (Controller-Service-Repository)

Cada microservicio implementa la separación clara de responsabilidades:
- **Controllers**: Manejo de solicitudes HTTP REST
- **Services**: Lógica de negocio y orquestación
- **Repositories**: Acceso a datos con JPA/Hibernate
- **Models**: Entidades JPA

### Componentes Principales

#### 1. **API Gateway** (Puerto: 8080)
- Punto de entrada único para todas las solicitudes
- Enrutamiento inteligente de tráfico
- Spring Cloud Gateway
- Integración con Eureka para descubrimiento dinámico

#### 2. **Eureka Server** (Puerto: 8761)
- Servidor de descubrimiento y registro de servicios
- Permite comunicación entre microservicios sin URLs hardcodeadas
- Auto-escalado y tolerancia a fallos

## 📦 Microservicios

### Servicios Implementados

| Servicio | Puerto | Base de Datos | Swagger |
|----------|--------|---------------|---------|
| **API Gateway** | 8080 | - | - |
| **Eureka Server** | 8761 | - | - |
| **Citas Service** | 8084 | citasdb | ✅ |
| **Cancelaciones Service** | 8085 | cancelacionesdb | ✅ |
| **Paciente Service** | 8086 | pacientesdb | ✅ |
| **Médico Service** | 8087 | medicosdb | ✅ |
| **Usuario Service** | 8088 | usuariosdb | ✅ |
| **Agenda Service** | 8089 | agendadb | - |
| **Dashboard Service** | 8090 | dashboarddb | - |
| **Facturación Service** | 8091 | facturaciondb | - |
| **Notificaciones Service** | 8092 | notificacionesdb | - |
| **Pagos Service** | 8093 | pagosdb | - |

## 🔌 Rutas Principales del API Gateway

```
GET  /api/citas              → Listar citas
POST /api/citas              → Crear cita
GET  /api/citas/{id}         → Obtener cita
PUT  /api/citas/{id}         → Actualizar cita
DELETE /api/citas/{id}       → Eliminar cita

GET  /api/cancelaciones      → Listar cancelaciones
POST /api/cancelaciones      → Crear cancelación

GET  /api/pacientes          → Listar pacientes
POST /api/pacientes          → Crear paciente
GET  /api/pacientes/{id}     → Obtener paciente

GET  /api/medicos            → Listar médicos
POST /api/medicos            → Crear médico
GET  /api/medicos/{id}       → Obtener médico

GET  /api/usuarios           → Listar usuarios
POST /api/usuarios           → Crear usuario
GET  /api/usuarios/{id}      → Obtener usuario
```

## 📚 Documentación Swagger/OpenAPI

La documentación interactiva Swagger está disponible en cada servicio:

### URLs de Swagger

| Servicio | URL |
|----------|-----|
| **Citas Service** | http://localhost:8084/swagger-ui.html |
| **Cancelaciones Service** | http://localhost:8085/swagger-ui.html |
| **Paciente Service** | http://localhost:8086/swagger-ui.html |
| **Médico Service** | http://localhost:8087/swagger-ui.html |
| **Usuario Service** | http://localhost:8088/swagger-ui.html |

### Acceder a Swagger vía API Gateway

```
http://localhost:8080/citas-service/v3/api-docs
http://localhost:8080/cancelaciones-service/v3/api-docs
http://localhost:8080/paciente-service/v3/api-docs
http://localhost:8080/medico-service/v3/api-docs
http://localhost:8080/usuario-service/v3/api-docs
```

## ✅ Pruebas Unitarias

### Cobertura Implementada

Cada servicio incluye pruebas unitarias cubriendo las 4 capas:

#### 1. **Capa de Modelo** (`Model Tests`)
- Validación de entidades JPA
- Pruebas de getters/setters
- Tests de relaciones entre modelos
- ✅ Cobertura: >80%

#### 2. **Capa de Repositorio** (`Repository Tests`)
- Tests de operaciones CRUD
- Pruebas de consultas personalizadas
- Validación de persistencia en BD
- ✅ Cobertura: >80%

#### 3. **Capa de Servicio** (`Service Tests`)
- Tests con Mockito de dependencias
- Validación de lógica de negocio
- Tests de transformación DTOs
- Manejo de excepciones
- ✅ Cobertura: >80%

#### 4. **Capa de Controlador** (`Controller Tests`)
- Tests con MockMvc
- Validación de endpoints REST
- Tests de códigos HTTP
- Validación de request/response
- ✅ Cobertura: >80%

### Ejecutar Pruebas

```bash
# Todas las pruebas
mvn test

# Pruebas de un servicio específico
cd citas-service
mvn test

# Con reporte de cobertura
mvn clean test jacoco:report
```

### Servicios con Pruebas Unitarias

- ✅ **Citas Service** - 25+ tests
- ✅ **Cancelaciones Service** - 8+ tests
- ✅ **Paciente Service** - 8+ tests
- ✅ **Médico Service** - 9+ tests
- ✅ **Usuario Service** - 8+ tests

## 🚀 Instrucciones de Ejecución

### Requisitos Previos

- **Java 21** o superior
- **Maven 3.9+**
- **Docker** y **Docker Compose**
- **Git**

### Opción 1: Ejecución con Docker Compose (Recomendado)

```bash
# Clonar repositorio
git clone <repository-url>
cd mi-proyecto-fullstack/entorno-desarrollo

# Construir y ejecutar todos los servicios
docker-compose up --build

# Los servicios estarán disponibles en:
# - API Gateway: http://localhost:8080
# - Eureka: http://localhost:8761
# - Citas Service: http://localhost:8084
# - Cancelaciones Service: http://localhost:8085
# - Paciente Service: http://localhost:8086
# - Médico Service: http://localhost:8087
# - Usuario Service: http://localhost:8088
```

### Opción 2: Ejecución Local (Desarrollo)

```bash
# Terminal 1: Iniciar Eureka Server
cd eureka-server
mvn spring-boot:run

# Terminal 2: Iniciar API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 3-7: Iniciar los microservicios
cd citas-service
mvn spring-boot:run

# (Repetir para otros servicios en terminales separadas)
cd cancelaciones-service
mvn spring-boot:run

cd paciente-service
mvn spring-boot:run

cd medico-service
mvn spring-boot:run

cd usuario-service
mvn spring-boot:run
```

### Opción 3: Compilación Manual

```bash
# Compilar cada servicio
mvn clean package

# Los JARs se generarán en target/
# Ejecutar manualmente:
java -jar citas-service/target/citas-service-1.0.0.jar
```

## 📊 Verificación de Servicios

### Endpoint de Salud

```bash
# Comprobar que el API Gateway está operativo
curl http://localhost:8080/actuator/health

# Comprobar Eureka
curl http://localhost:8761/eureka/status

# Comprobar cada servicio
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health
curl http://localhost:8087/actuator/health
curl http://localhost:8088/actuator/health
```

## 📌 Comunicación entre Microservicios

### Patrón de Comunicación: REST con Feign Client

Los servicios se comunican entre sí usando **Spring Cloud OpenFeign**, registrando sus llamadas a través de Eureka.

#### Ejemplo: Citas-Service llamando a Paciente-Service

```java
@FeignClient(name = "paciente-service")
public interface PacienteClient {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO obtenerPaciente(@PathVariable Long id);
}
```

#### Ejemplo de Uso en Servicio

```java
@Service
public class CitaService {
    private final PacienteClient pacienteClient;
    
    public CitaDTO crearCita(CitaCreateDTO request) {
        // Validar paciente llamando a paciente-service
        PacienteDTO paciente = pacienteClient.obtenerPaciente(
            request.getPacienteId()
        );
        // ... lógica de negocio
    }
}
```

## 🧪 Pruebas con Postman

### Colección de Endpoints

Se incluye una colección de Postman con ejemplos para:
1. Crear cita
2. Listar citas
3. Actualizar cita
4. Cancelar cita
5. CRUD de pacientes
6. CRUD de médicos
7. CRUD de usuarios

### Importar Colección

```
Archivo: postman-collection.json
Importar en Postman → New → File
```

## 🔒 Configuración de Seguridad

### Validación de Datos

- ✅ Bean Validation en DTOs
- ✅ Validación en controladores
- ✅ Manejo de excepciones global

### Manejo de Errores

Cada servicio implementa:
- `GlobalExceptionHandler` para manejo centralizado
- Códigos HTTP semánticos
- Respuestas de error estructuradas
- Logs estructurados para trazabilidad

## 📝 Estructura de Base de Datos

### Esquema normalizado

Cada servicio tiene su base de datos independiente:

#### citasdb
- Tabla: `citas` (id, paciente_id, medico_id, slot_agenda_id, motivo_consulta, estado)

#### pacientesdb
- Tabla: `pacientes` (id, nombre, apellido, email, telefono, rut)

#### medicosdb
- Tabla: `medicos` (id, nombre, apellido, especialidad, email, telefono, matricula)

#### usuariosdb
- Tabla: `usuarios` (id, nombre, email, username, password, rol, activo)

#### cancelacionesdb
- Tabla: `cancelaciones` (id, cita_id, motivo, estado, fecha_cancelacion)

## 🛠️ Herramientas y Tecnologías

| Componente | Versión |
|-----------|---------|
| Java | 21 LTS |
| Spring Boot | 3.2.5 |
| Spring Cloud | 2023.0.1 |
| MySQL | 8.0 |
| Docker | Latest |
| Maven | 3.9+ |
| JUnit 5 | Latest |
| Mockito | Latest |
| Swagger/OpenAPI | 2.1.0 |

## 📋 Checklist de Requisitos

- ✅ Mínimo 10 microservicios funcionales (11 implementados)
- ✅ Swagger activo en 5 microservicios
- ✅ Pruebas unitarias en 5 microservicios
- ✅ Cobertura ≥ 80% (4 capas: Modelo, Servicio, Controlador, Repositorio)
- ✅ API Gateway configurado
- ✅ Eureka como servidor de descubrimiento
- ✅ 5 microservicios registrados en Eureka
- ✅ Comunicación entre microservicios vía Gateway
- ✅ YAML correctamente configurado
- ✅ CRUD completo con JPA + Hibernate
- ✅ Commits descriptivos y distribuidos

## 🔍 Monitoreo y Debugging

### Ver logs en tiempo real

```bash
# Con Docker Compose
docker-compose logs -f <service-name>

# Ejemplo
docker-compose logs -f citas-service
```

### Acceder a Eureka Dashboard

```
http://localhost:8761/
```

Aquí puedes ver:
- Servicios registrados
- Instancias activas
- Estado de cada instancia
- Información de disponibilidad

## 📞 Soporte y Documentación

- **API Docs**: Swagger UI en cada puerto
- **Diagrama de arquitectura**: Ver `ARCHITECTURE.md`
- **Guía de desarrollo**: Ver `DEVELOPMENT.md`

## 📅 Información de Entrega

- **Fecha de Examen**: 09 de Julio de 2026
- **Duración**: 15 minutos por equipo
- **Formato**: Presentación grupal + Defensa individual
- **Ponderación Encargo**: 40%
- **Ponderación Defensa**: 60%

## 📜 Licencia

Proyecto académico - Sistema de Gestión de Clínicas 2026
