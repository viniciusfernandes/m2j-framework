package com.disclinc.netmanager.function.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mContext;
import mLibrary.mFunction;
import mLibrary.mVar;
import mLibrary.exceptions.UndefinedVariableException;
import mLibrary.exceptions.XecuteException;

import org.junit.Test;

public class XecuteTest {

	private mContext m$;

	public XecuteTest() {

	}

	@Test
	public void test$OrderWithSet() {
		m$ = mContext.createSemAcesso();
		m$.var("a", "1").set("teste1");
		m$.var("a", "2").set("teste2");
		m$.var("a", "3").set("teste3");
		m$.var("a", "4").set("teste4");
		// false post condition
		m$.Cmd.Xecute("set num = $order(a(1))");
		assertEquals("2", m$.var("num").getSuppressedNullValue());
	}

	@Test
	public void testDoSettingX() {
		m$ = mContext.createSemAcesso();

		mVar var = m$.var("var", "subscript1");
		var.set("default ");

		m$.Cmd.Xecute("do doMethod^TestmClass(var(\"subscript1\"))");

		assertEquals(var.get(), m$.var("x").get());

	}

	@Test
	public void testDoWithComments() {
		m$ = mContext.createSemAcesso();

		// false post condition
		m$.Cmd.Xecute("D ^TestmClass  ;Betrieb auswählen");

		assertEquals("modified", m$.var("a").get());

	}

	@Test(expected = XecuteException.class)
	public void testErrorBrackets() {
		m$ = mContext.createSemAcesso();
		m$.Cmd.Xecute("i (0) { set x=5 set y=2, z=x+y }");
	}

	@Test
	public void testFunction() {
		m$ = mContext.createSemAcesso();

		mVar var = m$.var("var");
		var.set(" world!");
		m$.Cmd.Xecute("set y=$$concat^TestmClass(\"hello\",var)");
		assertEquals("hello world!", m$.var("y").get());

	}

	@Test
	public void testFunctionByRef() {
		m$ = mContext.createSemAcesso();

		mVar var = m$.var("var");
		var.set(" world!");
		m$.Cmd.Xecute("set y=$$concatByRef^TestmClass(\"hello\",.var)");

		assertEquals("hello world!", m$.var("y").get());
		assertEquals("hello world!", var.get());

	}

	@Test
	public void testFunctionSuppressedArgument() {
		m$ = mContext.createSemAcesso();

		mVar var = m$.var("var");
		m$.Cmd.Xecute("set y=$$swapArguments^TestmClass(,.var)");

		assertEquals("default", var.get());

	}

	@Test
	public void testIf() {
		m$ = mContext.createSemAcesso();
		mVar z = m$.var("z");
		m$.Cmd.Xecute("i 1,2 set x=5 set y=2, z=x+y");
		assertEquals(7d, z.get());
	}

	@Test(expected = UndefinedVariableException.class)
	public void testIfFalseCondition() {
		m$ = mContext.createSemAcesso();

		mVar z = m$.var("z");
		m$.Cmd.Xecute("i (0) set x=5 set y=2, z=x+y");
		z.get();
	}

	@Test
	public void testIfFalseWithDo() {
		m$ = mContext.createSemAcesso();

		m$.var("a").set("notmodified");
		// m$.var("b").set("b value");

		// m$.Cmd.Xecute("do doMethod^TestmClass(.a) , doMethod^TestmClass(.b)");

		mVar YQUERY = m$.var("YQUERY");
		YQUERY.set("teste");
		m$.Cmd.Xecute("I $G(%(YQUERY,\"YCHART\"))'=\"\" DO doMethod^TestmClass(.a)");
		assertEquals("notmodified", m$.var("a").get());

	}

	@Test
	public void testIfTrueWithDo() {
		m$ = mContext.createSemAcesso();

		m$.var("a").set("notmodified");
		// m$.var("b").set("b value");

		// m$.Cmd.Xecute("do doMethod^TestmClass(.a) , doMethod^TestmClass(.b)");

		mVar YQUERY = m$.var("YQUERY");
		YQUERY.set("teste");
		mVar t = m$.var("%", YQUERY.get(), "YCHART");
		t.set("1");
		m$.Cmd.Xecute("I $G(%(YQUERY,\"YCHART\"))'=\"\" DO doMethod^TestmClass(.a)");
		assertEquals("modified", m$.var("a").get());

	}

