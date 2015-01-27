package com.disclinc.netmanager.variable.test;

import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Test;

import util.DataStructureUtil;

public class DataStructureUtilTest {
	private Object[] end = new Object[] { 5, 6, 7, 8, 9, 0 };
	private Object[] query = new Object[] { "^www001", "x", "1", "2", "3" };
	private Object[] start = new Object[] { 1, 2, 3, 4 };

	private final String STRING_DEMILITER = "||";
	private final int STRING_MAXSIZE = 15;

	@Test
	public void testConcat() {
		Object[] concat = DataStructureUtil.concat(start, end);
		Object[] result = new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		assertTrue("All elements must have the same value", Arrays.equals(result, concat));

	}

	@Test
	public void testConcatLastElement() {
		Object[] concat = DataStructureUtil.concat(start, 88);
		Object[] result = new Object[] { 1, 2, 3, 4, 88 };
		assertTrue("All elements must have the same value", Arrays.equals(result, concat));
	}

	@Test
	public void testConcatSinceLastElement() {
		Object[] concat = DataStructureUtil.concatSinceLast(start, end);
		Object[] result = new Object[] { 1, 2, 3, 5, 6, 7, 8, 9, 0 };
		assertTrue("All elements must have the same value", Arrays.equals(result, concat));
	}

	@Test
	public void testGenerateKeyOfParentSubscripts() {
		assertEquals("^www001~x~1~2", DataStructureUtil.generateKeyOfParent(query));
	}

	@Test
	public void testGenerateKeyOfTheSubscripts() {
		assertEquals("^www001~x~1~2~3", DataStructureUtil.generateKey(query));
	}

	@Test
	public void testGenerateKeyToLikeQuery() {
		assertEquals("x~1~2~", DataStructureUtil.generateKeyToLikeQuery(query));
	}

	@Test
	public void testGenerationDelimitedNullString() {
		String string = null;
		assertEquals(null, DataStructureUtil.generateStringValueWithSQLConcat(STRING_MAXSIZE, string, STRING_DEMILITER));
	}

	@Test
	public void testGenerationDelimitedOfASmallString() {
		String string = "string pequena";
		assertEquals("It is a string smaller than max size, so we must have the same value", string,
				DataStructureUtil.generateStringValueWithSQLConcat(STRING_MAXSIZE, string, STRING_DEMILITER));
	}

	@Test
	public void testGenerationDelimitedString() {
		String string = "string excedendo o valor limite";
		String result = "to_clob('')||'string excedend'" + STRING_DEMILITER + "'o o valor limit'" + STRING_DEMILITER + "'e'";
		assertEquals(result, DataStructureUtil.generateStringValueWithSQLConcat(STRING_MAXSIZE, string, STRING_DEMILITER));
	}
}
