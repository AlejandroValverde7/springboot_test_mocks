package org.avalverde.test.springboot.app.demo.services;

import org.avalverde.test.springboot.app.demo.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencia(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long numCuentaOrigen, Long NumCuentaDestino, BigDecimal monto,Long bancoId);
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
}