	@Test
	public void testIfWithOutParenteses() {
		m$ = mContext.createSemAcesso();

		mVar YSEITE = m$.var("YSEITE");
		YSEITE.set(0);
		m$.Cmd.Xecute("if YSEITE = 0 SET YSEITE = 50");
		assertEquals("50", m$.var("YSEITE").get());

	}

	@Test
	public void testIfWithParenteses() {
		m$ = mContext.createSemAcesso();

		m$.var("a").set("notmodified");
		// m$.var("b").set("b value");

		// m$.Cmd.Xecute("do doMethod^TestmClass(.a) , doMethod^TestmClass(.b)");

		mVar YQUERY = m$.var("YPARA");
		YQUERY.set("");
		mVar t = m$.var("YAUSWAHL");
		t.set("1");
		m$.Cmd.Xecute("if (YPARA = \"\") set YPARA = YAUSWAHL");
		assertEquals("1", m$.var("YPARA").get());

	}

	@Test
	public void testIntrinsicFunction$Get() {
		m$ = mContext.createSemAcesso();

		mVar y = m$.var("y");
		m$.Cmd.Xecute("set y=$g(undefinedVar)");
		assertEquals("", y.get());

		m$.var("definedVar").set("1");
		m$.Cmd.Xecute("set y=$get(definedVar)");
		assertEquals("1", y.get());
	}

	@Test
	public void testIntrinsicFunction$Piece() {
		m$ = mContext.createSemAcesso();
		mVar z = m$.var("z");
		// 2 args
		m$.Cmd.Xecute("set x=\"a;b;c\", z=$Piece(x,\";\")");
		assertEquals("a", z.get());
		// 3 args
		m$.Cmd.Xecute("set x=\"d;e;f\", z=$Piece(x,\";\",2)");
		assertEquals("e", z.get());
	}

	@Test
	public void testSerialDo() {
		m$ = mContext.createSemAcesso();

		m$.var("a").set("a value");
		m$.var("b").set("b value");

		m$.Cmd.Xecute("do doMethod^TestmClass(.a) , doMethod^TestmClass(.b)");

		assertEquals("modified", m$.var("a").get());
		assertEquals("modified", m$.var("b").get());

	}

	@Test
	public void testSerialDoPostConditionalAndSet() {
		m$ = mContext.createSemAcesso();

		// false post condition
		m$.Cmd.Xecute("do:0 doMethod^TestmClass(.a) , doMethod^TestmClass(.b) set c=1");
		assertEquals("", m$.var("a").getSuppressedNullValue());
		assertEquals("", m$.var("b").getSuppressedNullValue());
		assertEquals("1", m$.var("c").get());

		// true post condition
		m$.Cmd.Xecute("do:$p(\" 2 3 4\",\" \",2) doMethod^TestmClass(.a) , doMethod^TestmClass(.b) set c=2");
		assertEquals("modified", m$.var("a").get());
		assertEquals("modified", m$.var("b").get());
		assertEquals("2", m$.var("c").get());

	}

	@Test
	public void testSet$h() {
		m$ = mContext.createSemAcesso();
		m$.var("YINHALT").set("teste1");

		// false post condition
		m$.Cmd.Xecute("S YINHALT=$h");
		assertEquals(mFunction.$piece(mFunction.$horolog(), ",", 1),
				mFunction.$piece(m$.var("YINHALT").getSuppressedNullValue(), ",", 1));
	}

	@Test
	public void testSetPiece() {

		m$ = mContext.createSemAcesso();
		m$.var("YINHALT", 0, 0, 1).set("teste1~teste2~teste3~teste4");
		m$.Cmd.Xecute("SET $P(YINHALT(0,0,1),\"~\",2)=\"testTwo\"");
		assertEquals("teste1~testTwo~teste3~teste4", m$.var("YINHALT", 0, 0, 1).getSuppressedNullValue());
	}

	@Test
	public void testSettingBinaryOperators() {
		m$ = mContext.createSemAcesso();

		m$.Cmd.Xecute("s s=\"abc;123\" + 3 ");
		assertEquals(3d, m$.var("s").get());

		mVar x = m$.var("x");
		m$.Cmd.Xecute("s:((1&&s)) x = 1");
		assertEquals("1", x.get());
	}

	@Test
	public void testSettingChained() {
		m$ = mContext.createSemAcesso();

		mVar z = m$.var("z");
		m$.Cmd.Xecute("set x=1,       y=2,z=x+y");
		assertEquals(3d, z.get());

		m$.Cmd.Xecute("set x=1         ,         y=2,z=x+y");
		assertEquals(3d, z.get());
	}

