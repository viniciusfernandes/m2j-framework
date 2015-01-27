package com.disclinc.netmanager.variable.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import util.DataStructureUtil;
import br.com.innovatium.mumps2java.datastructure.Node;
import br.com.innovatium.mumps2java.datastructure.NodeInterval;
import br.com.innovatium.mumps2java.datastructure.Tree;

public class NodeIntervalTest {
	private NodeInterval interval = null;
	private final int PLUS = 3;
	private final int STEP = NodeInterval.STEP;
	private final int TOTAL = PLUS + STEP;
	private Tree tree = null;

	private Tree variables = null;

	public void createVariables() {
		variables = new Tree();
		for (int i = 1; i <= TOTAL; i++) {
			Object[] subs = new Object[] { i };
			variables.set(subs, "variavel_" + i);
		}
	}

	@Before
	public void init() {
		Object[] subs = new Object[] { "www001", "a" };
		tree = new Tree();
		tree.set(subs, "primeiro");
		Node node = tree.findNode(subs);

		// Estamos simulando o primeiro node do intervalo, apos isso vamos
		// acrescentar outros node em outros testes.
		interval = node.getInterval();

		Assert.assertEquals("O total de nodes do intervalo nao confere", 1, interval.size());
	}

	public void populateTree() {
		for (int i = 1; i <= TOTAL; i++) {
			Object[] subs = new Object[] { "www001", "b" + i };

			tree.set(subs, "testes" + i);
		}
	}

	@Test
	public void testAddAnotherLastNodeIntoTheInterval() {

		Object[] subs = new Object[] { "www001", "1" };
		Node node = new Node(subs, "testes", DataStructureUtil.generateKey(subs));

		Assert.assertTrue("This node is lower than the interval", interval.isBeforeBegin(node));
	}

	@Test
	public void testInclusaoDeNodeIdenticoAoLimiteInferior() {
		Object[] subs = new Object[] { "www001", "a" };
		tree.set(subs, "copia identica ao limite inferior");
		Assert
				.assertEquals(
						"O total de nodes do intervalo nao confere. Inserimos o mesmo node, portanto total de nodes do intervalo nao deve ser alterado.",
						1, interval.size());
	}

	@Test
	public void testResetInterval() {
		populateTree();

		Assert.assertEquals("O total de nodes do intervalo nao confere", STEP, interval.size());

		// Vamos remover o primeiro node para verificar o reset.
		tree.kill("www001", "a");

		// Deve existir um node a menos pois o removemos com o kill da arvore.
		Assert.assertEquals("O total de nodes do intervalo depois da remocao do node nao confere", STEP - 1,
				interval.size());

	}

	@Test
	public void testSizeInterval() {

		/*
		 * IMPORTANTE: ESTAMOS SUPONDO QUE O STEP SEJA MAIOR DO QUE 5 PARA ESSE
		 * TESTE
		 */
		Assert.assertEquals("This interval size after init does not match", 1, interval.size());

		Object[] subs = new Object[] { "www001", "b" };
		tree.set(subs, "teste b");
		Assert.assertEquals("This interval size after add a new last node does not match", 2, interval.size());

		subs = new Object[] { "www001", "c" };
		tree.set(subs, "teste c");
		Assert.assertEquals("This interval size after add a new last node does not match", 3, interval.size());

		// Inserindo um node fora da sequencia a, b, c
		subs = new Object[] { "www001", "a1" };
		tree.set(subs, "teste a1");
		Assert.assertEquals("This interval size after add a new last node does not match", 4, interval.size());

		// Inserindo um node no inicio da sequencia a, b, c
		subs = new Object[] { "www001", "1" };
		tree.set(subs, "teste a1");
		Assert.assertEquals("This interval size after add a new last node does not match", 5, interval.size());

		Assert.assertNull("This interval was not resized, so, must not have next", interval.getNext());
		Assert.assertNull("This interval was not resized, so, must not have previous", interval.getPrevious());
	}

	@Test
	public void testSplitInterval() {
		Assert.assertEquals("This interval size after init does not match", 1, interval.size());

		// Apos esse metodo teremos nove nodes na lista pois 1 node ja foi
		// inicializado no init.
		populateTree();

		// Aqui estamos verificando o split que ja foi executado pelos mecanismos
		// internos da arvore.
		Assert.assertEquals("This interval size after add a new last node does not match", STEP, interval.size());

		Assert.assertNotNull("This interval as splited and must to have one next interval", interval.getNext());

		// Aqui estamos testando o next interval que foi gerado apos o split do
		// intervalo gerenciado pela arvore resultando em um intervalo menor. Aqui
		// estamos levando em conta o node ja existente, portanto teremos um node a
		// mais no nextInterval.
		Assert.assertEquals("This interval as splited and must to have one next interval", TOTAL - STEP + 1, interval
				.getNext().size());

		Assert.assertNotNull("This interval as splited and must to have one previous interval", interval.getNext()
				.getPrevious());
		Assert.assertEquals("This interval size after add a new last node does not match", STEP, interval.getNext()
				.getPrevious().size());
	}

	@Test
	public void testStackEUnstack() {
		populateTree();

		// Pegando o intervalo dessa variavel
		NodeInterval intervalo = tree.findNode("www001").getInterval();

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 1,
				intervalo.size());

