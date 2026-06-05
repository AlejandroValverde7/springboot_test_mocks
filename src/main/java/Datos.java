import org.avalverde.test.springboot.app.demo.models.Banco;
import org.avalverde.test.springboot.app.demo.models.Cuenta;

import java.math.BigDecimal;

public class Datos {
    public static final Cuenta CUENTA_001 = new Cuenta(1L,"Andrés", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L,"Jhon", new BigDecimal("1000"));
    public static final Cuenta CUENTA_003 = new Cuenta(3L,"David", new BigDecimal("1000"));
    public static final Banco BANCO = new Banco(1L,"Banco de finanzas", 0);
}
