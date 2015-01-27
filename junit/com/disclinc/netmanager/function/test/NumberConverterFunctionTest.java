package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.mFunctionUtil;

public class NumberConverterFunctionTest {

	@Test
	public void testFormatAlpha() {
		Object obj = "-TEST";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 0D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatAlphaNum() {
		Object obj = "TESTE123.5";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 0D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatAlphaStartWithPoint() {
		Object obj = ".TESTE123.5";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 0D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatDecimal() {
		Object obj = "+00049.95";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 49.95d;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatDoublePoints() {
		Object obj = "34.34.666";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 34.34D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatInteger() {
		Object obj = "12345";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 12345d;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatNumAlpha() {
		Object obj = "34.3ABCD";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 34.3D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatSign() {
		Object obj = "+-+-++---+34.34xxx";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = -34.34D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}

	@Test
	public void testFormatStartWithZero() {
		Object obj = "0000034.34xxx";
		Double dbl = mFunctionUtil.numberConverter(obj);
		Double expected = 34.34D;
		assertEquals("Fail to generate number from a string", expected, dbl);
	}
}
