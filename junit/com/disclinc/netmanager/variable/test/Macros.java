package com.disclinc.netmanager.variable.test;

import mLibrary.mContext;

/**
 * This class was created to be employed in dispatch functions test.
 * 
 * @author vinicius
 * 
 */
public final class Macros {
	public static Object $$$alterarValorVariavel(mContext m$) {
		m$.var("pedido").set(99);
		return m$.var("pedido").getValue();
	}

	public static int $$$diasNoMes() {
		return 30;
	}

	public static void $$$operadorNewChamada1(mContext m$) {
		m$.newVar(m$.var("pedido"));
		m$.var("pedido").set("chamada1");

	}

	public static void $$$operadorNewChamada2(mContext m$) {
		m$.newVar(m$.var("pedido"));
		m$.var("pedido").set("chamada2");
	}

	public static void $$$operadorNewChamada3(mContext m$) {
		m$.newVar(m$.var("pedido"));
		m$.var("pedido").set("chamada3");
	}

	public static Object $$$recuperarValorInalteradoDepoisDoOperadorNew(mContext m$) {
		m$.newVar(m$.var("pedido"));
		return m$.var("entrega").getValue();
	}

	public static Object $$$recuperarValorNovoDepois3ChamadasDoOperadorNew(mContext m$) {
		$$$operadorNewChamada1(m$);
		$$$operadorNewChamada2(m$);
		$$$operadorNewChamada3(m$);
		return m$.var("pedido").get();
	}

	public static Object $$$recuperarValorNuloDepoisDoOperadorNew(mContext m$) {
		m$.newVar(m$.var("pedido"));
		return m$.var("pedido").getValue();
	}
}
