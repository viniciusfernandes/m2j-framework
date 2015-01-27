package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.m$op;
import mLibrary.mContext;
import mLibrary.mFunction;
import mLibrary.mVar;

import org.junit.Test;

public class TranslateFunctionTest {
	@Test
	public void testJustifyThreeArgs() {

		Object string = "06-23-1993";
		Object oldCharsequenceA = "19";
		String newCharsequenceA = "9";

		Object actualA = mFunction.$translate(string, oldCharsequenceA, newCharsequenceA);

		Object oldCharsequence = "-";
		String newCharsequence = "/";

		String expected = "06/23/93";
		Object actual = mFunction.$translate(actualA, oldCharsequence, newCharsequence);

		assertEquals("Fail on translate with Three arguments.", expected, actual);
	}

	@Test
	public void testJustifyTimeFormat() {
		mContext m$ = mContext.createSemAcesso();
		mVar TRENN = m$.var("TRENN");
		TRENN.set(":");
		mFunction m$fn = m$.getFunction();
		mVar YA = m$.var("YA");
		YA.set("63006");
		mVar YSEC = m$.var("YSEC");
		YSEC.set(m$op.Modulus(YA.get(), 60));
		YA.set(m$op.IntegerDivide(YA.get(), 60));
		// <<
		// << ;IF $GET(SPRACHE)="DE" QUIT
		// $TRANSLATE($JUSTIFY(YA\60,2)_TRENN_$JUSTIFY(YA#60,2)_TRENN_$JUSTIFY(YSEC,2)," ","0")
		// ;SR13729
		// << QUIT
		// $TRANSLATE($JUSTIFY(YA\60,2)_TRENN_$JUSTIFY(YA#60,2)_TRENN_$JUSTIFY(YSEC,2)," ","0")
		Object actual = m$fn.$translate(
				m$op.Concat(m$fn.$justify(m$op.IntegerDivide(YA.get(), 60), 2), TRENN.get(),
						m$fn.$justify(m$op.Modulus(YA.get(), 60), 2), TRENN.get(), m$fn.$justify(YSEC.get(), 2)), " ", "0");

		Object expected = "17:30:06";
		assertEquals("Fail on translate with Three arguments.", expected, actual);
	}

	@Test
	public void testJustifyTwoArgs() {

		Object string = "1,462,543";
		Object oldCharsequence = ",";

		String expected = "1462543";
		Object actual = mFunction.$translate(string, oldCharsequence);

		assertEquals("Fail on translate with two arguments.", expected, actual);
	}
}
