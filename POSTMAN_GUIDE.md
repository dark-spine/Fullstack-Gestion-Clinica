# Postman Collection para Testing

Esta colección contiene ejemplos de todos los endpoints disponibles en el sistema.

## Importar Colección en Postman

1. Abrir Postman
2. Menú: **File → Import**
3. Seleccionar archivo `postman-collection.json`
4. Click en **Import**

## Variables de Entorno

Se recomienda configurar las siguientes variables en Postman:

```
{
  "base_url": "http://localhost:8080",
  "citas_url": "http://localhost:8084",
  "pacientes_url": "http://localhost:8086",
  "medicos_url": "http://localhost:8087",
  "usuarios_url": "http://localhost:8088"
}
```

## Endpoints por Servicio

### 1. Citas Service (Puerto 8084)

#### Listar todas las citas
```
GET {{citas_url}}/api/citas
```

#### Crear nueva cita
```
POST {{citas_url}}/api/citas
Content-Type: application/json

{
  "pacienteId": 1,
  "medicoId": 1,
  "slotAgendaId": 1,
  "motivoConsulta": "Consulta general"
}
```

#### Obtener cita por ID
```
GET {{citas_url}}/api/citas/1
```

#### Actualizar cita
```
PUT {{citas_url}}/api/citas/1
Content-Type: application/json

{
  "pacienteId": 1,
  "medicoId": 1,
  "motivoConsulta": "Seguimiento"
}
```

#### Eliminar cita
```
DELETE {{citas_url}}/api/citas/1
```

### 2. Pacientes Service (Puerto 8086)

#### Listar pacientes
```
GET {{pacientes_url}}/api/pacientes
```

#### Crear paciente
```
POST {{pacientes_url}}/api/pacientes
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "telefono": "123456789",
  "rut": "12.345.678-9"
}
```

### 3. Médicos Service (Puerto 8087)

#### Listar médicos
```
GET {{medicos_url}}/api/medicos
```

#### Crear médico
```
POST {{medicos_url}}/api/medicos
Content-Type: application/json

{
  "nombre": "Dr. Carlos",
  "apellido": "García",
  "especialidad": "Cardiología",
  "email": "carlos@example.com",
  "telefono": "987654321",
  "matricula": "MED12345"
}
```

### 4. Usuarios Service (Puerto 8088)

#### Listar usuarios
```
GET {{usuarios_url}}/api/usuarios
```

#### Crear usuario
```
POST {{usuarios_url}}/api/usuarios
Content-Type: application/json

{
  "nombre": "Admin",
  "email": "admin@example.com",
  "username": "admin",
  "password": "password123",
  "rol": "ADMIN"
}
```

## Scripts Pre-request

### Verificar disponibilidad de servicio
```javascript
pm.test("Servicio disponible", function () {
    pm.response.to.have.status([200, 201, 400]);
});
```

### Validar estructura de respuesta
```javascript
pm.test("Estructura de respuesta válida", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
});
```

## Pruebas Automáticas

### Test: Crear y listar citas

```javascript
// 1. Crear cita
POST {{citas_url}}/api/citas

// 2. Verificar respuesta
pm.test("Cita creada correctamente", function () {
    var jsonData = pm.response.json();
    pm.expect(pm.response.code).to.equal(201);
    pm.expect(jsonData).to.have.property('id');
    
    // Guardar ID para siguiente request
    pm.globals.set("citaId", jsonData.id);
});

// 3. Obtener cita creada
GET {{citas_url}}/api/citas/{{citaId}}

// 4. Verificar que es la misma
pm.test("Cita recuperada correctamente", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.equal(parseInt(pm.globals.get("citaId")));
});
```

## Códigos de Respuesta Esperados

| Método | Endpoint | Código | Descripción |
|--------|----------|--------|-------------|
| GET | /api/citas | 200 | Listar exitoso |
| POST | /api/citas | 201 | Crear exitoso |
| GET | /api/citas/{id} | 200 | Obtener exitoso |
| PUT | /api/citas/{id} | 200 | Actualizar exitoso |
| DELETE | /api/citas/{id} | 204 | Eliminar exitoso |
| GET | /api/citas/999 | 404 | No encontrado |
| POST | /api/citas (datos inválidos) | 400 | Solicitud inválida |

## Flujo de Negocio Completo

### 1. Crear Paciente
```
POST /api/pacientes
Response: { "id": 1, "nombre": "Juan", ... }
```

### 2. Crear Médico
```
POST /api/medicos
Response: { "id": 1, "nombre": "Dr. Carlos", ... }
```

### 3. Crear Agenda/Slot
```
POST /api/agenda/slots
Response: { "id": 1, "medicoId": 1, "fecha": "2026-07-10", ... }
```

### 4. Crear Cita
```
POST /api/citas
{
  "pacienteId": 1,
  "medicoId": 1,
  "slotAgendaId": 1,
  "motivoConsulta": "Consulta"
}
Response: { "id": 1, "estado": "CONFIRMADA", ... }
```

### 5. Cancela Cita
```
POST /api/cancelaciones
{
  "citaId": 1,
  "motivo": "Emergencia"
}
Response: { "id": 1, "estado": "CANCELADA", ... }
```

## Consejos de Testing

1. **Usar variables de entorno** para reutilizar valores
2. **Ejecutar scripts en orden** para mantener consistencia
3. **Validar siempre la respuesta** con tests
4. **Revisar logs del servidor** en caso de error
5. **Usar diferentes verbos HTTP** para probar CRUD completo

## Troubleshooting

### Error: "Connection refused"
- Verificar que Docker Compose está ejecutándose
- Comprobar puertos abiertos

### Error: 400 Bad Request
- Validar formato JSON
- Revisar tipos de datos requeridos

### Error: 404 Not Found
- Verificar ID del recurso
- Comprobar que el servicio está registrado en Eureka

## Referencias

- [Postman Documentation](https://learning.postman.com/)
- [API Testing Best Practices](https://swagger.io/)
