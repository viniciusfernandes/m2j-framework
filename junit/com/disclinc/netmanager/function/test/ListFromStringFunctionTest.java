package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mLibrary.mFunction;
import mLibrary.mListBuild;

public class ListFromStringFunctionTest {

	@Test
	public void testListFromUrl() {

		Object string = "%18%01VARPacienteAutorizacao%1B%01YVARPacienteAutorizacaoP1%02%01%02%01%02%01";
		String mode = "I";
		String trantable = "URL";
		String actual = mFunction.$zconvert(string, mode, trantable).toString();

		assertEquals("Fail on convert with two arguments.", true, mListBuild.isList(actual));
	}

}
