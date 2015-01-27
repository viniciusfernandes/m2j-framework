package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;

import org.junit.Test;

public class IsObjectTest {
	@Test
	public void testValidateIsObject() {
		Object object = "";
		Object actual = mFunction.$isobject(object);
		Object expected = 0;
		assertEquals("Fail to validate object. This is not a valid ", expected, actual);
	}

	@Test
	public void testValidateIsObjectNotNull() {
		Object object = "teste";
		Object actual = mFunction.$isobject(object);
		Object expected = 1;
		assertEquals("Fail to validate object. This is not a valid ", expected, actual);
	}

	@Test
	public void testValidateIsObjectNull() {
		Object object = null;
		Object actual = mFunction.$isobject(object);
		Object expected = 0;
		assertEquals("Fail to validate object. This is not a valid ", expected, actual);
	}
}
