package com.disclinc.netmanager.function.test;

import mLibrary.mListBuild;
import static mLibrary.mFunction.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ListLengthFunctionTest {
	@Test
	public void testEmptyListLength() {
		mListBuild list = $listbuild("");
		assertEquals(0, $listlength(list));
	}

	@Test
	public void testNestedListLength() {
		mListBuild list = $listbuild("x", "y", "z");
		list = $listbuild(list, "w", "z");
		assertEquals(3, $listlength(list));
	}

	@Test
	public void testNullListLength() {
		mListBuild list = $listbuild((Object[]) null);
		assertEquals(0, $listlength(list));
	}

	@Test
	public void testSimpleLength() {
		mListBuild list = $listbuild("x", "y", "z");
		assertEquals(3, $listlength(list));
	}
}
