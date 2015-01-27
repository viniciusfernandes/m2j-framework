package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class AsciiFunctionTest {

	@Test
	public void testAsciiOneArgs() {

		Object expression = "W";

		Integer expected = 87;
		Object actual = mFunction.$ascii(expression);

		assertEquals("Fail on convert with one arguments.", expected, actual);
	}

	@Test
	public void testAsciiTwoArgs() {

		Object expression = "TEST";
		Object position = "3";

		Integer expected = 83;
		Object actual = mFunction.$ascii(expression, position);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

}
