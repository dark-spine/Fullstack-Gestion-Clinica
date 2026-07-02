# 🚀 QUICK START - Guía de Inicio Rápido

## ⏱️ 5 Minutos para Levantar Todo

### Opción 1: Docker Compose (RECOMENDADO)

```bash
# 1. Entrar a la carpeta
cd entorno-desarrollo

# 2. Levantar todo
docker-compose up --build

# 3. Esperar ~2-3 minutos mientras se descargan imágenes y se inician servicios

# 4. ¡Listo! Acceder a:
# - API Gateway: http://localhost:8080
# - Eureka Dashboard: http://localhost:8761
# - Swagger Citas: http://localhost:8084/swagger-ui.html
```

### Opción 2: Maven Local

```bash
# Terminal 1: Eureka
cd eureka-server && mvn spring-boot:run

# Terminal 2: API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 3+: Servicios
cd citas-service && mvn spring-boot:run
cd cancelaciones-service && mvn spring-boot:run
# ... etc
```

---

## 📍 Acceso Rápido a URLs

| Servicio | URL |
|----------|-----|
| **API Gateway** | http://localhost:8080 |
| **Eureka Dashboard** | http://localhost:8761 |
| **Citas Swagger** | http://localhost:8084/swagger-ui.html |
| **Cancelaciones Swagger** | http://localhost:8085/swagger-ui.html |
| **Paciente Swagger** | http://localhost:8086/swagger-ui.html |
| **Médico Swagger** | http://localhost:8087/swagger-ui.html |
| **Usuario Swagger** | http://localhost:8088/swagger-ui.html |

---

## 🧪 Ejecutar Pruebas (30 segundos)

```bash
# Todas las pruebas
mvn clean test

# Resultado esperado: 58+ tests pasen ✅
```

---

## 📝 Probar Endpoints con cURL

### Crear Cita
```bash
curl -X POST http://localhost:8080/api/citas \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteId": 1,
    "medicoId": 1,
    "slotAgendaId": 1,
    "motivoConsulta": "Consulta general"
  }'
```

### Listar Citas
```bash
curl http://localhost:8080/api/citas
```

### Crear Paciente
```bash
curl -X POST http://localhost:8080/api/pacientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@example.com",
    "telefono": "123456789",
    "rut": "12.345.678-9"
  }'
```

---

## 🔍 Verificar Estado

```bash
# ¿Está todo funcionando?
curl http://localhost:8761/eureka/apps

# Respuesta esperada: JSON con servicios registrados
```

---

## 🆘 Problemas Comunes

### ❌ "Connection refused"
```bash
# Verificar que Docker está corriendo
docker-compose ps

# Reiniciar
docker-compose restart
```

### ❌ "Port already in use"
```bash
# Eliminar contenedores previos
docker-compose down

# Reintentar
docker-compose up --build
```

### ❌ "Service not registered"
```bash
# Esperar 10-15 segundos
# Los servicios necesitan tiempo para registrarse en Eureka

# Comprobar estado
curl http://localhost:8761/eureka/apps/citas-service
```

---

## 📊 Status Esperado

Cuando todo está corriendo correctamente:

```
✅ Eureka Server: http://localhost:8761
   - Status: UP

✅ API Gateway: http://localhost:8080
   - Status: UP
   - Routes: 5 configured

✅ Citas Service: 
   - Registered in Eureka: YES
   - Database: citasdb (UP)
   - Swagger: http://localhost:8084/swagger-ui.html

✅ [Similar para otros 4 servicios]
```

---

## 📚 Documentación Completa

| Documento | Para | Lectura |
|-----------|------|---------|
| **README.md** | Descripción general | 10 min |
| **ARCHITECTURE.md** | Entender el diseño | 15 min |
| **TESTING.md** | Tests unitarios | 10 min |
| **DEPLOYMENT.md** | Despliegue | 15 min |
| **POSTMAN_GUIDE.md** | Testing manual | 10 min |
| **CHECKLIST.md** | Requisitos cumplidos | 5 min |

---

## ✅ Pre-Examen Checklist

- [ ] Docker Compose levanta sin errores
- [ ] Todos los servicios se registran en Eureka
- [ ] API Gateway accesible
- [ ] Swagger cargable en 5 servicios
- [ ] Pruebas unitarias pasan (58+)
- [ ] Endpoints responden correctamente
- [ ] Logs sin errores críticos
- [ ] Documentación completa

---

## 🎯 Próximos Pasos

1. **Levantar proyecto** → `docker-compose up --build`
2. **Verificar estado** → http://localhost:8761
3. **Probar endpoints** → Swagger o cURL
4. **Ejecutar pruebas** → `mvn test`
5. **Documentación** → Revisar README.md

---

## 📞 Soporte

- 🐛 Bugs: Ver DEPLOYMENT.md sección Troubleshooting
- 📖 Documentación: Revisar archivos MD en raíz
- 🧪 Pruebas: Ver TESTING.md
- 🌐 Endpoints: Ver POSTMAN_GUIDE.md

---

**¡Tu proyecto está listo para el examen! 🚀**
