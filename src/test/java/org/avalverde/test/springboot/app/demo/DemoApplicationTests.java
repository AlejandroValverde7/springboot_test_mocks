package org.avalverde.test.springboot.app.demo;

import org.avalverde.test.springboot.app.demo.exceptions.DineroInsuficienteException;
import org.avalverde.test.springboot.app.demo.models.Banco;
import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.repositories.BancoRepository;
import org.avalverde.test.springboot.app.demo.repositories.CuentaRepository;
import org.avalverde.test.springboot.app.demo.services.CuentaService;
import org.avalverde.test.springboot.app.demo.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.avalverde.test.springboot.app.demo.Datos.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.DataOutput;
import java.math.BigDecimal;

@SpringBootTest
class DemoApplicationTests {

	@MockBean
	CuentaRepository cuentaRepository;
	@MockBean
	BancoRepository bancoRepository;

	@Autowired
	CuentaServiceImpl service;

	@BeforeEach
	void setUp() {
//		cuentaRepository = mock(CuentaRepository.class);
//		bancoRepository = mock(BancoRepository.class);
//		service = new CuentaServiceImpl(cuentaRepository,bancoRepository);
		//reiniciamos los datos en cada test
//		Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
//		Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
//		Datos.BANCO.setTotalTransferencias(0);
	}

	//Test para que cuando se busque por esos id se encuentren las cuentas indicadas
	@Test
	void contextLoads() {
		//indicamos los valores que se deben devovler segun el parametro de entrada
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());

		//Dentro del revisar saldo se devuelven las cuentas indicadas arriba
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		//comprobamos el saldo
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		//hacemos una transferencia de una cuenta a otra
		service.transferir(1L,2L,new BigDecimal("100"),1L);

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);

		assertEquals("900",saldoOrigen.toPlainString());
		assertEquals("2100",saldoDestino.toPlainString());

		//Verificamos que se realizan todas las llamadas a losmmetodos
		int total = service.revisarTotalTransferencia(1L);
		assertEquals(1,total);

		verify(cuentaRepository,times(3)).findById(1L);
		verify(cuentaRepository,times(3)).findById(2L);
		verify(cuentaRepository,times(2)).save(any(Cuenta.class));

		verify(bancoRepository,times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

		verify(cuentaRepository,times(6)).findById(anyLong());
		verify(cuentaRepository,never()).findAll();
	}

	//Test para que cuando se busque por esos id se encuentren las cuentas indicadas
	@Test
	void contextLoads2() {
		//indicamos los valores que se deben devovler segun el parametro de entrada
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());

		//Dentro del revisar saldo se devuelven las cuentas indicadas arriba
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		//comprobamos el saldo
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		//Para comprobar si lanza la excepcion
		assertThrows(DineroInsuficienteException.class, () -> {
		service.transferir(1L,2L,new BigDecimal("1200"),1L);
		});

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);

		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		//Verificamos que se realizan todas las llamadas a losmmetodos
		int total = service.revisarTotalTransferencia(1L);
		assertEquals(0,total);

		verify(cuentaRepository,times(3)).findById(1L);
		verify(cuentaRepository,times(2)).findById(2L);
		verify(cuentaRepository,never()).save(any(Cuenta.class));

		verify(bancoRepository,times(1)).findById(1L);
		verify(bancoRepository,never()).save(any(Banco.class));
	}

	@Test
	void contextLOads3() {
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());

		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);

		//Para comprobar que los 2 objetos son el mismo objeto
		assertSame(cuenta1,cuenta2);
		assertTrue(cuenta1 == cuenta2); //igual q assertSame
		assertEquals("Andrés",cuenta1.getPersona());
		assertEquals("Andrés",cuenta2.getPersona());
		verify(cuentaRepository, times(2)).findById(1L);
	}
}
