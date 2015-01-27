package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mLibrary.mOperation;

import org.junit.Test;

public class BooleanOperatorTest {
	@Test
	public void testAndOperatorOverIntegers() {
		assertFalse(mOperation.And(0, 0));
		assertFalse(mOperation.And(1, 0));
		assertFalse(mOperation.And(0, 1));
		assertTrue(mOperation.And(1, 1));
	}

	@Test
	public void testAndOperatorOverStrings() {
		assertFalse(mOperation.And(0, "0"));
		assertFalse(mOperation.And("0", "0"));
		assertFalse(mOperation.And(1, "teste"));
		assertTrue(mOperation.And(1, "1teste"));
		assertFalse(mOperation.And(0, "teste"));
		assertTrue(mOperation.And("+1", "1"));
		assertTrue(mOperation.And("+1", "---+++1"));
	}

	@Test
	public void testOrOperatorOverIntegers() {
		assertFalse(mOperation.Or(0, 0));
		assertTrue(mOperation.Or(1, 0));
		assertTrue(mOperation.Or(0, 1));
		assertTrue(mOperation.Or(1, 1));
	}

	@Test
	public void testOrOperatorOverStrings() {
		assertFalse(mOperation.Or(0, "0"));
		assertFalse(mOperation.Or("0", "0"));
		assertTrue(mOperation.Or(1, "teste"));
		assertTrue(mOperation.Or(1, "1teste"));
		assertFalse(mOperation.Or(0, "teste"));
	}
}
