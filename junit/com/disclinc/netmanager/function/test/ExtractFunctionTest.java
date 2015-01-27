package com.disclinc.netmanager.function.test;

import static mLibrary.mFunction.*;
import static org.junit.Assert.*;
import mLibrary.mContext;
import mLibrary.mVar;

import org.junit.Test;

public class ExtractFunctionTest {

	private mContext m$ = mContext.createSemAcesso();

	private final String stringTest = "1234Alabama567";

	@Test
	public void testEndIndexEqualNull() {
		String x = "xyz";
		assertEquals("Fail to extract string a non present element", "yz", $extract(x, 2, null));
	}

	@Test
	public void testEndIndexEqualValueLength() {
		String x = "x";
		assertEquals("Fail to extract string a non present element", "x", $extract(x, 1, 1));
	}

	@Test
	public void testEndIndexLargerThantValueLength() {
		String x = "x";
		assertEquals("Fail to extract string a non present element", "x", $extract(x, 1, 10));
	}

	@Test
	public void testExtractDefault() {
		assertEquals("Fail to extract string as default value", "1", $extract(stringTest));
	}

	@Test
	public void testExtractIntervalElement() {
		assertEquals("Fail to extract string as default value", "Alabama", $extract(stringTest, 5, 11));
	}

	@Test
	public void testExtractInvalidInterval() {
		assertEquals("Fail to extract string as default value", "", $extract(stringTest, 50, 11));
	}

	@Test
	public void testExtractNegativeStart() {
		assertEquals("Fail to extract string as default value", "1234Alabama", $extract(stringTest, -5, 11));
	}

	@Test
	public void testExtractNegativeStartAndEnd() {
		assertEquals("Fail to extract string as default value", "", $extract(stringTest, -5, -11));
	}

	@Test
	public void testExtractSomeElement() {
		assertEquals("Fail to extract string as default value", "A", $extract(stringTest, 5));
	}

	@Test
	public void testNonPresenteElement() {
		assertEquals("Fail to extract string a non present element", "", $extract(stringTest, 50));
	}

	@Test
	public void testNonPresenteElementWithNegativeIndex() {
		assertEquals("Fail to extract string a non present element", "", $extract(stringTest, -9));
	}

	@Test
	public void testSet$ExtractAtBegin() {
		mVar variable = m$.var("variable");
		variable.set("9blah");
		m$.extractVar(variable, 1).set("");
		assertEquals("Fail to set $extract string at the begining", "blah", variable.get());
	}

	@Test
	public void testSet$ExtractMiddle() {
		mVar variable = m$.var("variable");
		variable.set("blah9999blah");
		m$.extractVar(variable, 5, 8).set("");
		assertEquals("Fail to set $extract string at the begining", "blahblah", variable.get());
	}
}
