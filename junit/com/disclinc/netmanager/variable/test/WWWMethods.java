package com.disclinc.netmanager.variable.test;

import java.util.Arrays;

import mLibrary.mClass;
import mLibrary.mContext;

public class WWWMethods extends mClass {

	public WWWMethods(mContext m$) {
		super.init(m$);
	}

	public void executarRestoreVarBlock() {
		String antes = "222";
		m$.var("xxx").set(antes);

		m$.newVarBlock(1, m$.var("xxx"));

		m$.var("xxx").set("segundonew");

		m$.restoreVarBlock(1);

		if (!antes.equals(m$.var("xxx").getValue())) {
			throw new RuntimeException("Os valores dessa variavel " + Arrays.deepToString(m$.var("xxx").getSubs())
					+ " deveriam ser os mesmo apos o rsetore do bloco executado pelo dispath. Antes era \"" + antes
					+ "\" mas depois \"" + m$.var("xxx").getValue() + "\"");
		}
	}
}
