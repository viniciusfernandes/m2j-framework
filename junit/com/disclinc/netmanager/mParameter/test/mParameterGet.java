package com.disclinc.netmanager.mParameter.test;

import static org.junit.Assert.*;
import mLibrary.mContext;

import org.junit.Test;

import com.disclinc.netmanager.mParameter.test.mClassChild;

public class mParameterGet {
	private mContext m$ = mContext.createSemAcesso();

	@Test
	public void testGetParameter() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException {
		mClassChild t = new mClassChild();
		assertEquals(m$.param(t, "p_LOGINSUBMIT").get(), "COMUserPreferences.cls");
	}
}
