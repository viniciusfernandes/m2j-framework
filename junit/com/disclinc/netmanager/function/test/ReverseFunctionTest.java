package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class ReverseFunctionTest {
	@Test
	public void testReverseDouble() {

		Object charter = "1234";

		Object expected = "4321";
		Object actual = mFunction.$reverse(charter);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testReverseString() {

		Object charter = "TESTE";

		String expected = "ETSET";
		Object actual = mFunction.$reverse(charter);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}
}
