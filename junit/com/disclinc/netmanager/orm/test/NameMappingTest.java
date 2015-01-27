package com.disclinc.netmanager.orm.test;

import static org.junit.Assert.*;
import mLibrary.mContext;

import org.junit.Test;

import ORM.mNameMapping;

public class NameMappingTest {

	@Test
	public void testNomeObjetoComplexo1() {
		mContext m$ = mContext.createSemAcesso();
		mNameMapping n1 = new mNameMapping(m$, "TabComplexa_TesteTeste_TesteTeste1", false);
		mNameMapping n2 = new mNameMapping(m$, "TabComplexa_TesteTeste_TesteTeste2", false);
		assertEquals("TabComplexa_TesteTeste_TesteTe", n1.getMapName());
		assertEquals("TabComplexa_TesteTeste_TesteT1", n2.getMapName());
	}

	@Test
	public void testNomeObjetoComplexo2() {
		mContext m$ = mContext.createSemAcesso();
		mNameMapping n1 = new mNameMapping(m$, "TabComplexa_TesteTesteTeste_Teste1", false);
		mNameMapping n2 = new mNameMapping(m$, "TabComplexa_TesteTesteTeste_Teste2", false);
		assertEquals("TabComplexa_TesteTesteT_Teste1", n1.getMapName());
		assertEquals("TabComplexa_TesteTesteT_Teste2", n2.getMapName());
	}

	@Test
	public void testNomeObjetoSimples() {
		mContext m$ = mContext.createSemAcesso();
		mNameMapping n1 = new mNameMapping(m$, "TabSimples", false);
		assertEquals("TabSimples", n1.getMapName());
	}

	@Test
	public void testNomeSubObjetoComplexo1() {
		mContext m$ = mContext.createSemAcesso();
		mNameMapping n1 = new mNameMapping(m$, "TabComplexa_TesteTeste_TesteTeste1", true);
		mNameMapping n2 = new mNameMapping(m$, "TabComplexa_TesteTeste_TesteTeste2", true);
		assertEquals("TabComplexa_TesteTeste_TesteTe", n1.getMapName());
		assertEquals("TabComplexa_TesteTeste_TesteT1", n2.getMapName());
	}

}
