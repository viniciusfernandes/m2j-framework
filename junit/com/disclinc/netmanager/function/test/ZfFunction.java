package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import mLibrary.mFunction;

import org.junit.Test;

public class ZfFunction {

	@Test
	public void testDeleteFileArgs() {

		mFunction m$ = new mFunction(null);
		Object actual = 0;
		m$.$zf(-1, "copy nul " + System.getProperty("java.io.tmpdir") + "teste.txt");
		m$.$zf(-1, "del " + System.getProperty("java.io.tmpdir") + "teste.txt");
		File file = new File(System.getProperty("java.io.tmpdir") + "teste.txt");
		Object expected = file.exists() ? 1 : 0;
		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

}
