package com.disclinc.netmanager.variable.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mContext;

import org.junit.Before;
import org.junit.Test;

public class DispatchMethodsTest {
	private mContext m$ = mContext.createSemAcesso();

	@Before
	public void init() {
		m$ = mContext.createSemAcesso();
		m$.var("pedido").set(78);
		m$.var("entrega").set(57);
		m$.var("cliente").set(6);
	}

	@Test
	public void testChangeValueOfVariableInsideMacro() {
		// Executing some function which makes new operator calling
		assertEquals("Fail to recover value of the variable after", 99,
				m$.dispatch(true, null, "com.disclinc.netmanager.variable.test.Macros.$$$alterarValorVariavel", m$));

		// After a function call which does not make new operator calling we
		// must have the same value before the calling
		assertEquals(99, m$.var("pedido").get());
	}

	@Test
	public void testMethodCallingNewOperatorAndGetVariableUnchanged() {
		// Executing some function which makes no one new operator calling
		assertEquals(57, m$.dispatch(true, null,
				"com.disclinc.netmanager.variable.test.Macros.$$$recuperarValorInalteradoDepoisDoOperadorNew", m$));

		// After a dispatch function calls we must have the same value before
		// the calling
		assertEquals(78, m$.var("pedido").getValue());
	}

	@Test
	public void testMethodWithReturnAndNoCallingNewOperator() {
		// Executing some function which makes no one new operator calling
		assertEquals(30,
				m$.dispatch(true, null, "com.disclinc.netmanager.variable.test.Macros.$$$diasNoMes", (Object[]) null));

		// After a function call which does not make new operator calling we
		// must have the same value before the calling
		assertEquals(78, m$.var("pedido").get());
	}

	@Test
	public void testRecuperarValorNovoDepois3ChamadasDoOperadorNew() {
		// Executing some function which makes new operator calling
		assertEquals("Fail to recover value of the variable after", "chamada3", m$.dispatch(true, null,
				"com.disclinc.netmanager.variable.test.Macros.$$$recuperarValorNovoDepois3ChamadasDoOperadorNew", m$));

		// After a function call which does not make new operator calling we
		// must have the same value before the calling
		assertEquals(57, m$.var("entrega" + "").get());
	}
	
	@Test
	public void testechamandoOperadorMetodoNewdeClassev2() {
		// Executing some function which makes new operator calling
		assertEquals("Fail to recover value of the variable after", "chamada3", m$.dispatch(true, null,
				"com.disclinc.netmanager.variable.test.Macros.$$$recuperarValorNovoDepois3ChamadasDoOperadorNew", m$));

		// After a function call which does not make new operator calling we
		// must have the same value before the calling
		assertEquals(57, m$.var("entrega" + "").get());
	}
}
