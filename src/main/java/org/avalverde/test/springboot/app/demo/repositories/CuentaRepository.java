package org.avalverde.test.springboot.app.demo.repositories;

import org.avalverde.test.springboot.app.demo.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    void update(Cuenta cuenta);
}
