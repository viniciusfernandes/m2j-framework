package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mLibrary.mContext;

import org.junit.Test;

public class ParseVarTest {
	@Test
	public void testAssert() {
		assertFalse(false);
		assertTrue(true);
	}

	@Test
	public void testTrace() {
		mContext m$ = mContext.createSemAcesso();
		m$.parseCall("##class(projeto.classe).metodo($get(teste,\"\"\"\"))");
	}
}
