package com.disclinc.netmanager.function.test;

import mLibrary.mListBuild;
import static mLibrary.mFunction.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ListFindFunctionTest {
	private final mListBuild list = $listbuild("a", "x", "y", "z", "a");

	@Test
	public void testFindElementNotFoundInList() {
		assertEquals("This element is not present into the list and must return zero", 0, $listfind(list, "w"));
	}

	@Test
	public void testFindElementWithStartAfterIndex() {
		assertEquals("This element is not present into the list and must return zero", 5, $listfind(list, "a", 1));

		assertEquals("This index is larger than list size and must return zero", 0, $listfind(list, "a", 10));

		assertEquals(3, $listfind(list, "y", 1));
		assertEquals(3, $listfind(list, "y", 2));
		assertEquals("This element is not present into the list after the chosen index", 0, $listfind(list, "y", 3));
		assertEquals("Negative index must returns zero always", 0, $listfind(list, "a", -1));

	}

	@Test
	public void testIndexAsString() {
		assertEquals(3, $listfind(list, "y", "2"));
		assertEquals(3, $listfind(list, "y", "2x"));
		assertEquals(1, $listfind(list, "a", "x2"));
		assertEquals(1, $listfind(list, "a", "   x2"));
	}

	@Test
	public void testNegativeIndexLessThanMinusOne() {
		boolean throwed = false;
		try {
			$listfind(list, "a", -2);
		}
		catch (IllegalArgumentException e) {
			throwed = true;
		}
		assertTrue("Negative index less thah -1 is not allowed", throwed);
	}

	@Test
	public void testSimpleListFind() {
		assertEquals(1, $listfind(list, "a"));
		assertEquals(2, $listfind(list, "x"));
		assertEquals(3, $listfind(list, "y"));
		assertEquals(4, $listfind(list, "z"));
		assertEquals(1, $listfind(list, "a"));
	}
}
