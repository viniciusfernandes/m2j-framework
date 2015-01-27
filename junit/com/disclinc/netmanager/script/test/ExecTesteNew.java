package com.disclinc.netmanager.script.test;

import java.io.StringWriter;

import mLibrary.mContext;

public class ExecTesteNew {

	public static void main(String[] args) {
		TesteNew teste = new TesteNew();
		teste.init(mContext.createComAcessoABancoSQL(new StringWriter()));
		teste.main();
	}

}
