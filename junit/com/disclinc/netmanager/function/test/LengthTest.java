package com.disclinc.netmanager.function.test;

import mLibrary.mFunction;
import mLibrary.exceptions.UndefinedVariableException;

import org.junit.Test;

import static org.junit.Assert.*;

public class LengthTest {

	private final String DELIMITER = "~";

	@Test
	public void testLengthWithEmptyString() {
		assertEquals(0, mFunction.$length(""));
		assertEquals(1, mFunction.$length("", DELIMITER));
	}

	@Test
	public void testLengthWithFirstCharAsDelimiter() {
		String x = "~a~b~c~";
		assertEquals(5, mFunction.$length(x, DELIMITER));
	}

	@Test
	public void testLengthWithLastCharAsDelimiter() {
		String x = "a~b~c~";
		assertEquals(4, mFunction.$length(x, DELIMITER));
	}

	@Test
	public void testLengthWithNullValue() {
		boolean throwed = false;
		try {
			mFunction.$length(null, DELIMITER);
		}
		catch (UndefinedVariableException e) {
			throwed = true;
		}
		assertTrue("Null values are not allowed to function length", throwed);

		throwed = false;
		try {
			mFunction.$length(null);
		}
		catch (UndefinedVariableException e) {
			throwed = true;
		}
		assertTrue("Null values are not allowed to function length", throwed);
	}

	@Test
	public void testSimpleStringLengthWithDelimiter() {
		String x = "a~b~c";
		assertEquals(3, mFunction.$length(x, DELIMITER));
	}

	@Test
	public void testSimpleStringLengthWithoutDelimiter() {
		String x = "teste";
		assertEquals(5, mFunction.$length(x));
	}
}