	@Test
	public void testSettingLogicalExpressions() {
		m$ = mContext.createSemAcesso();

		mVar or = m$.var("or");
		m$.Cmd.Xecute("s or=\"or (short circuit) sem parenteses.\"");
		m$.Cmd.Xecute("s result(or,1)= 1 & 10 || 0 && 0 ");
		assertEquals("0", m$.var("result", or.get(), 1).get());

		m$.Cmd.Xecute("s or=\"or com parenteses\"");
		m$.Cmd.Xecute("s result(or,1)= ( 1 && 10 ) ! ( 0 & 0 ) ");
		assertEquals("1", m$.var("result").s$(or, 1).get());

	}

	@Test
	public void testSettingPostConditional() {
		m$ = mContext.createSemAcesso();

		m$.Cmd.Xecute("set:1 s=+\"abc;123\" ");
		assertEquals(0d, m$.var("s").get());

		mVar x = m$.var("x");
		m$.Cmd.Xecute("s:(1) x = 1");
		assertEquals("1", x.get());
	}

	@Test
	public void testSettingSerial() {
		m$ = mContext.createSemAcesso();

		mVar z = m$.var("z");
		m$.Cmd.Xecute("set x=5 set y=2, z=x+y");
		assertEquals(7d, z.get());

	}

	@Test
	public void testSettingUnaryOperators() {
		m$ = mContext.createSemAcesso();

		m$.Cmd.Xecute("set s='1 ");
		assertEquals("0", m$.var("s").get());

		m$.Cmd.Xecute("set s=''1 ");
		assertEquals("0", m$.var("s").get());
	}

	@Test
	public void testSettingWithSemiCollon() {
		m$ = mContext.createSemAcesso();

		mVar x = m$.var("x");
		m$.Cmd.Xecute("set x = 1 ; x = 0");
		assertEquals("1", x.get());

		m$.Cmd.Xecute("set z=\"abc;123\" ; comentario proposital");
		assertEquals("abc;123", m$.var("z").get());
	}

	@Test
	public void testSetWithClass() {

		m$ = mContext.createSemAcesso();
		m$.var("YINHALT").set("teste1");
		m$.Cmd.Xecute("S YINHALT = $$concat^TestmClass(##class(TestmClass).concat(123,456),789)");
		assertEquals("123456789", m$.var("YINHALT").getSuppressedNullValue());
	}

	@Test
	public void testSetWithSubscript() {
		m$ = mContext.createSemAcesso();
		m$.Cmd.Xecute("SET %TXT(1)=$$returnArguments^TestmClass(\"hello\",.var)");
		assertEquals("hello world2!", m$.var("%TXT", 1).get());
	}

	@Test
	public void testWrite() {
		// w $$ExecuteString^WWWTransaction(YKEY,"ExecuteAfterPrimaryKey",1)
		m$ = mContext.createSemAcesso();

		m$.Cmd.Open("c:/temp/text.txt", "wns", 1);
		m$.Cmd.Use("c:/temp/text.txt");
		m$.Cmd.Xecute("w $$returnArguments^TestmClass(1)");
		m$.Cmd.Xecute("w $$returnArguments^TestmClass(1)");
		m$.Cmd.Close("c:/temp/text.txt");
		assertEquals("hello world2!", m$.var("$zwrite").get());
	}

	@Test
	public void testWriteTwoArgs() {
		// w $$ExecuteString^WWWTransaction(YKEY,"ExecuteAfterPrimaryKey",1)
		m$ = mContext.createSemAcesso();
		m$.Cmd.Open("c:/temp/text.txt", "wns", 1);
		m$.Cmd.Use("c:/temp/text.txt");
		m$.Cmd.Xecute("write \"Max = \",$$returnArguments^TestmClass(1)");
		m$.Cmd.Close("c:/temp/text.txt");
		assertEquals("1", m$.var("$zwrite").get());
	}
	/**
	 * Parser p = new Parser(m,"do doMethod^TestmClass(var)");
	 * 
	 * //Parser p = new Parser(m,
	 * "set y=$$concat^TestmClass(\"hello\",\" World\")"); //Parser p = new
	 * Parser(m,"set y=$$swapArguments^TestmClass(,.var)"); p.parse();
	 * System.out.println(m.var("x").get());
	 */
	/*
	 * @Test public void testSpecialVariables(){ m$ = mContext.createSemAcesso(); //Special
	 * Variables are not implemented. mVar y = m$.var("y");
	 * m$.Cmd.Xecute("set y=$io"); assertEquals("", y.get() );
	 * 
	 * }
	 */
}
