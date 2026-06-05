package org.avalverde.test.springboot.app.demo.models;

import org.avalverde.test.springboot.app.demo.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

public class Cuenta {

    private Long id;
    private String persona;
    private BigDecimal saldo;

    //Constructores
    public Cuenta() {
    }

    public Cuenta(Long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getPersona() {return persona;}
    public void setPersona(String persona) {this.persona = persona;}

    public BigDecimal getSaldo() {return saldo;}
    public void setSaldo(BigDecimal saldo) {this.saldo = saldo;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id) && Objects.equals(persona, cuenta.persona) && Objects.equals(saldo, cuenta.saldo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, persona, saldo);
    }

    //metodo para quitar dinero
    public void credito(BigDecimal monto){
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
        }
        this.saldo = nuevoSaldo;
    }

    //metodo para dar dinero
    public void debito(BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }
}
