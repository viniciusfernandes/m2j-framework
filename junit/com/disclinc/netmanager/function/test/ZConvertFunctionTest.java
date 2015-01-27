package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mFunction;
import mLibrary.mListBuild;

import org.junit.Test;

public class ZConvertFunctionTest {

	@Test
	public void testUnescapeHtml() {

		Object string = "&#x25cf;";
		String mode = "I";
		String trantable = "HTML";
		String expected = "\u25cf";
		Object actual = mFunction.$zconvert(string, mode, trantable);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testUnescapeHtml2() {

		Object string = "&#xfffc;";
		String mode = "I";
		String trantable = "HTML";
		String expected = "\ufffc";
		Object actual = mFunction.$zconvert(string, mode, trantable);

		assertEquals("Fail on convert with two arguments.", expected, actual);
	}

	@Test
	public void testUnescapeUrlFromListBuild() {

		Object string = "%18%01VARPacienteAutorizacao%1B%01YVARPacienteAutorizacaoP1%02%01%02%01%02%01";
		String mode = "I";
		String trantable = "URL";
		int expected = 5;
		Object actualUrl = mFunction.$zconvert(string, mode, trantable);
		mListBuild lb = mListBuild.getList(actualUrl.toString());
		int actual = lb.length();

		assertEquals("Fail on convert with two arguments.", expected, actual);

		Object actualUrl2 = mFunction.$zconvert(lb, "O", trantable);

		assertEquals("Fail on convert with two arguments.", string, actualUrl2);

	}
}
