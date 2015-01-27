package com.disclinc.netmanager.function.test;

import static mLibrary.mFunction.$find;
import static org.junit.Assert.*;

import org.junit.Test;

public class FindFunctionTest {

	@Test
	public void testFindDefault() {
		assertEquals("Fail to find next position index of the substring", 5, $find("ABCDEFG", "BCD"));
	}

	@Test
	public void testFindLowestStartingIndex() {
		assertEquals("Fail to find next position index of the substring", 0, $find("EVERGREEN FOREST", "XXX", 0));
	}

	@Test
	public void testFindNotPresentSubstring() {
		assertEquals("Fail to find next position index of the substring", 0, $find("EVERGREEN FOREST", "XXX"));
	}

	@Test
	public void testFindStartingAtSomeIndex() {
		assertEquals("Fail to find next position index of the substring", 14, $find("EVERGREEN FOREST", "R", 7));
	}

	@Test
	public void testFindStartingIndexGreaterThanStringLength() {
		assertEquals("Fail to find next position index of the substring", 0, $find("EVERGREEN FOREST", "XXX", 88));
	}

}
