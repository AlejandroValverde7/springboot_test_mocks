package org.avalverde.test.springboot.app.demo;

import org.avalverde.test.springboot.app.demo.models.Banco;
import org.avalverde.test.springboot.app.demo.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
//    public static final Cuenta CUENTA_001 = new Cuenta(1L,"Andrés", new BigDecimal("1000"));
//    public static final Cuenta CUENTA_002 = new Cuenta(2L,"Jhon", new BigDecimal("2000"));
    public static final Cuenta CUENTA_003 = new Cuenta(3L,"David", new BigDecimal("3000"));
//    public static final Banco BANCO = new Banco(1L,"Banco de finanzas", 0);

    //Creamos los metodos en vez de lñas constantes para evitar errores de datos en los test
    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L,"Andrés", new BigDecimal("1000")));
    }

    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L,"Jhon", new BigDecimal("2000")));
    }

    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L,"Banco de finanzas", 0));
    }
}
