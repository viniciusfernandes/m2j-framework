package com.disclinc.netmanager.function.test;

import static mLibrary.mFunction.$concat;
import static mLibrary.mFunction.$list;
import static mLibrary.mFunction.$listbuild;
import static mLibrary.mFunction.list;
import static mLibrary.mFunction.listData;
import static mLibrary.mFunction.listGet;
import static org.junit.Assert.assertEquals;
import mLibrary.mListBuild;

import org.junit.Test;

public class ListBuildFunctionTest {
	@Test
	public void testCheckigListDataElementExistence() {
		mListBuild l1 = $listbuild("maca", null, "pera", "uva");
		assertEquals("Fail on checking list object existence. The element in this position is null.", 0, listData(l1, 2));

		mListBuild l2 = $listbuild("maca", "", "pera", "uva");
		assertEquals("Fail on checking list object existence. The element in this position is null.", 0, listData(l2, 2));

		mListBuild l3 = $listbuild("maca", "xxx", "pera", "uva");
		assertEquals("Fail on checking list object existence. The element in this position is not null.", 1,
				listData(l3, 2));
	}

	@Test
	public void testConcatenatingListObject() {
		mListBuild l1 = $listbuild("maca", "pera", "uva");
		mListBuild l2 = $listbuild("manga", "banana", "tomate");
		mListBuild l3 = $concat(l1, l2);

		assertEquals("Fail on concatening list object. Number elements is not matching.", 6, l3.length());
	}

	@Test
	public void testCreatingListObjectDefault() {
		assertEquals("Fail on creating list object. Number elements is not matching.", 3, $listbuild("maca", "pera", "uva")
				.length());
	}

	@Test
	public void testNestedListObject() {
		mListBuild list = $listbuild("maca", "pera", "uva", $listbuild("manga", "banana", "tomate"));
		assertEquals("Fail on creating nested list object. Number elements is not matching.", 4, list.length());
	}

	@Test
	public void testOneParameterSublistOfTheListObject() {
		mListBuild list = $listbuild("maca", "pera", "uva", "manga", "banana", "tomate");
		assertEquals("Fail on recovering list object element. The element is not matching.", "uva", list(list, 3));
	}

	@Test
	public void testRecoveringElementDefaultValueOfListObject() {
		mListBuild l1 = $listbuild("maca", null, "pera", "uva");
		assertEquals(
				"Fail on recovering default element od the list object because there is not element present in the position.",
				"DEFAULT", listGet(l1, 2, "DEFAULT"));

		assertEquals("Fail on recovering default element od the list object because there is present in the position.",
				"pera", listGet(l1, 3, "pera"));
	}

	@Test
	public void testRecoveringElementOfTheListObject() {
		mListBuild list = $listbuild("maca", "pera", "uva", "manga", "banana", "tomate");
		assertEquals("Fail on recovering list object element. The element is not matching.", "uva", list(list, 3));
	}

	@Test
	public void testRecoveringFirstElementOfTheListObject() {
		mListBuild list = $listbuild("maca", "pera", "uva", "manga", "banana", "tomate");
		assertEquals("Fail on recovering list object first element. The element is not matching.", "maca", list(list));
	}

	@Test
	public void testSublistOfTheListObject() {
		mListBuild list = $listbuild("maca", "pera", "uva", "manga", "banana", "tomate");
		assertEquals("Fail on creating sublist object. Number elements is not matching.", 3, $list(list, 3, 5).length());
	}

}
