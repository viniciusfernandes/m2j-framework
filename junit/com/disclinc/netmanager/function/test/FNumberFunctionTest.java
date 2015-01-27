package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class FNumberFunctionTest {

	@Test
	public void testFNumber() {

		Object inumber = "6.25198";
		String format = "T+";
		Object decimal = "3";

		String expected = "6.252+";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testFNumberDecimal() {

		Object inumber = "4546544124";
		String format = "";
		Object decimal = "2.0";

		String expected = "4546544124.00";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testFNumberDecimalvirgula() {

		Object inumber = "4546,41";
		String format = "";
		Object decimal = "2.0";

		String expected = "4546.00";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testFNumberDecimalvirgula2() {

		Object inumber = "45.4641";
		String format = "";
		Object decimal = "2.0";

		String expected = "45.46";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testFNumberSemValor() {

		Object inumber = "SIM";
		String format = ",";
		Object decimal = "2";

		String expected = "0.00";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}
	
	@Test
	public void testFNumberaddPonto() {

		Object inumber = "43443434";
		String format = ",";
		Object decimal = "2";

		String expected = "43,443,434.00";
		Object actual = mFunction.$fnumber(inumber, format, decimal);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

}
