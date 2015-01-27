package com.disclinc.netmanager.variable.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mContext;
import mLibrary.mVar;

import org.junit.Before;
import org.junit.Test;

public class MContextUsingInMemoryAccessTest {
	private mContext m$;

	@Before
	public void init() {
		m$ = mContext.createSemAcesso();
	}

	@Test
	public void testLocalVariableAccess() {
		m$.var("x").set("local");
		assertEquals("local", m$.var("x").get());
	}

	@Test
	public void testPublicVariableAccess() {
		m$.var("%x").set("publica");
		assertEquals("publica", m$.var("%x").get());
	}

	@Test
	public void testSettingVariableValueInSingleStep() {
		// Setting value in single step
		m$.var("carro", "esportivo").set(66);
		assertEquals(66, m$.var("carro", "esportivo").get());
	}

	@Test
	public void testSettingVariableValueInTwoSteps() {
		// Setting value in two steps
		mVar pedido = m$.var("pedido");
		pedido.s$("item").set(33);
		assertEquals(33, m$.var("pedido", "item").get());
	}
}
