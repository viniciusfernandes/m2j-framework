package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mContext;

import org.junit.Test;

public class OrderFunctionTest {

	private mContext m$ = mContext.createSemAcesso();

	public OrderFunctionTest() {
		m$.var("b").set(88);
		m$.var("a").set(12);
		m$.var("a", "1").set(6);
		m$.var("a", "1", "amarelo").set(2);
		m$.var("a", "1", "azul").set(4);
		m$.var("a", "1", "branco").set(14);
		m$.var("a", "1", "preto").set(4);
		m$.var("a", "2").set(8);

		m$.var("b", " ").set(66);
		m$.var("b", 1).set(68);
		m$.var("b", "2").set(99);
	}

	@Test
	public void testShobby() {
		String varatalinhas = "^VARAtaLinhas";
		mContext m$ = mContext.createSemAcesso();

		m$.var(varatalinhas, "0", "2", "90520", "161/2012-27").set(null);
		assertEquals("Fail on first navegation through the level", "0", m$.Fnc.$order(m$.var(varatalinhas, "")));
		assertEquals("Fail on first navegation through the level", "90520", m$.Fnc.$order(m$.var(varatalinhas, "0", "2", "")));
	}

	@Test
	public void testOrderFirstLevel() {
		assertEquals("Fail on first navegation through the first level", "a", m$.Fnc.$order(m$.var("")));

		assertEquals("Fail on first navegation through the first level", "1", m$.Fnc.$order(m$.var("a", "")));
		assertEquals("Fail on first navegation through the first level", "2", m$.Fnc.$order(m$.var("a", "1")));
		assertEquals("Fail on first navegation through the first level", "2", m$.Fnc.$order(m$.var("a", "1")));
		assertEquals("Fail on second navegation through the first level", "", m$.Fnc.$order(m$.var("a", "2")));

		assertEquals("Fail on first navegation through the first level", "azul", m$.Fnc.$order(m$.var("a", "1", "amarelo")));
		assertEquals("Fail on first navegation through the first level", "branco", m$.Fnc.$order(m$.var("a", "1", "azul")));
		assertEquals("Fail on first navegation through the first level", "preto", m$.Fnc.$order(m$.var("a", "1", "branco")));
		assertEquals("Fail on first navegation through the first level", "", m$.Fnc.$order(m$.var("a", "1", "preto")));

	}

	@Test
	public void testOrderFirstLevelInverse() {
		assertEquals("Fail on first navegation through the first level", "a", m$.Fnc.$order(m$.var(""), -1));

		assertEquals("Fail on first navegation through the first level", "2", m$.Fnc.$order(m$.var("a", ""), -1));
		assertEquals("Fail on first navegation through the first level", "1", m$.Fnc.$order(m$.var("a", "2"), -1));
		assertEquals("Fail on first navegation through the first level", "", m$.Fnc.$order(m$.var("a", "1"), -1));
	}

	@Test
	public void testOrderOfAllPublicVariablesOnMemory() {
		mContext m$ = mContext.createSemAcesso();
		m$.var("%x", "1", "2").set(1);
		m$.var("%").set(1);
		m$.var("%g", "2").set(2);
		m$.var("%a", "5", "2").set(4);
		m$.var("e", "6").set(66);

		assertEquals("Fail to find first public variable on memory.", "%a", m$.Fnc.$order(m$.var("%")));
		assertEquals("Fail to find first public variable on memory.", "%g", m$.Fnc.$order(m$.var("%a")));
		assertEquals("Fail to find first public variable on memory.", "%x", m$.Fnc.$order(m$.var("%g")));
		assertEquals("Fail to find first public variable on memory.", "e", m$.Fnc.$order(m$.var("%x")));
		assertEquals("Fail to find first public variable on memory.", "", m$.Fnc.$order(m$.var("e")));
		assertEquals("Fail to find first public variable on memory.", "%", m$.Fnc.$order(m$.var("")));
	}

	@Test
	public void testOrderOfInexistentGlobalAtStart() {
		mContext m$ = mContext.createSemAcesso();
		m$.var("^CacheTempTestInMemory").kill();
		m$.var("^CacheTempTestInMemory", "subscript 1", "1", "zzz").set(1);
		m$.var("^CacheTempTestInMemory", "subscript 1", "2", "zzz").set(1);

		assertEquals("1", m$.Fnc.$order(m$.var("^CacheTempTestInMemory", "subscript 1", "0")));
	}

	@Test
	public void testOrderWithBlankSubscripts() {
		assertEquals("2", m$.Fnc.$order(m$.var("b", 1)));
		assertEquals(" ", m$.Fnc.$order(m$.var("b", 2)));
	}

	@Test
	public void testSearchingVariableValue() {
		assertEquals("Fail on searching local variable value", 88, m$.var("b").get());
		assertEquals("Fail on searching local variable value", 12, m$.var("a").get());
		assertEquals("Fail on searching local variable value", 6, m$.var("a", "1").get());
		assertEquals("Fail on searching local variable value", 2, m$.var("a", "1", "amarelo").get());
		assertEquals("Fail on searching local variable value", 8, m$.var("a", "2").get());
	}

	@Test
	public void testSettingDoubleValueSubscript() {

		m$.var("x", "4").set(4);
		m$.var("x", 1).set(1);
		m$.var("x", "9.9").set(9);
		m$.var("x", 11.1).set(11);
		m$.var("x", 12).set(12);
		m$.var("x", "a").set("a");

		// Aqui a ordem deve ser a seguinte: 1, 4, 9.9, 11.1, 12, a
		assertEquals("4", m$.Fnc.$order(m$.var("x", 1)));
		assertEquals("9.9", m$.Fnc.$order(m$.var("x", 4)));
		assertEquals(11.1, m$.Fnc.$order(m$.var("x", "9.9")));
		assertEquals(12, m$.Fnc.$order(m$.var("x", 11.1)));
		assertEquals("a", m$.Fnc.$order(m$.var("x", 12)));
		assertEquals("", m$.Fnc.$order(m$.var("x", "a")));
	}

	@Test
	public void testVariableWithoutSubscriptAndValue() {

		m$.var("x", "4").set(4);
		m$.var("x", 1).set(1);
		m$.var("x", "9.9").set(9);
		m$.var("x", 11.1).set(11);
		m$.var("x", 12).set(12);
		m$.var("x", "a").set("a");
		m$.var("x", "b", 1).set("c");
		m$.var("x", "b", 2).set("d");

		// Aqui a ordem deve ser a seguinte: 1, 4, 9.9, 11.1, 12, a
		assertEquals("4", m$.Fnc.$order(m$.var("x", 1)));
		assertEquals("9.9", m$.Fnc.$order(m$.var("x", 4)));
		assertEquals(11.1, m$.Fnc.$order(m$.var("x", "9.9")));
		assertEquals(12, m$.Fnc.$order(m$.var("x", 11.1)));
		assertEquals("a", m$.Fnc.$order(m$.var("x", 12)));
		assertEquals("b", m$.Fnc.$order(m$.var("x", "a")));
		assertEquals("", m$.Fnc.$order(m$.var("x", "b")));
		m$.var("x", "b", 1).kill();
		m$.var("x", "b", 2).kill();
		assertEquals("", m$.Fnc.$order(m$.var("x", "a")));
	}

}
