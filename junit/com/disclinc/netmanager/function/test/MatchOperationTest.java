package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mLibrary.mOperation;

import org.junit.Test;

public class MatchOperationTest {

	@Test
	public void testMatch1() {

		Object expression = "63480,57166";
		Object pattern = "2A";

		assertFalse("Fail matche pattern.", mOperation.Match(expression, pattern));
		
		expression = "ss";
		assertTrue("Fail matche pattern.", mOperation.Match(expression, pattern));
	}

	@Test
	public void testMatch2() {
		// pstrDate?2A
		Object expression = "1230-98";
		Object pattern = "0.1(4N1\"-\"6N1\"/\"4N)";

		assertFalse("Fail matche pattern.", mOperation.Match(expression, pattern));
	}

}
