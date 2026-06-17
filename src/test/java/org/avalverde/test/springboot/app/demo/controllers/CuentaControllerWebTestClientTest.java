package org.avalverde.test.springboot.app.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integracion_wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestClientTest {

    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Order(1)
    @Test
    void testTransferir() throws JsonProcessingException {

        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(1L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("mensaje","Transferencia realizada con exito");
        response.put("transaccion",dto);

        client.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        assertEquals("Transferencia realizada con exito",json.path("mensaje").asText());
                        assertEquals(1L,json.path("transaccion").path("cuentaOrigenId").asLong());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("Transferencia realizada con exito"))
                .jsonPath("$.mensaje").value( valor -> {
                    assertEquals("Transferencia realizada con exito",valor);
                })
                .json(objectMapper.writeValueAsString(response));

    }

    @Order(2)
    @Test
    void testDetalle() throws JsonProcessingException {

        Cuenta cuenta = new Cuenta(1L, "Andrés", new BigDecimal("900"));

        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Andrés")
                .jsonPath("$.saldo").isEqualTo(1000)
                .json(objectMapper.writeValueAsString(cuenta));
    }

    @Order(3)
    @Test
    void testDetalle2() {
        client.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta cuenta = response.getResponseBody();
                    assertEquals("Jhon",cuenta.getPersona());
                    assertEquals("2000.00",cuenta.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(4)
    void testListar() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("Andrés")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(1000)
                .jsonPath("$[1].persona").isEqualTo("Jhon")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(2000)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    void testListar2() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertEquals(4,cuentas.size());
                    assertEquals("Andrés",cuentas.get(0).getPersona());
                    assertEquals(1,cuentas.get(0).getId());
                    assertEquals("1000.0",cuentas.get(0).getSaldo().toPlainString());
                    assertEquals("Jhon",cuentas.get(1).getPersona());
                    assertEquals(2,cuentas.get(1).getId());
                    assertEquals("2000.0",cuentas.get(1).getSaldo().toPlainString());
                });
    }

    @Test
    @Order(6)
    void testGuardar() {
        //given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        //when
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Pepe")
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.saldo").isEqualTo("3000");
    }

    @Test
    @Order(7)
    void testGuardar2() {
        //given
        Cuenta cuenta = new Cuenta(null, "Pepa", new BigDecimal("3500"));

        //when
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertEquals(4,c.getId());
                    assertEquals("Pepa",c.getPersona());
                    assertEquals("3500",c.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(8)
    void testEliminar(){
        client.get().uri("/uri/cuentas").exchange()
                .expectStatus().isOk()
                .expectBodyList(Cuenta.class)
                .hasSize(4);


        client.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        //comprobamos q borra
        client.get().uri("/uri/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

        client.get().uri("/api/cuentas/3")
                .exchange()
                //.expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}