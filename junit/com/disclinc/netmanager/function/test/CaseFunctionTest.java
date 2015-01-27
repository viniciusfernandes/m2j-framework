package com.disclinc.netmanager.function.test;

import static org.junit.Assert.*;
import mLibrary.mFunction;

import org.junit.Test;

public class CaseFunctionTest {

	@Test
	public void testCaseAvoidDefaultValue() {
		Object target = "0";

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		boolean fail = false;
		try {
			mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3);
		}
		catch (IllegalArgumentException e) {
			fail = true;
		}
		assertTrue("Default value has to be present into parameters", fail);
	}

	@Test
	public void testCaseDefaultConditional() {
		Object target = "4";

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		Object defaultValue = "XXX";
		String expected = "XXX";
		Object actual = mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3, defaultValue);

		assertEquals("Fail on case a valid condition", expected, actual);
	}

	@Test
	public void testCaseFirstValidConditional() {
		Object target = "1";

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		Object defaultValue = "XXX";

		Object actual = mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3, defaultValue);

		assertEquals("Fail on case a valid condition", "amarelo", actual);
	}

	@Test
	public void testCaseLastValidConditional() {
		Object target = "3";

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		Object defaultValue = "XXX";

		Object actual = mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3, defaultValue);

		assertEquals("Fail on case a valid condition", "azul", actual);
	}

	@Test
	public void testCaseSecondValidConditional() {
		Object target = "2";

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		Object defaultValue = "XXX";

		Object actual = mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3, defaultValue);

		assertEquals("Fail on case a valid condition", "vermelho", actual);
	}

	@Test
	public void testCaseWithNullPointer() {
		Object target = null;

		Object bool1 = "1";
		Object var1 = "amarelo";
		Object bool2 = "2";
		Object var2 = "vermelho";
		Object bool3 = "3";
		Object var3 = "azul";
		Object defaultValue = "XXX";
		String expected = "XXX";
		Object actual = mFunction.$case(target, bool1, var1, bool2, var2, bool3, var3, defaultValue);

		assertEquals("Fail on case a valid condition", expected, actual);
	}

}
