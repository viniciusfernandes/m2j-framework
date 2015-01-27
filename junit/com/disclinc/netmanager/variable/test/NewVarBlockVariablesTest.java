package com.disclinc.netmanager.variable.test;

import static org.junit.Assert.assertEquals;
import mLibrary.mContext;

import org.junit.Before;
import org.junit.Test;

public class NewVarBlockVariablesTest {
	private mContext m$ = null;

	@Before
	public void init() {
		m$ = mContext.createSemAcesso();
		m$.var("x").set(1);
		m$.var("x", "1").set(11);
		m$.var("y").set(2);
		m$.var("y", "1").set(21);
		m$.var("z").set(3);

		m$.var("ya").set(331);
		m$.var("y1").set(444);
		m$.var("y2", "1").set(122);
		m$.var("ybed").set(1234);
	}

	@Test
	public void testExecutionewVarBlockInsideAloop() {

		int firstBlock = 1;
		for (int i = 0; i < 2; i++) {
			m$.newVarBlock(firstBlock, m$.var("x"));
			// Checking first time execution newVarBlock operator
			assertEquals("This variable was removed through new var operator and must be remove from the tree", null,
					m$.var("x").getValue());
			assertEquals("This variable was removed through new var operator and must be remove from the tree", 2, m$
					.var("y").get());
			assertEquals("This variable was removed through new var operator and must be remove from the tree", 3, m$
					.var("z").get());
		}
	}

	@Test
	public void testExecutionNewVarBlockInARandomOrder() {
		int firsBlock = 1;
		int secondBlock = 2;
		System.out.println(m$.dumpLocal());
		// Setting new value to variable x which will be test into nested
		// loop
		m$.var("x").set(55);
		m$.var("w").set(66);
		System.out.println(m$.dumpLocal());
		for (int j = 0; j < 2; j++) {
			m$.newVarBlock(secondBlock, m$.var("w"));
			System.out.println(m$.dumpLocal());
			// Checking first time execution newVarBlock operator
			assertEquals("This variable was not removed through new var operator and must be into the tree", 55, m$
					.var("x").get());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 2,
					m$.var("y").get());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 3,
					m$.var("z").get());

			assertEquals("This variable was removed through new var operator and must not be into the tree", null, m$
					.var("w").getValue());

