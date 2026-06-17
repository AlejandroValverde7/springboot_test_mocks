package org.avalverde.test.springboot.app.demo;

import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.repositories.CuentaRepository;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integracion_jpa")
@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test

    void testFindId() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());

    }

    @Test
    void testFindByPerson() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andrés");
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, () -> {
           cuenta.orElseThrow();
        });
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave(){
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        Cuenta save = cuentaRepository.save(cuentaPepe);

        Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        assertEquals("Pepe",cuenta.getPersona());
        assertEquals("3000",cuenta.getSaldo().toPlainString());
//        assertEquals(3,cuenta.getId());
    }

    @Test
    void testUpdate(){
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        Cuenta save = cuentaRepository.save(cuentaPepe);

        Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        assertEquals("Pepe",cuenta.getPersona());
        assertEquals("3000",cuenta.getSaldo().toPlainString());
//        assertEquals(3,cuenta.getId());

        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        assertEquals("Pepe",cuentaActualizada.getPersona());
        assertEquals("3800",cuentaActualizada.getSaldo().toPlainString());
    }

    //comprobamos por medio de la exvcepcion q lanza al bucarlka que se ha eliminado
    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();

        assertEquals("Jhon", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
           cuentaRepository.findByPersona("Jhon").orElseThrow();
        });
    }
}

