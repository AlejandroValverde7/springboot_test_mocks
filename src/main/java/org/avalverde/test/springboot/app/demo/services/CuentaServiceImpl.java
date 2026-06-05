package org.avalverde.test.springboot.app.demo.services;

import org.avalverde.test.springboot.app.demo.models.Banco;
import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.repositories.BancoRepository;
import org.avalverde.test.springboot.app.demo.repositories.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//tenemos que indicar q la impl es un service para registrarlo en spring y poder inyectarlo con autowired
@Service
public class CuentaServiceImpl implements CuentaService{
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository,BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public int revisarTotalTransferencia(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId);
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Long numCuentaOrigen, Long NumCuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen);
        cuentaOrigen.credito(monto);
        cuentaRepository.update(cuentaOrigen);

        Cuenta cuentaDestino= cuentaRepository.findById(NumCuentaDestino);
        cuentaDestino.debito(monto);
        cuentaRepository.update(cuentaDestino);

        //si todo sale bien se actualizan las transferencias
        Banco banco = bancoRepository.findById(bancoId);
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.update(banco);
    }
}
