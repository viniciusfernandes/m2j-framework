package com.disclinc.netmanager.variable.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import br.com.innovatium.mumps2java.datastructure.Tree;

public class TreeTest {
	private Tree tree;
	Object[] _10 = new Object[] { "10" };
	Object[] carro = new Object[] { "carro" };
	Object[] contrato = new Object[] { "contrato" };
	Object[] empty = new Object[] { "" };
	Object[] x = new Object[] { "x" };
	Object[] y = new Object[] { "y" };
	Object[] z = new Object[] { "z" };

	@Before
	public void init() {

		tree = new Tree();
		tree.set(new Object[] { "1" }, 1);
		tree.set(new Object[] { "10" }, 10);
		tree.set(new Object[] { "2" }, 2);
		tree.set(new Object[] { "carro" }, 65);
		tree.set(new Object[] { "carro", "esportivo" }, 99);
		tree.set(new Object[] { "carro", "esportivo", "amarelo" }, 76);
		tree.set(new Object[] { "contrato" }, 66);
		tree.set(new Object[] { "contrato", "transporte" }, 123);
		tree.set(new Object[] { "contrato", "transporte", "publico" }, 88);
		tree.set(new Object[] { "contrato", "uu", "publico" }, 62);
		tree.set(new Object[] { "", "teste" }, 88);
		tree.set(new Object[] { "" }, "sou vazio");
		tree.set(x, 1);
		tree.set(y, 2);
		tree.set(z, 3);

	}

	@Test
	public void testFindPrevious() {

		testWithContrato();

		// Remove contrato to test previous without it
		tree.kill(contrato);

		testWithouContrato();

		// Including into the tree again
		tree.set(contrato, "novo valor");

		tree.stacking(contrato);

		testWithouContrato();

		tree.set(contrato, "contrato stack 1");

		testWithContrato();

		// Checking the new value defined.
		assertEquals("contrato stack 1", tree.get(contrato));

		tree.stackingBlock(1, contrato);
		testWithouContrato();
		// Here the contrato must be undefined
		assertEquals(null, tree.get(contrato));

		// Setting new value
		tree.set(contrato, "contrato block 1");

		tree.stackingBlock(2, contrato);
		testWithouContrato();
		// Here the contrato must be undefined again
		assertEquals(null, tree.get(contrato));

		tree.set(contrato, "contrato block 2");
		testWithContrato();
		// Checking the contrato value of block 2
		assertEquals("contrato block 2", tree.get(contrato));

		// Unstacking contrato from the block 2
		tree.unstackingBlock(2);
		testWithContrato();
		// Checking the contrato value of block 1 was done before block 2
		assertEquals("contrato block 1", tree.get(contrato));

		// Unstacking contrato from the block 1
		tree.unstackingBlock(1);
		testWithContrato();
		// Checking the contrato value of stack 1
		assertEquals("contrato stack 1", tree.get(contrato));

		// Checking the contrato value before stack 1
		tree.unstacking();
		testWithContrato();
		// Checking the contrato value before stack 1
		assertEquals("novo valor", tree.get(contrato));

		// Unstacking some undefined block index and before this action all
		// variable values must remain the same
		tree.unstackingBlock(4);
		testWithContrato();
		// Checking the contrato value before stack 1
		assertEquals("novo valor", tree.get(contrato));
		assertEquals(65, tree.get(carro));
	}

	@Test
	public void testRecoveringVariable() {
		assertEquals(65, tree.get(new Object[] { "carro" }));
		assertEquals(99, tree.get(new Object[] { "carro", "esportivo" }));
		assertEquals(76, tree.get(new Object[] { "carro", "esportivo", "amarelo" }));

	}

	@Test
	public void testRemoveNodes() {
		tree.kill("carro");
		assertEquals("The node should not be into the tree after killing node method", null, tree.get("carro"));
		assertEquals("The node should not be into the tree after killing node method", null, tree.get("carro", "esportivo"));
		assertEquals("The node should not be into the tree after killing node method", null,
				tree.get("carro", "esportivo", "amarelo"));
		assertEquals("The node should be into the tree because he was not removed from the tree", 66, tree.get("contrato"));
		assertEquals("The node should be into the tree because he was not removed from the tree", 123,
				tree.get("contrato", "transporte"));
		assertEquals("The node should be into the tree because he was not removed from the tree", 88,
				tree.get("contrato", "transporte", "publico"));
	}

	@Test
	public void testStackingFirsNodeOfTheTree() {
		// stacking the first variable of the tree
		tree.stacking("1");
		assertEquals("2", tree.order(new Object[] { "" }));

		tree.set(new Object[] { "1" }, "valor na pilha");

		assertEquals("1", tree.order(new Object[] { "" }));
		assertEquals("2", tree.order(new Object[] { "1" }));

		// removing the first stacked variable from the stack
		tree.unstacking();
		assertTrue(
				"The number of the variables in the first level of the tree and the variables setted in the keyvalue map does not the same. After unstacking the tree must contain the same variables number.",
				tree.isNumberVariablesCoherent());

		assertEquals(1, tree.get("1"));
		assertEquals("1", tree.order(new Object[] { "" }));
		assertEquals("2", tree.order(new Object[] { "1" }));
	}

	@Test
	public void testStackingLastNodeOfTheTree() {
		// stacking the first variable of the tree
		tree.stacking("z");
		assertEquals(null, tree.get(new Object[] { "z" }));

		tree.set(new Object[] { "z" }, "valor na pilha");

		// verificando se os dois ultimos nodes estao amarrados
		assertEquals("z", tree.order(new Object[] { "y" }));
		assertEquals("y", tree.order(new Object[] { "z" }, -1));

		// removing the first stacked variable from the stack
		tree.unstacking();
		assertTrue(
				"The number of the variables in the first level of the tree and the variables setted in the keyvalue map does not the same. After unstacking the tree must contain the same variables number.",
				tree.isNumberVariablesCoherent());

		assertEquals(1, tree.get("1"));
		assertEquals("1", tree.order(new Object[] { "" }));
		assertEquals("2", tree.order(new Object[] { "1" }));
	}

	private void testWithContrato() {
		assertTrue(Arrays.equals(contrato, tree.findNext(carro)));
		assertTrue(Arrays.equals(x, tree.findNext(contrato)));
		assertTrue(Arrays.equals(y, tree.findNext(x)));
		assertTrue(Arrays.equals(z, tree.findNext(y)));
		assertTrue(Arrays.equals(null, tree.findNext(z)));

		assertTrue(Arrays.equals(empty, tree.findPrevious(carro)));
		assertTrue(Arrays.equals(carro, tree.findPrevious(contrato)));
		assertTrue(Arrays.equals(contrato, tree.findPrevious(x)));
		assertTrue(Arrays.equals(x, tree.findPrevious(y)));
		assertTrue(Arrays.equals(y, tree.findPrevious(z)));

	}

	private void testWithouContrato() {
		assertTrue(Arrays.equals(x, tree.findNext(carro)));
		assertTrue(Arrays.equals(y, tree.findNext(x)));
		assertTrue(Arrays.equals(z, tree.findNext(y)));
		assertTrue(Arrays.equals(null, tree.findNext(z)));

		assertTrue(Arrays.equals(empty, tree.findPrevious(carro)));
		assertTrue(Arrays.equals(carro, tree.findPrevious(x)));
		assertTrue(Arrays.equals(x, tree.findPrevious(y)));
		assertTrue(Arrays.equals(y, tree.findPrevious(z)));
	}
}
