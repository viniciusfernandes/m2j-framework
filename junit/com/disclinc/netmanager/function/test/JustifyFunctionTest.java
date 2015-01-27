package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class JustifyFunctionTest {
	@Test
	public void testJustifyAlphaNumeric() {
		Object expression = "x2";
		int width = 5;
		String expected = "   x2";
		Object actual = mFunction.$justify(expression, width);
		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyDoubleRoundigValueAndBlanckSpaces() {
		Object expression = "1.259";
		int width = 6;
		String expected = "  1.26";
		Object actual = mFunction.$justify(expression, width, 2);
		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyDoubleRoundigValueIncreasingDecimalsAndBlanckSpaces() {
		Object expression = "1.259";
		int width = 7;
		String expected = " 1.2590";
		Object actual = mFunction.$justify(expression, width, 4);
		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyDoubleValueAndBlanckSpaces() {
		Object expression = "1.1";
		int width = 5;
		String expected = "  1.1";
		Object actual = mFunction.$justify(expression, width, 1);
		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyEmptyString() {

		Object expression = "";
		int width = 3;

		String expected = "   ";
		Object actual = mFunction.$justify(expression, width);

		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyIntegerValueWithOneDecimals() {
		Object expression = "1";
		int width = 3;
		String expected = "1.0";
		Object actual = mFunction.$justify(expression, width, 1);
		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyNullPointer() {

		Object expression = null;
		int width = 3;

		Object actual = mFunction.$justify(expression, width);
		assertEquals("Fail on justify with some string", null, actual);
	}

	@Test
	public void testJustifySomeStringWithNegativeWidth() {

		Object expression = "x";
		int width = -1;

		String expected = "x";
		Object actual = mFunction.$justify(expression, width);

		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifySomeStringWithTwoParameters() {

		Object expression = "x";
		int width = 3;

		String expected = "  x";
		Object actual = mFunction.$justify(expression, width);

		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifySomeStringWithZeroWidth() {

		Object expression = "x";
		int width = 0;

		String expected = "x";
		Object actual = mFunction.$justify(expression, width);

		assertEquals("Fail on justify with some string", expected, actual);
	}

	@Test
	public void testJustifyStringAsNumericValue() {
		Object expression = "1x";
		int width = 5;
		String expected = " 1.00";
		Object actual = mFunction.$justify(expression, width, 2);
		assertEquals("Fail on justify with some string", expected, actual);
	}

}
