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
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    void testDetalle() {
        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Andrés")
                .jsonPath("$.saldo").isEqualTo(1000);
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
}