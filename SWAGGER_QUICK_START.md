# 🎯 Guía Rápida - Acceso a Swagger UI

## Acceso Inmediato

### 1. Opción Web (Navegador)
```
http://localhost:8006/doc/swagger-ui.html
```

**Haz clic en el enlace anterior o copia la URL en tu navegador**

### 2. Especificación JSON (para herramientas)
```
http://localhost:8006/v3/api-docs
```

---

## 🧪 Probar los Endpoints

### Desde Swagger UI:

1. Accede a: `http://localhost:8006/doc/swagger-ui.html`
2. Verás la sección **"Pagos"** con todos los endpoints
3. Haz clic en cualquier endpoint (ej: GET /api/payments)
4. Haz clic en **"Try it out"**
5. Haz clic en **"Execute"**

### Resultado esperado:
- **Status**: 200 OK
- **Response**: Lista de pagos en JSON

---

## 📝 Ejemplos de Peticiones desde CLI

### Listar todos los pagos
```bash
curl http://localhost:8006/api/payments
```

### Crear un nuevo pago
```bash
curl -X POST http://localhost:8006/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "appointmentId": 1,
    "amount": 150.50,
    "type": "CREDIT_CARD"
  }'
```

### Obtener el total de ingresos
```bash
curl http://localhost:8006/api/payments/revenue
```

### Obtener contador de pagos
```bash
curl http://localhost:8006/api/payments/count
```

### Reembolsar un pago
```bash
curl -X POST http://localhost:8006/api/payments/{id}/refund
```
(Reemplaza `{id}` con el ID del pago)

### Obtener resumen de una cita
```bash
curl "http://localhost:8006/api/payments/summary?appointmentId=1"
```

---

## 📦 Archivos Modificados

```
✅ pagos-service/pom.xml
   └─ Dependencia springdoc-openapi-starter-webmvc-ui:2.1.0

✅ pagos-service/src/main/resources/application.yml
   └─ Configuración de Swagger UI

✅ pagos-service/src/main/java/com/clinica/pagos/config/OpenApiConfig.java
   └─ Información de la API

✅ pagos-service/src/main/java/com/clinica/pagos/controller/PaymentController.java
   └─ Anotaciones @Tag, @Operation, @ApiResponse, @Parameter

✅ pagos-service/src/main/java/com/clinica/pagos/dto/PaymentRequestDTO.java
   └─ Anotaciones @Schema con ejemplos

✅ pagos-service/src/main/java/com/clinica/pagos/dto/PaymentResponseDTO.java
   └─ Anotaciones @Schema con ejemplos
```

---

## 🔗 Información en Swagger UI

Una vez accedas a `http://localhost:8006/doc/swagger-ui.html` verás:

### Cabecera (Header)
- **Título**: API Pagos Service
- **Versión**: 1.0.0
- **Descripción**: Gestión de pagos y procesamiento de transacciones financieras...
- **Contacto**: pagos@clinica.com
- **Licencia**: Apache 2.0

### Servidores (Servers)
- `http://localhost:8006` - Servidor de Desarrollo
- `http://api.clinica.local:8006` - Servidor de Staging

### Endpoints (bajo la sección "Pagos")

#### 🟢 GET /api/payments
- Listar todos los pagos
- Parámetros: ninguno
- Respuesta: 200 - Lista de PaymentResponseDTO

#### 🔵 POST /api/payments
- Procesar un nuevo pago
- Body: PaymentRequestDTO (con campos documentados)
- Respuestas: 201, 400, 500

#### 🔵 POST /api/payments/{id}/refund
- Reembolsar un pago
- Parámetro: id (Path)
- Respuestas: 200, 404, 400

#### 🟢 GET /api/payments/count
- Contar total de pagos
- Parámetros: ninguno
- Respuesta: 200 - Número entero

#### 🟢 GET /api/payments/revenue
- Calcular ingresos totales
- Parámetros: ninguno
- Respuesta: 200 - Número decimal

#### 🟢 GET /api/payments/summary
- Obtener resumen por cita
- Parámetro: appointmentId (Query)
- Respuestas: 200, 404

---

## ⚠️ Importante

El contenedor `pagos-service` debe estar corriendo:
```bash
docker ps | grep pagos-service
```

Si no está corriendo:
```bash
docker compose up -d --build
```

---

## 🎓 Conceptos Implementados

✅ **OpenAPI 3.0** - Estándar internacional  
✅ **Swagger UI** - Interfaz visual interactiva  
✅ **@Tag** - Agrupación de endpoints  
✅ **@Operation** - Descripción de operaciones  
✅ **@ApiResponse** - Códigos HTTP documentados  
✅ **@Parameter** - Parámetros documentados  
✅ **@Schema** - Modelos de datos documentados  
✅ **Ejemplos reales** - En cada campo de DTO  

---

## 🚀 Siguiente Paso (Opcional)

Para aplicar esto en otros microservicios, seguir los mismos pasos:
1. Actualizar `application.yml`
2. Mejorar `OpenApiConfig` o crear una similar
3. Agregar anotaciones al controlador
4. Documentar los DTOs

¡Ahora tu API está profesionalmente documentada! 📚
