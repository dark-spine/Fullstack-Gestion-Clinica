# 🎬 GUÍA DE DEMOSTRACIÓN EN VIVO - EFT 2026

## Objetivo
Demostrar en tiempo real el funcionamiento de Bean Validation, Manejo de Excepciones y SLF4J logging durante la defensa individual (09-07-2026).

---

## Parte 1: Preparación Previa (5 minutos antes)

### 1.1 Iniciar los Servicios
```bash
cd /home/ubuntu/mi-proyecto-fullstack/entorno-desarrollo

# Iniciar todos los servicios
docker-compose up --build

# Esperar a que todos estén listos (~2-3 minutos)
# Verificar: "Tomcat started on port XXXX"
```

### 1.2 Verificar Accesibilidad
```bash
# En otra terminal, verificar que Swagger está accesible
curl -s http://localhost:8081/swagger-ui.html | head -20

# Debería retornar HTML de Swagger UI
```

---

## Parte 2: Demostración de Bean Validation (5 minutos)

### Objetivo
Mostrar que las validaciones de entrada funcionan automáticamente.

### 2.1 Crear Usuario VÁLIDO
**Paso 1**: Abrir Postman (o usar curl)

**Paso 2**: Crear POST request a:
```
http://localhost:8081/usuarios
```

**Paso 3**: Body JSON (VÁLIDO):
```json
{
  "username": "carlos.perez",
  "email": "carlos@clinica.com",
  "password": "SecurePass123!",
  "nombre": "Carlos",
  "apellido": "Pérez",
  "telefono": "+56912345678",
  "direccion": "Calle Principal 123"
}
```

**Resultado Esperado**:
```
HTTP 201 Created
{
  "id": 1,
  "username": "carlos.perez",
  "email": "carlos@clinica.com",
  "nombre": "Carlos",
  "apellido": "Pérez",
  "telefono": "+56912345678",
  "direccion": "Calle Principal 123"
}
```

**Explicar**: "El servidor aceptó todos los datos porque cumplen las validaciones."

### 2.2 Crear Usuario INVÁLIDO (campo vacío)
**Body JSON (INVÁLIDO - email vacío)**:
```json
{
  "username": "juan.gomez",
  "email": "",
  "password": "SecurePass123!",
  "nombre": "Juan",
  "apellido": "Gómez",
  "telefono": "+56987654321",
  "direccion": "Avenida Secundaria 456"
}
```

**Resultado Esperado**:
```
HTTP 400 Bad Request
{
  "status": 400,
  "message": "Validation failed",
  "error": "Validation error",
  "timestamp": "2026-07-09T10:15:30.123456",
  "path": "/usuarios",
  "details": [
    {
      "field": "email",
      "message": "must not be blank"
    }
  ]
}
```

**Explicar**: "Bean Validation rechazó el email vacío automáticamente. HTTP 400 es el código correcto para validación de entrada."

### 2.3 Crear Usuario INVÁLIDO (email malformado)
**Body JSON (INVÁLIDO - email sin @)**:
```json
{
  "username": "maria.lopez",
  "email": "maria.clinica.com",
  "password": "SecurePass123!",
  "nombre": "María",
  "apellido": "López",
  "telefono": "+56988776655",
  "direccion": "Boulevard Terciario 789"
}
```

**Resultado Esperado**:
```
HTTP 400 Bad Request
{
  "status": 400,
  "message": "Validation failed",
  "error": "Invalid email format",
  "timestamp": "2026-07-09T10:16:15.123456",
  "path": "/usuarios"
}
```

**Explicar**: "La anotación @Email rechazó el formato incorrecto del email."

---

## Parte 3: Demostración de Excepciones (5 minutos)

### Objetivo
Mostrar que las excepciones personalizadas retornan códigos HTTP semánticos.

### 3.1 Crear Usuario Duplicado (409 Conflict)
**Paso 1**: Intentar crear el usuario "carlos.perez" nuevamente

**Body JSON**:
```json
{
  "username": "carlos.perez",
  "email": "carlos2@clinica.com",
  "password": "DifferentPass123!",
  "nombre": "Carlos 2",
  "apellido": "Pérez 2",
  "telefono": "+56912345679",
  "direccion": "Calle Principal 124"
}
```

**Resultado Esperado**:
```
HTTP 409 Conflict
{
  "status": 409,
  "message": "El username 'carlos.perez' ya existe en el sistema",
  "error": "DuplicateResourceException",
  "timestamp": "2026-07-09T10:17:00.123456",
  "path": "/usuarios"
}
```

**Explicar**: "HTTP 409 Conflict es la respuesta correcta cuando hay duplicados. Nuestra aplicación usa excepciones personalizadas para retornar códigos HTTP semánticos."

