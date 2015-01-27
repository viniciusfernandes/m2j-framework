package com.disclinc.netmanager.function.test;

import static mLibrary.mFunction.$replace;
import static mLibrary.mFunction.$translate;
import static org.junit.Assert.*;

import org.junit.Test;

import util.mFunctionUtil;

public class ReplaceFunctionTest {

	@Test
	public void testCovertToLowercase() {
		assertEquals("Fail on converto to lowercase", "asdf33", mFunctionUtil.zconvert("ASDF33", "L"));
	}

	@Test
	public void testCovertToUppercase() {
		assertEquals("Fail on converto to uppcase", "ASDF33", mFunctionUtil.zconvert("asdf33", "u"));
	}

	@Test
	public void testCovertUsingModeNotDefined() {
		assertEquals("Fail on converto to lowercase", "asdf33", mFunctionUtil.zconvert("asdf33", "X"));
	}

	@Test
	public void testReplaceSubstring() {
		String string = "apenas xxx mais um teste xxx";
		String oldSubstring = "xxx";
		String newSubstring = "333";

		assertEquals("Fail on replace substring", "apenas 333 mais um teste 333",
				$replace(string, oldSubstring, newSubstring));
	}

	@Test
	public void testTranslateSubstring() {
		String string = "primeiro,.cliente,,..,";
		String oldSubstring = ",.";
		String newSubstring = "XY";

		assertEquals("Fail on translate substring", "primeiroXYclienteXXYYX",
				$translate(string, oldSubstring, newSubstring));
	}

	@Test
	public void testTranslateSubstringBlankOldSubstring() {
		String string = "nome do cliente";
		String oldSubstring = " ";
		String newSubstring = "XY";

		assertEquals("Fail on translate substring", "nomeXdoXcliente", $translate(string, oldSubstring, newSubstring));
	}

	/*
	 * @Test public void testTranslateNullSubstring() { String string = null;
	 * String oldSubstring = ",.w"; String newSubstring = "XY"; boolean isThrowed
	 * = false; try { $translate(string, oldSubstring, newSubstring); } catch
	 * (IllegalArgumentException e) { isThrowed = true; }
	 * 
	 * assertTrue("String to be translated must no be null", isThrowed);
	 * 
	 * }
	 */
	@Test
	public void testTranslateSubstringEmptyOldSubstring() {
		String string = "nome do cliente";
		String oldSubstring = "";
		String newSubstring = "XY";

		assertEquals("Fail on translate substring", "nome do cliente", $translate(string, oldSubstring, newSubstring));
	}

	@Test
	public void testTranslateSubstringUsingNewSubstringDifferentLength() {
		String string = "primeiro,.cliente,,..,www.google.com ou   ddd";
		String oldSubstring = ",.w";
		String newSubstring = "XY";

		assertEquals("Fail on translate substring", "primeiroXYclienteXXYYXYgoogleYcom ou   ddd",
				$translate(string, oldSubstring, newSubstring));
	}

}
