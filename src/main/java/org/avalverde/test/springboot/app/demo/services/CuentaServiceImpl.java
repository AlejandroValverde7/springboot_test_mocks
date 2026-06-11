package org.avalverde.test.springboot.app.demo.services;

import org.avalverde.test.springboot.app.demo.models.Banco;
import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.repositories.BancoRepository;
import org.avalverde.test.springboot.app.demo.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencia(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional //No le indicamos readonly ya que modifica
    public void transferir(Long numCuentaOrigen, Long NumCuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
        cuentaOrigen.credito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino= cuentaRepository.findById(NumCuentaDestino).orElseThrow();
        cuentaDestino.debito(monto);
        cuentaRepository.save(cuentaDestino);

        //si todo sale bien se actualizan las transferencias
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional()
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
