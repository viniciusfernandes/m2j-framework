package com.disclinc.netmanager.variable.test;

public class GlobalVariableTest {
	/*
	 * private mData mGlobal = new mData(); private final String TABLE_NAME =
	 * "^www001"; public GlobalVariableTest() { mGlobal.kill(TABLE_NAME, "carro");
	 * 
	 * mGlobal.subs(TABLE_NAME, "carro", "carro").set(12);
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo").set(6);
	 * mGlobal.subs(TABLE_NAME, "carro", "passeio").set(8);
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "amarelo").set(2);
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "azul").set(4);
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "branco").set(14);
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "preto").set(4); }
	 * 
	 * @Test public void testSearchingVariableValue() {
	 * assertEquals("Fail on searching global variable value", "12",
	 * mGlobal.get(TABLE_NAME, "carro"));
	 * assertEquals("Fail on searching global variable value", "6",
	 * mGlobal.get(TABLE_NAME, "esportivo"));
	 * assertEquals("Fail on searching global variable value", "2",
	 * mGlobal.get(TABLE_NAME, "esportivo", "amarelo"));
	 * assertEquals("Fail on searching global variable value", "8",
	 * mGlobal.get(TABLE_NAME, "passeio")); }
	 * 
	 * @Test public void testOrderFirstLevel() {
	 * assertEquals("Fail on first navegation through the first level", "passeio",
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo").order(1));
	 * assertEquals("Fail on second navegation through the first level", null,
	 * mGlobal.subs(TABLE_NAME, "carro", "passeio").order(1));
	 * 
	 * assertEquals("Fail on first navegation through the first level", "azul",
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "amarelo").order(1));
	 * assertEquals("Fail on first navegation through the first level", "branco",
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "azul").order(1));
	 * assertEquals("Fail on first navegation through the first level", "preto",
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "branco").order(1));
	 * assertEquals("Fail on first navegation through the first level", null,
	 * mGlobal.subs(TABLE_NAME, "carro", "esportivo", "preto").order(1)); }
	 * 
	 * @Test public void testKillingKey() { mGlobal.subs("metal").set("6");
	 * assertEquals("Fail on persisting data. There is not data on database", "6",
	 * mGlobal.get("metal"));
	 * 
	 * mGlobal.subs("metal", "aluminio").set("43");
	 * assertEquals("Fail on persisting data. There is not data on database",
	 * "43", mGlobal.get("metal", "aluminio"));
	 * 
	 * mGlobal.kill("metal", "aluminio"); assertEquals(
	 * "Fail on remove data. There is data on database and it should be removed",
	 * null, mGlobal.get("metal", "aluminio"));
	 * 
	 * assertEquals(
	 * "Fail on remove data. This data was removed and should not be that", "6",
	 * mGlobal.get("metal"));
	 * 
	 * 
	 * }
	 */
}
