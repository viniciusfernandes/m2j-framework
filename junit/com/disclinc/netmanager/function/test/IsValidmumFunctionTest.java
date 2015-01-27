package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class IsValidmumFunctionTest {

	@Test
	public void testValidadeAlpha() {
		Object obj = "-TEST";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateAlphaNum() {
		Object obj = "TESTE123.5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateDecimal() {
		Object obj = "49.95";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateDoublePoints() {
		Object obj = "34.34.666";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateDoubleWithMinusSign() {
		Object obj = "-49.99";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateDoubleWithPlusSign() {
		Object obj = "+49.99";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateExponential() {
		Object obj = "49E5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateExponentialMinusSign() {
		Object obj = "-49E5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateExponentialPlusSign() {
		Object obj = "+49E5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateExponentialWithDotSign() {
		Object obj = "4.9E5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateExponentialWithLongIndex() {
		Object obj = "49E589876";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateInteger() {
		Object obj = "12345";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateIntegerWithMinusSign() {
		Object obj = "-49";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateIntegerWithPlusSign() {
		Object obj = "+49";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = true;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateNegativeExponential() {
		Object obj = "4.9E-5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateNullPointer() {
		boolean actual = mFunction.$isvalidnum(null);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateNumAlpha() {
		Object obj = "34.3ABCD";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidatePositiveExponential() {
		Object obj = "4.9E+5";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateStartWithZero() {
		Object obj = "0000034.34xxx";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}

	@Test
	public void testValidateWithSigns() {
		Object obj = "+-+-++---+34.34xxx";
		boolean actual = mFunction.$isvalidnum(obj);
		boolean expected = false;
		assertEquals("Fail to validate string. This is not a valid number.", expected, actual);
	}
}
