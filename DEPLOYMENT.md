# Guía de Operaciones - CI/CD y Despliegue

## 🚀 Despliegue Local

### Prerequisitos

- Docker 20.10+
- Docker Compose 2.0+
- Java 21 (opcional para desarrollo)
- Maven 3.9+ (opcional para compilación)

### Despliegue con Docker Compose

```bash
cd entorno-desarrollo
docker-compose up --build
```

**Servicios disponibles después del despliegue**:

| Servicio | URL |
|----------|-----|
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Citas Swagger | http://localhost:8084/swagger-ui.html |
| Cancelaciones Swagger | http://localhost:8085/swagger-ui.html |
| Paciente Swagger | http://localhost:8086/swagger-ui.html |
| Médico Swagger | http://localhost:8087/swagger-ui.html |
| Usuario Swagger | http://localhost:8088/swagger-ui.html |

### Verificar Estado de Servicios

```bash
# Ver todos los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de servicio específico
docker-compose logs -f citas-service

# Ejecutar comando en contenedor
docker-compose exec citas-service /bin/sh
```

### Detener y Limpiar

```bash
# Detener servicios
docker-compose stop

# Detener y eliminar contenedores
docker-compose down

# Limpiar volúmenes de datos
docker-compose down -v
```

---

## 🔧 Despliegue en Desarrollo Local (Maven)

### Requisitos

- Java 21 JDK
- Maven 3.9+

### Procedimiento

```bash
# 1. Compilar todos los servicios
mvn clean package

# 2. Iniciar Eureka Server
cd eureka-server
mvn spring-boot:run

# 3. (En otra terminal) Iniciar API Gateway
cd api-gateway
mvn spring-boot:run

# 4. (En terminales adicionales) Iniciar microservicios
cd citas-service
mvn spring-boot:run

# Repetir para otros servicios...
```

---

## 📊 Monitoreo

### Health Checks

```bash
# API Gateway
curl http://localhost:8080/actuator/health

# Eureka Status
curl http://localhost:8761/eureka/status

# Servicio individual
curl http://localhost:8084/actuator/health
```

Respuesta esperada:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "discoveryComposite": {
      "status": "UP"
    }
  }
}
```

### Verificar Servicios Registrados

```bash
curl http://localhost:8761/eureka/apps
```

### Ver Logs

```bash
# Todos los servicios
docker-compose logs

# Con filtro por servicio
docker-compose logs citas-service

# Últimas líneas
docker-compose logs --tail 50

# En tiempo real
docker-compose logs -f
```

---

## 🧪 Pruebas

### Ejecución Local

```bash
# Todas las pruebas
mvn clean test

# Tests específicos
mvn test -Dtest=CitaControllerTest

# Con cobertura
mvn clean test jacoco:report
```

### Pruebas en Docker

```bash
# Ejecutar pruebas dentro del contenedor
docker-compose exec citas-service mvn test

# Con reporte de cobertura
docker-compose exec citas-service mvn clean test jacoco:report
```

### Resultados

```
target/surefire-reports/      # Resultados XML
target/site/jacoco/           # Reporte de cobertura
```

---

## 🔄 CI/CD Pipeline (Recomendado)

### GitHub Actions Configuration

Crear archivo `.github/workflows/ci-cd.yml`:

```yaml
name: CI/CD Pipeline

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
        options: >-
          --health-cmd="mysqladmin ping"
          --health-interval=10s
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: mvn clean package
      
      - name: Run Tests
        run: mvn test
      
      - name: Generate Coverage
        run: mvn jacoco:report
      
      - name: Upload Coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./target/site/jacoco/jacoco.xml
```

---

## 📈 Escalado

### Escalar Servicio Específico

```yaml
# docker-compose.yml
citas-service:
  deploy:
    replicas: 3  # 3 instancias
```

```bash
# Rebuild y reiniciar
docker-compose up --scale citas-service=3
```

### Load Balancing

API Gateway distribuye automáticamente:
```
Request 1 → citas-service:1
Request 2 → citas-service:2
Request 3 → citas-service:3
Request 4 → citas-service:1 (round robin)
```

---

## 🔐 Seguridad

### Variables de Entorno Sensibles

```bash
# Crear .env file
DB_PASSWORD=dev123
EUREKA_PASSWORD=secret
JWT_SECRET=supersecretkey

# Usar en docker-compose
environment:
  - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
```

### Puertos y Firewall

| Puerto | Servicio | Acceso |
|--------|----------|--------|
| 8080 | API Gateway | Público |
| 8761 | Eureka | Interno |
| 8084-8088 | Servicios | Interno |
| 3306 | MySQL | Interno |

### HTTPS en Producción

```yaml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_PASSWORD}
    key-store-type: PKCS12
```

---

## 📋 Checklist de Despliegue

- [ ] Todos los servicios están compilando sin errores
- [ ] Todas las pruebas pasan (cobertura ≥80%)
- [ ] Docker Compose levanta correctamente
- [ ] API Gateway accesible en localhost:8080
- [ ] Eureka Dashboard operativo en localhost:8761
- [ ] 5 microservicios registrados en Eureka
- [ ] Swagger accesible en cada servicio
- [ ] Base de datos inicializado (DDL auto-create)
- [ ] Logs sin errores de configuración
- [ ] Health checks respondiendo correctamente

---

## 🆘 Troubleshooting

### Error: "Connection refused"

```bash
# Verificar contenedores en ejecución
docker-compose ps

# Reiniciar docker daemon
sudo systemctl restart docker
```

### Error: "Port already in use"

```bash
# Encontrar proceso en puerto
lsof -i :8080

# O eliminar contenedores previos
docker-compose down
```

### Error: "Database connection timeout"

```bash
# Esperar a que MySQL inicie
docker-compose logs db-citas

# Reiniciar con health checks
docker-compose restart db-citas
```

### Error: "Service not registered in Eureka"

```bash
# Verificar configuración Eureka
curl http://localhost:8761/eureka/apps

# Revisar logs del servicio
docker-compose logs citas-service | grep Eureka
```

### Error: "504 Bad Gateway"

```bash
# El servicio no está disponible
# Verificar si está registrado
curl http://localhost:8761/eureka/apps/citas-service

# Ver estado en API Gateway logs
docker-compose logs api-gateway
```

---

## 📊 Performance Tuning

### JVM Heap Memory

```yaml
environment:
  - _JAVA_OPTIONS=-Xms512m -Xmx1024m
```

### Connection Pool

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

### Logging Level

```yaml
logging:
  level:
    root: WARN
    com.clinica: DEBUG
```

---

## 📚 Referencias

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Deployment](https://spring.io/guides/gs/spring-boot/)
- [Microservices Best Practices](https://microservices.io/)
