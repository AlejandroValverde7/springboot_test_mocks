# Proyecto de Testing con Spring Boot, JUnit 5 y Mockito

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![JUnit](https://img.shields.io/badge/JUnit-5-red)
![Mockito](https://img.shields.io/badge/Mockito-4-blue)
![Database](https://img.shields.io/badge/Database-H2-lightgrey)
![Swagger](https://img.shields.io/badge/Swagger-API_Docs-green)
![Maven](https://img.shields.io/badge/Maven-Build-yellow)

## Descripción del Proyecto

Este proyecto implementa una API REST con Spring Boot, acompañada de una arquitectura de pruebas completa que cubre:

- Tests unitarios con JUnit 5 y Mockito
- Tests de integración con TestRestTemplate
- Tests de controladores con MockMvc
- Tests de repositorios con H2
- Documentación de API con Swagger

El objetivo es demostrar buenas prácticas de testing en aplicaciones Java modernas.

---

##  Tipos de Tests Implementados

###  Tests Unitarios

**Tecnologías:** JUnit 5, Mockito

Validan la lógica de negocio sin cargar el contexto Spring. Incluyen:

- Mock de repositorios
- Simulación de comportamientos (`when`, `thenReturn`)
- Verificación de interacciones (`verify`)
- Tests de excepciones (`assertThrows`)

###  Tests de Integración

**Tecnologías:** SpringBootTest, TestRestTemplate, H2

Prueban el flujo completo de la aplicación:

- Controladores reales
- Servicios reales
- Repositorios reales
- Serialización JSON

**Ejemplo de configuración:**

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTest { ... }
```

### Tests de Controladores

**Tecnologías:** MockMvc

Validan:

- Endpoints
- Códigos HTTP
- JSON devuelto
- Validaciones

### Tests de Repositorios

**Tecnologías:** DataJpaTest, H2

Permiten probar:

- Consultas personalizadas
- Persistencia
- Relaciones
- Integridad de datos

---

## Caso Real: Transferencias Bancarias

El dominio incluye:

- Cuenta
- Banco
- Transferencias

Los tests validan:

- Cálculo de saldos
- Movimientos entre cuentas
- Persistencia
- Errores por saldo insuficiente

---

## Ejemplo de Test

```java
@Test
@Order(2)
void testDetalle() {
    ResponseEntity<Cuenta> respuesta = client.getForEntity(crearUri("api/cuentas/1"), Cuenta.class);
    assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
    Cuenta cuenta = respuesta.getBody();
    assertNotNull(cuenta);
    assertEquals(1L, cuenta.getId());
    assertEquals("Andrés", cuenta.getPersona());
    assertEquals("900.00", cuenta.getSaldo().toPlainString());
}
```

---

## 📚 Documentación Swagger

Swagger está habilitado para visualizar y probar la API.

| Recurso | URL |
|---------|-----|
| 📄 JSON de la API | `http://localhost:8080/v2/api-docs` |
| 🖥️ Interfaz UI | `http://localhost:8080/swagger-ui/` |

---

## 🧰 Tecnologías Usadas

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje principal |
| Spring Boot 3.x | Framework backend |
| Spring Web | Controladores REST |
| Spring Data JPA | Persistencia |
| H2 Database | Base de datos en memoria para tests |
| JUnit 5 | Framework de testing |
| Mockito | Mocking de dependencias |
| MockMvc | Testing de controladores |
| TestRestTemplate | Testing de integración |
| Swagger / SpringFox | Documentación de API |
| Maven | Gestión de dependencias |

---

## ▶️ Ejecución de Tests

**Maven:**

```bash
mvn test
```

**IDE:**

Click derecho → *Run tests*

---

## 🚀 Objetivo del Proyecto

Este proyecto sirve como base para aprender:

- Buenas prácticas de testing
- Diseño orientado a pruebas
- Mocking profesional
- Pruebas de integración reales
- Documentación de APIs