			// Setting some value to be test in next iteration.
			m$.var("w").set(j);
			System.out.println(m$.dumpLocal());
		}
		m$.restoreVarBlock(secondBlock);
		System.out.println(m$.dumpLocal());

		m$.restoreVarBlock(secondBlock);
		System.out.println(m$.dumpLocal());

		// Executing another newVarBlock called as firstBlock after the
		// secondBlock, because it could be generate by the mumps2java code
		// generator.
		m$.newVarBlock(firsBlock, m$.var("x"), m$.var("y"), m$.var("z"), m$.var("w"));

		System.out.println(m$.dumpLocal());

		assertEquals("This variable was removed through new var operator and must not be into the tree", null,
				m$.var("x").getValue());
		assertEquals("This variable was removed through new var operator and must not be into the tree", null,
				m$.var("y").getValue());
		assertEquals("This variable was removed through new var operator and must not be into the tree", null,
				m$.var("z").getValue());
		assertEquals("This variable was removed through new var operator and must not be into the tree", null,
				m$.var("w").getValue());

		// Setting a new value to variable x which will no be present into the
		// tree after restore variables.
		m$.var("x").set(888);

		// Restoring variables of the first block.
		m$.restoreVarBlock(firsBlock);
		assertEquals("This variable was not removed through new var operator and must be into the tree", 55, m$
				.var("x").get());
		assertEquals("This variable was not removed through new var operator and must be into the tree", 2, m$.var("y")
				.get());
		assertEquals("This variable was not removed through new var operator and must be into the tree", 3, m$.var("z")
				.get());

		assertEquals("This variable was removed through new var operator and must not be into the tree", 66, m$
				.var("w").get());

	}

	@Test
	public void testExecutionRestoreNewVarBlockAfterALoop() {

		// Executing newVarBlock into a loop
		for (int i = 0; i < 2; i++) {
			m$.newVarBlock(1, m$.var("x"));
			// Checking first time execution newVarBlock operator
			assertEquals("This variable was removed through new var operator and must be remove from the tree", null,
					m$.var("x").getValue());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 2,
					m$.var("y").get());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 3,
					m$.var("z").get());

		}

		m$.restoreVarBlock(1);

		m$.restoreVarBlock(1);
		// Verificando o valor das variaveis apos a execucao do empilhamento
		// efetuado no laco
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				1, m$.var("x").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				2, m$.var("y").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				3, m$.var("z").get());

	}

	@Test
	public void testExecutionRestorenewVarBlockAfterNestedLoops() {

		int firstBlock = 1;
		// Executing newVarBlock into a nested loop
		for (int i = 0; i < 2; i++) {
			m$.newVarBlock(firstBlock, m$.var("x"));
			// Checking first time execution newVarBlock operator
			assertEquals("This variable was removed through new var operator and must be remove from the tree", null,
					m$.var("x").getValue());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 2,
					m$.var("y").get());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 3,
					m$.var("z").get());

			// Setting new value to variable x which will be test into nested
			// loop
			m$.var("x").set(55);
			m$.var("w").set(66);
			for (int j = 0; j < 2; j++) {
				m$.newVarBlock(2, m$.var("w"));
				// Checking first time execution newVarBlock operator
				assertEquals("This variable was not removed through new var operator and must be into the tree", 55, m$
						.var("x").get());
				assertEquals("This variable was not removed through new var operator and must be into the tree", 2, m$
						.var("y").get());
				assertEquals("This variable was not removed through new var operator and must be into the tree", 3, m$
						.var("z").get());

				assertEquals("This variable was removed through new var operator and must not be into the tree", null,
						m$.var("w").getValue());
			}

			m$.restoreVarBlock(2);
			// Checking restores execution newVarBlock operator of the nested
			// loop.
			assertEquals("This variable was not removed through new var operator and must be into the tree", 55, m$
					.var("x").getValue());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 2,
					m$.var("y").getValue());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 3,
					m$.var("z").getValue());
			assertEquals("This variable was not removed through new var operator and must be into the tree", 66, m$
					.var("w").getValue());

		}

		m$.restoreVarBlock(firstBlock);
		// Checking restore variables after first time execution newVarBlock
		// operator
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				1, m$.var("x").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				2, m$.var("y").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				3, m$.var("z").get());

	}

	@Test
	public void testNewVarBlockAndRestoreVarBlockThroughWrongIndex() {

		m$.newVarBlock(1, m$.var("y"));

		m$.restoreVar(0);

		m$.var("y").set("teste");

		m$.newVar(m$.var("y"));

		m$.restoreVarBlock(1);

		m$.restoreVar(0);
	}

	/**
	 * 
	 */
	@Test
	public void testNewVarBlockChamadaRestoreSemTerChamadoNew() {
		// Essa variavel sera criada para testar o encadeamento entre varios
		// restoreVarBlocks feitos por metodos executados pelo dispatch.
		m$.var("a").set("a");
		m$.var("b").set("b");
		m$.var("c").set("c");

		m$.newVarBlock(1, m$.var("a"), m$.var("b"));

		assertEquals(null, m$.var("a").getValue());
		assertEquals(null, m$.var("b").getValue());
		assertEquals("c", m$.var("c").getValue());

		m$.var("a").set("a2");
		m$.var("b").set("b2");

		m$.newVarBlock(2, m$.var("a"), m$.var("b"));

		assertEquals(null, m$.var("a").getValue());
		assertEquals(null, m$.var("b").getValue());
		assertEquals("c", m$.var("c").getValue());

		m$.restoreVarBlock(3);

		assertEquals(null, m$.var("a").getValue());
		assertEquals(null, m$.var("b").getValue());
		assertEquals("c", m$.var("c").getValue());

		m$.restoreVarBlock(1);

		assertEquals("a", m$.var("a").getValue());
		assertEquals("b", m$.var("b").getValue());
		assertEquals("c", m$.var("c").getValue());
	}

	@Test
	public void testNewVarBlockComDipatch() {
		// Essa variavel sera criada para testar o encadeamento entre varios
		// restoreVarBlocks feitos por metodos executados pelo dispatch.
		m$.var("xxx").set("abcd");

		// Aqui faremos o new para testar o conflito entre o restore do dispatch
		m$.newVarBlock(1, m$.var("xxx"));

		m$.var("xxx").set("primeironew");

		WWWMethods obj = new WWWMethods(m$);

		m$.dispatch(false, obj, "com.disclinc.netmanager.variable.test.WWWMethods.executarRestoreVarBlock",
				(Object[]) null);
	}

	@Test
	public void testNewVarExceptsAndRestoreValues() {

		int firstBlock = 1;
		m$.newVarExceptBlock(firstBlock, m$.var("x"));

		assertEquals("This variable was not removed through new var except operator and must be into the tree", 1, m$
				.var("x").get());
		assertEquals("This variable was removed through new var except operator and must not be into the tree", null,
				m$.var("y").getValue());
		assertEquals("This variable was removed through new var except operator and must not be into the tree", null,
				m$.var("z").getValue());

		m$.restoreVarBlock(firstBlock);

		assertEquals("This variable was not removed through new var except operator and must be into the tree", 1, m$
				.var("x").get());
		assertEquals("This variable was restore through restore new var except operator and must be into the tree", 2,
				m$.var("y").get());
		assertEquals("This variable was restore through restore new var except operator and must be into the tree", 3,
				m$.var("z").get());
	}

	@Test
	public void testNewVarExceptsAndRestoreValuesIntoALoop() {

		for (int i = 0; i < 2; i++) {
			m$.newVarExceptBlock(1, m$.var("x"));

			assertEquals("This variable was not removed through new var except operator and must be into the tree", 1,
					m$.var("x").getValue());
			assertEquals("This variable was removed through new var except operator and must not be into the tree",
					null, m$.var("y").getValue());
			assertEquals("This variable was removed through new var except operator and must not be into the tree",
					null, m$.var("z").getValue());

		}

		// Recuperando as variaveis que foram empilhadas na iteracao do
		// laco anterior
		m$.restoreVarBlock(1);
		assertEquals("This variable was not removed through new var except operator and must be into the tree", 1, m$
				.var("x").getValue());
		assertEquals("This variable was restore through restore new var except operator and must be into the tree", 2,
				m$.var("y").getValue());
		assertEquals("This variable was restore through restore new var except operator and must be into the tree", 3,
				m$.var("z").getValue());
	}

	@Test
	public void testSimpleExecutingNewVariable() {

		int firstBlock = 1;
		m$.newVarBlock(firstBlock, m$.var("x"));
		// Checking first time execution newVarBlock operator
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("x").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", 2,
				m$.var("y").get());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", 3,
				m$.var("z").get());

		m$.newVarBlock(firstBlock, m$.var("x"), m$.var("y"));
		// Checking execution newVarBlock operator twice.
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("x").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("y").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", 3,
				m$.var("z").get());

		m$.newVarBlock(firstBlock, m$.var("x"), m$.var("y"), m$.var("z"));
		// Checking execution newVarBlock operator twice.
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("x").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("y").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("z").getValue());

	}

	@Test
	public void testSimpleExecutionRestoreNewVarBlock() {

		int firstBlock = 1;
		m$.newVarBlock(firstBlock, m$.var("x"), m$.var("y"), m$.var("z"));
		// Checking first time execution newVarBlock operator
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("x").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("y").getValue());
		assertEquals("This variable was removed through new var operator and must be remove from the tree", null, m$
				.var("z").getValue());

		m$.restoreVarBlock(firstBlock);
		// Checking restore variables after first time execution newVarBlock
		// operator
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				1, m$.var("x").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				2, m$.var("y").get());
		assertEquals(
				"This variable was restore through restore var operator and must be into the tree and its value have to be the same as before new var block execution",
				3, m$.var("z").get());

	}
}
