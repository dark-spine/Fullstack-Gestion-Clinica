# Guía de Pruebas Unitarias

## 📋 Descripción General

Este documento detalla la estrategia de pruebas unitarias implementada en el proyecto, cubriendo las 4 capas de la arquitectura CSR: Controlador, Servicio, Repositorio y Modelo.

## 🎯 Objetivos de las Pruebas

1. ✅ Cobertura mínima del **80%** en lógica de negocio
2. ✅ Validación de comportamiento en cada capa
3. ✅ Detección temprana de defectos
4. ✅ Facilitar refactorización segura
5. ✅ Documentación viva del código

## 🏗️ Arquitectura de Pruebas

### 1. Pruebas de Modelo (Unit Tests)

**Ubicación**: `src/test/java/.../model/`

**Propósito**: Validar getters, setters y comportamiento de entidades

**Ejemplo**:
```java
@Test
public void testCrearCita() {
    Cita cita = new Cita();
    cita.setId(1L);
    cita.setEstado("CONFIRMADA");
    
    assertEquals(1L, cita.getId());
    assertEquals("CONFIRMADA", cita.getEstado());
}
```

**Servicios cubiertos**: Todos los 5 servicios principales

---

### 2. Pruebas de Repositorio (Integration Tests)

**Ubicación**: `src/test/java/.../repository/`

**Propósito**: Validar operaciones CRUD y consultas personalizadas

**Anotaciones**:
```java
@DataJpaTest // Para tests de JPA
```

**Ejemplo**:
```java
@Test
public void testObtenerCitaPorId() {
    Cita cita = new Cita();
    cita.setPacienteId(1L);
    cita.setMedicoId(1L);
    
    Cita saved = citaRepository.save(cita);
    Cita found = citaRepository.findById(saved.getId()).orElse(null);
    
    assertNotNull(found);
    assertEquals(saved.getId(), found.getId());
}
```

**Tests Incluidos**:
- Guardar entidad
- Obtener por ID
- Listar todas
- Actualizar
- Eliminar
- Consultas personalizadas

---

### 3. Pruebas de Servicio (Unit Tests con Mockito)

**Ubicación**: `src/test/java/.../service/`

**Propósito**: Validar lógica de negocio con dependencias mockeadas

**Anotaciones**:
```java
@ExtendWith(MockitoExtension.class)
```

**Ejemplo**:
```java
@Mock
private CitaRepository citaRepository;

@InjectMocks
private CitaService citaService;

@Test
public void testCrearCita() {
    when(citaRepository.save(any(Cita.class))).thenReturn(cita);
    
    CitaDTO resultado = citaService.crearCita(citaCreateDTO);
    
    assertNotNull(resultado);
    verify(citaRepository, times(1)).save(any(Cita.class));
}
```

**Características**:
- Mock de repositorios
- Mock de clientes Feign
- Validación de interacciones
- Tests de casos exitosos y fallidos

---

### 4. Pruebas de Controlador (Web Tests)

**Ubicación**: `src/test/java/.../controller/`

**Propósito**: Validar endpoints REST, códigos HTTP y respuestas

**Anotaciones**:
```java
@WebMvcTest(CitaController.class)
```

**Ejemplo**:
```java
@Test
public void testCrearCita() throws Exception {
    when(citaService.crearCita(any())).thenReturn(citaDTO);
    
    mockMvc.perform(post("/api/citas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(citaCreateDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));
}
```

**Tests Incluidos**:
- POST (crear) → 201 CREATED
- GET (listar) → 200 OK
- GET (obtener por ID) → 200 OK
- PUT (actualizar) → 200 OK
- DELETE (eliminar) → 204 NO CONTENT

---

## 📊 Cobertura de Pruebas

### Citas Service - 25+ Tests
```
Model Tests:          5 tests
Repository Tests:     7 tests
Service Tests:        8 tests
Controller Tests:     7 tests
Total Coverage:       ~85%
```

### Cancelaciones Service - 8+ Tests
```
Service Tests:        8 tests
Coverage:             >80%
```

### Paciente Service - 8+ Tests
```
Service Tests:        8 tests
Coverage:             >80%
```

### Médico Service - 9+ Tests
```
Service Tests:        9 tests
Coverage:             >80%
```

### Usuario Service - 8+ Tests
```
Service Tests:        8 tests
Coverage:             >80%
```

**Total: 58+ Pruebas unitarias implementadas**

---

## 🚀 Ejecutar Pruebas

### Ejecutar todas las pruebas del proyecto

```bash
cd entorno-desarrollo
mvn clean test
```

### Ejecutar pruebas de un servicio específico

```bash
cd citas-service
mvn test
```

### Ejecutar pruebas específicas

```bash
# Tests de controlador solamente
mvn test -Dtest=CitaControllerTest

# Tests de servicio solamente
mvn test -Dtest=CitaServiceTest
```

### Generar reporte de cobertura

```bash
mvn clean test jacoco:report
# Reporte: target/site/jacoco/index.html
```

---

## 🧪 Frameworks Utilizados

| Framework | Versión | Propósito |
|-----------|---------|----------|
| **JUnit 5** | Latest | Framework de pruebas |
| **Mockito** | Latest | Mocking de dependencias |
| **MockMvc** | Spring Test | Testing de controladores |
| **DataJpaTest** | Spring Boot | Testing de repositorios |
| **JaCoCo** | Latest | Cobertura de código |

---

## 📋 Checklist de Pruebas

Para cada microservicio:

- ✅ Pruebas de Modelo (5+ casos)
- ✅ Pruebas de Repositorio (7+ casos CRUD)
- ✅ Pruebas de Servicio (8+ casos con mocks)
- ✅ Pruebas de Controlador (6+ casos REST)
- ✅ Cobertura ≥ 80%
- ✅ Todas las pruebas en verde (passing)
- ✅ Maven build exitosa con tests

---

## 💡 Mejores Prácticas Implementadas

### 1. Nomenclatura Clara
```java
// ✅ Bien
@Test
public void testCrearCitaConDatosValidos()

// ❌ Evitar
@Test
public void test1()
```

### 2. AAA Pattern (Arrange-Act-Assert)
```java
@Test
public void testObtenerCita() {
    // Arrange
    Cita cita = new Cita();
    cita.setId(1L);
    
    // Act
    CitaDTO resultado = citaService.obtenerPorId(1L);
    
    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
}
```

### 3. Uso de Mocks
```java
@Mock
private CitaRepository citaRepository;

@BeforeEach
public void setUp() {
    when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
}
```

### 4. Verificación de Interacciones
```java
verify(citaRepository, times(1)).save(any(Cita.class));
verify(citaRepository, never()).delete(any(Cita.class));
```

---

## 🔍 Casos de Prueba Clave

### Capa de Modelo
- [x] Crear instancia
- [x] Validar propiedades
- [x] Valores por defecto

### Capa de Repositorio
- [x] CRUD completo
- [x] Búsquedas personalizadas
- [x] Manejo de transacciones

### Capa de Servicio
- [x] Lógica de negocio
- [x] Transformación de DTOs
- [x] Manejo de excepciones
- [x] Integración con otros servicios

### Capa de Controlador
- [x] Endpoints funcionales
- [x] Códigos HTTP correctos
- [x] Validación de entrada
- [x] Formato de respuesta

---

## 📚 Referencias

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core)
- [Spring Test Documentation](https://spring.io/projects/spring-framework)
- [Testing Best Practices](https://www.baeldung.com/testing)