### 3.2 Obtener Usuario NO EXISTENTE (404 Not Found)
**Paso 2**: GET request a:
```
GET http://localhost:8081/usuarios/99999
```

**Resultado Esperado**:
```
HTTP 404 Not Found
{
  "status": 404,
  "message": "Usuario no encontrado con id: 99999",
  "error": "ResourceNotFoundException",
  "timestamp": "2026-07-09T10:17:45.123456",
  "path": "/usuarios/99999"
}
```

**Explicar**: "HTTP 404 es el código correcto cuando un recurso no existe. GlobalExceptionHandler captura la excepción y retorna la respuesta adecuada."

### 3.3 Actualizar Usuario NO EXISTENTE
**Paso 3**: PUT request a:
```
PUT http://localhost:8081/usuarios/99999
```

**Body JSON**:
```json
{
  "username": "carlos.updated",
  "email": "carlos.updated@clinica.com",
  "nombre": "Carlos Updated"
}
```

**Resultado Esperado**:
```
HTTP 404 Not Found
{
  "status": 404,
  "message": "Usuario no encontrado con id: 99999",
  "error": "ResourceNotFoundException",
  "timestamp": "2026-07-09T10:18:20.123456",
  "path": "/usuarios/99999"
}
```

**Explicar**: "La actualización de un usuario inexistente también retorna 404, validando que nuestro manejo de excepciones es consistente."

---

## Parte 4: Demostración de SLF4J Logging (5 minutos)

### Objetivo
Mostrar que todas las operaciones quedan registradas con contexto.

### 4.1 Ver Logs en Tiempo Real
**Paso 1**: En una terminal nueva:
```bash
docker logs -f usuario-service
```

**Paso 2**: Volver a Postman y crear otro usuario exitosamente

**Body JSON**:
```json
{
  "username": "diego.sanchez",
  "email": "diego@clinica.com",
  "password": "SecurePass456!",
  "nombre": "Diego",
  "apellido": "Sánchez",
  "telefono": "+56991234567",
  "direccion": "Pasaje Especial 321"
}
```

### 4.2 Logs Esperados
```
2026-07-09 10:19:30,123 [http-nio-8081-exec-1] INFO  com.clinica.usuario.service.UsuarioService
  - Creando usuario con username: diego.sanchez

2026-07-09 10:19:30,124 [http-nio-8081-exec-1] INFO  com.clinica.usuario.service.UsuarioService
  - Usuario creado exitosamente con id: 2, username: diego.sanchez

2026-07-09 10:19:30,125 [http-nio-8081-exec-1] INFO  com.clinica.usuario.handler.GlobalExceptionHandler
  - Usuario creado con éxito: 201
```

**Explicar**: "Cada operación genera logs INFO. Vemos exactamente qué pasó, con qué usuario, en qué momento."

### 4.3 Logs de Duplicado (WARN)
**Paso 3**: Intentar crear "diego.sanchez" nuevamente

**Logs Esperados**:
```
2026-07-09 10:20:15,345 [http-nio-8081-exec-2] INFO  com.clinica.usuario.service.UsuarioService
  - Creando usuario con username: diego.sanchez

2026-07-09 10:20:15,346 [http-nio-8081-exec-2] WARN  com.clinica.usuario.service.UsuarioService
  - Intento de crear usuario con username existente: diego.sanchez

2026-07-09 10:20:15,347 [http-nio-8081-exec-2] WARN  com.clinica.usuario.handler.GlobalExceptionHandler
  - DuplicateResourceException capturada: El username 'diego.sanchez' ya existe en el sistema
```

**Explicar**: "Los conflictos generan logs WARN. Es fácil rastrear intentos de duplicado."

### 4.4 Logs de No Encontrado (ERROR)
**Paso 4**: GET /usuarios/99999

**Logs Esperados**:
```
2026-07-09 10:21:00,567 [http-nio-8081-exec-3] INFO  com.clinica.usuario.service.UsuarioService
  - Obteniendo usuario con id: 99999

2026-07-09 10:21:00,568 [http-nio-8081-exec-3] ERROR com.clinica.usuario.service.UsuarioService
  - Usuario no encontrado con id: 99999

2026-07-09 10:21:00,569 [http-nio-8081-exec-3] ERROR com.clinica.usuario.handler.GlobalExceptionHandler
  - ResourceNotFoundException capturada: Usuario no encontrado con id: 99999
```

**Explicar**: "Los errores generan logs ERROR. En producción, esto facilita mucho el debugging."

---

## Parte 5: Resumen Arquitectónico (2 minutos)