		// Acrescentando um node termos mais um node no intervalo
		tree.set(new Object[] { "www002", "b" }, "outra variavel");

		Assert
				.assertEquals(
						"O total de nodes no coincide, pois apos a inclusao na arvore de mais uma variavel teremos um acrescimo no total do intervalo",
						2, intervalo.size());

		tree.set(new Object[] { "www001" }, "teste");

		tree.stacking("www001");

		Assert.assertEquals(
				"O total de nodes no coincide, pois apos o stack de uma variavel teremos um decrescimo no total do intervalo",
				1, intervalo.size());

		tree.set(new Object[] { "www001" }, "segundos");

		tree.unstacking();
		Assert.assertEquals(
				"O total de nodes no coincide, pois apos o unstack devemos ter todos os nodes no intervalo novamente", 2,
				intervalo.size());

	}

	@Test
	public void testStackingSubnode() {

		tree.set(new Object[] { "www001", "b" }, "outra variavel");
		tree.set(new Object[] { "www001", "c" }, "outra variavel");

		NodeInterval intervaloWWW001 = tree.findNode("www001").getInterval();

		// Pegando o intervalo dessa variavel
		NodeInterval intervalo = tree.findNode("www001~a").getInterval();

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 1,
				intervaloWWW001.size());

		Assert.assertEquals("O total de nodes no coincide, pois foram incluidos outros nodes ao intervalo", 3,
				intervalo.size());

		// Adicionando outra variavel
		tree.set(new Object[] { "www002", "a" }, "outra variavel");

		Assert.assertEquals("O total de nodes no coincide, pois foi incluida outra variavel ao intervalo", 2,
				intervaloWWW001.size());

		tree.stacking("www001");

		Assert.assertEquals("O total de nodes no coincide, pois foi uma variavel foi removido do intervalo", 1,
				intervaloWWW001.size());

		Assert.assertEquals("O total de nodes no coincide, pois o intervalo nao foi alterado", 3, intervalo.size());

	}

	@Test
	public void testStackingVariables() {
		createVariables();
		NodeInterval intervaloVariaveis = variables.findNode("1").getInterval();

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", TOTAL - STEP,
				intervaloVariaveis.getNext().size());

		Assert.assertNull("Devemos ter apenas 2 intervalos de variaveis, portanto um next de next nao eh valido",
				intervaloVariaveis.getNext().getNext());

		// Vamos empilhar os nodes de extremos do segundo intervalo
		variables.stacking(STEP + 1);
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", TOTAL - STEP - 1,
				intervaloVariaveis.getNext().size());

		// Vamos empilhar os nodes de extremos do segundo intervalo
		variables.stacking(STEP + 2);
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", TOTAL - STEP - 2,
				intervaloVariaveis.getNext().size());

		// Vamos empilhar os nodes de extremos do segundo intervalo
		variables.stacking(STEP + 3);
		Assert
				.assertNull(
						"Como removemos todos os nodes do segundo intervalo, entao esse intervalo nao deve existir como next do primeiro intervalo",
						intervaloVariaveis.getNext());

		// Verificando se o primeiro node continua no mesmo estado
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		// Agora vamos iniciar o desempilhamento das variaveis e posteriormente
		// faremos os mesmos testes de integridade dos intervalos
		// EFETUANDO O PRIMEIRO UNSTACK
		variables.unstacking();
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 1,
				intervaloVariaveis.getNext().size());

		Assert.assertNull("Devemos ter apenas 2 intervalos de variaveis, portanto um next de next nao eh valido",
				intervaloVariaveis.getNext().getNext());

		// EFETUANDO O SEGUNDO UNSTACK
		variables.unstacking();
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 2,
				intervaloVariaveis.getNext().size());

		Assert.assertNull("Devemos ter apenas 2 intervalos de variaveis, portanto um next de next nao eh valido",
				intervaloVariaveis.getNext().getNext());

		// EFETUANDO O TERCEIRO UNSTACK
		variables.unstacking();
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 3,
				intervaloVariaveis.getNext().size());

		Assert.assertNull("Devemos ter apenas 2 intervalos de variaveis, portanto um next de next nao eh valido",
				intervaloVariaveis.getNext().getNext());

		// EFETUANDO O QUARTO E ARBITRARIO UNSTACK APENAS PARA TESTAR A CONSISTENCIA
		// DO MECANISMO
		variables.unstacking();
		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", STEP,
				intervaloVariaveis.size());

		Assert.assertEquals("O total de nodes no coincide, pois esse intervalo tem apenas uma variavel", 3,
				intervaloVariaveis.getNext().size());

		Assert.assertNull("Devemos ter apenas 2 intervalos de variaveis, portanto um next de next nao eh valido",
				intervaloVariaveis.getNext().getNext());

	}

	@Test
	public void testVerifyNodeIsHigherThanInterval() {

		Object[] subs = new Object[] { "www001", "x" };
		Node node = new Node(subs, "testes", DataStructureUtil.generateKey(subs));

		Assert.assertTrue("This node is higher than the interval", interval.isAfterEnd(node));
	}

	@Test
	public void testVerifyNodeIsLowerThanInterval() {

		Object[] subs = new Object[] { "www001", "1" };
		Node node = new Node(subs, "testes", DataStructureUtil.generateKey(subs));

		Assert.assertTrue("This node is lower than the interval", interval.isBeforeBegin(node));
	}
}
