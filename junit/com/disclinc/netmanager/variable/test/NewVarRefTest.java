package com.disclinc.netmanager.variable.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import mLibrary.mContext;
import mLibrary.mVar;

import org.junit.Before;
import org.junit.Test;

public class NewVarRefTest {
	private mContext m$ = null;

	@Before
	public void init() {
		m$ = mContext.createSemAcesso();
		m$.var("pedido").set(55);
	}

	@Test
	public void testUsageVariableThroughReferece() {
		mVar var = m$.var("pedido");
		mVar other = m$.newVarRef("pedido", var);
		assertEquals("This method have to return the same object when used a wrapper variable as second parameter", var,
				other);

		mVar x = m$.newVarRef("pedido", "p1");
		assertTrue("This method have to return diffent object when used a primitive type variable as second parameter.",
				var != x);
		assertEquals("The value setted does not matchs", "p1", x.get());

		mVar y = m$.newVarRef("item", null, "default1");
		assertEquals("The default value setted does not matchs", "default1", y.get());

		y = m$.newVarRef("carro", null, null);
		assertEquals("The null value setted does not matchs", null, y.getValue());
	}

	@Test
	public void testUsageVariableThroughRefereceAndCheckingStackedValue() {
		mVar var = m$.var("pedido");
		var.set(55);

		// Stacking this variable to set new values into that
		mVar other = m$.newVarRef("pedido", "novo");
		assertEquals(
				"This method have to return the new value, because the newVarRef method stacks this variable, then, we have to get new values",
				"novo", other.get());

		// Unstacking this variable to get old values into that
		m$.restoreVar(1);
		assertEquals(
				"This method have to return the new value, because the newVarRef method stacks this variable, then, we have to get new values",
				55, other.get());
	}

	@Test
	public void testUsageVariableThroughValue() {
		mVar var = m$.var("pedido");
		mVar other = m$.newVarRef("pedido", "cod1");
		assertEquals("This method have to return the same object when used a wrapper variable as second parameter",
				var.get(), other.get());
	}
}