### 5.1 Mostrar Archivos Clave
```bash
# Estructura de excepciones
tree usuario-service/src/main/java/com/clinica/usuario/

# Debería mostrar:
# - exception/
#   - ResourceNotFoundException.java
#   - DuplicateResourceException.java
# - handler/
#   - GlobalExceptionHandler.java
# - service/
#   - UsuarioService.java (con Logger)
```

### 5.2 Explicar Diagrama Mental

```
Solicitud HTTP (Body JSON)
        ↓
    [Bean Validation] → Si inválido → HTTP 400 + ErrorResponseDTO
        ↓ (válido)
    [Controller]
        ↓
    [Service] → Logs INFO
        ↓
    [Negocio] → Puede lanzar excepciones
        ↓ (Error)
    [GlobalExceptionHandler] → Logs WARN/ERROR → HTTP 409/404
        ↓ (Éxito)
    [Response] + Logs INFO
```

**Explicar**: "La arquitectura es limpia: cada capa tiene una responsabilidad clara. Las excepciones son manejadas centralmente, no diseminadas por el código."

---

## Parte 6: Preguntas Potenciales y Respuestas

### P1: ¿Por qué HTTP 409 para duplicados y no 400?
**R**: "HTTP 400 es para validación de formato. HTTP 409 es para conflictos de negocio (duplicados). Es más semántico para clientes API."

### P2: ¿Por qué tres tipos de excepciones?
**R**: "ResourceNotFoundException maneja el patrón 'no existe', DuplicateResourceException maneja duplicados, y la genérica `Exception` captura lo inesperado. Cada una retorna el código HTTP correcto."

### P3: ¿Por qué SLF4J y no System.out.println?
**R**: "SLF4J es un facade que permite cambiar implementaciones de logging sin cambiar código. Además, facilita niveles (INFO/WARN/ERROR) y redirección a archivos."

### P4: ¿Se puede cambiar formato de logs?
**R**: "Sí, modificando logback-spring.xml. Con SLF4J, cambiar de Logback a Log4j es solo cambiar la dependencia."

### P5: ¿Cómo escala esto a 12 microservicios?
**R**: "El patrón es idéntico en usuario, paciente y médico. Otros servicios pueden usarlo como base. Es escalable horizontalmente."

---

## Cronograma de Presentación Sugerido

| Tiempo | Actividad | Notas |
|--------|-----------|-------|
| 0-1 min | Intro + startup servicios | Dejar iniciando mientras hablas |
| 1-6 min | Bean Validation | 3 ejemplos (válido, inválido email, inválido tipo) |
| 6-11 min | Excepciones | 409 Conflict, 404 Not Found |
| 11-16 min | SLF4J Logging | Ver logs en tiempo real |
| 16-18 min | Arquitectura | Mostrar estructura de archivos |
| 18-20 min | Responder preguntas | Estar preparado |

---

## Checklist Pre-Presentación

- [ ] Servicios iniciados y verificados
- [ ] Postman abierto con requests listas
- [ ] Terminal de logs visible (`docker logs -f usuario-service`)
- [ ] Documentación a mano (MEJORAS_IMPLEMENTADAS.md)
- [ ] Respuestas memorizadas para preguntas comunes
- [ ] Backup: si algo falla, tener código fuente visible
- [ ] Conexión a internet estable
- [ ] Proyector probado

---

## Plan B: Si Algo Falla

### Si un servicio no inicia:
```bash
# Verificar logs
docker logs usuario-service

# Reintentar
docker-compose restart usuario-service
```

### Si Postman falla:
```bash
# Usar curl desde terminal
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"test"}'
```

### Si Swagger no carga:
```bash
# Mostrar código fuente en IDE
# Explicar estructura de @RestController y @PostMapping
```

---

## Materiales de Apoyo

### Durante la Defensa
- [x] Laptop con servicios inicializados
- [x] Postman o curl
- [x] Terminal con docker logs
- [x] IDE con código visible
- [x] Documentos: MEJORAS_IMPLEMENTADAS.md, DETALLES_TECNICOS_IMPLEMENTACION.md
- [x] Esta guía impresa o en segunda pantalla

### Después
- [x] Todos los logs capturados
- [x] Respuestas a preguntas documentadas
- [x] Feedback incorporado al código

---

## Notas Finales

> "Lo importante no es que todo sea perfecto, sino que demuestres comprensión de lo que construiste. Si algo falla, explica por qué y cómo lo resolverías. Los evaluadores valoran el debugging y la adaptación."

---

**Generado**: 02-07-2026  
**Objetivo**: Defensa Individual EFT 09-07-2026  
**Duración Total**: ~20 minutos  
**Estado**: Listo para presentación
