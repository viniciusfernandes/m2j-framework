package br.com.innovatium.mumps2java.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.innovatium.mumps2java.todo.TODO;

public final class Tree extends Node {

	private final class AddOnTreeOperationOverNodes implements OperationOverNodes {

		public void operate(Node node) {
			KeyValue.put(node.getKey(), node);
		}

	}

	private final class DumpOperationOverNodes implements OperationOverNodes {
		private StringBuilder dump;

		public DumpOperationOverNodes(StringBuilder dump) {
			this.dump = dump;
		}

		public void operate(Node node) {
			dump.append(node).append("\n");
		}
	}

	private final class KillOperationOverNodes implements OperationOverNodes {

		public void operate(Node node) {
			KeyValue.remove(node.getKey());
		}

	}

	private final class MergeOperationOverNodes implements OperationOverNodes {
		private Object[] dest;
		private Object[] orig;
		private final Tree tree;

		public MergeOperationOverNodes(Tree tree) {
			this.tree = tree;
		}

		public void operate(Node node) {
			Object[] concatSubs = null;
			Object subnodeValue = node.getValue();
			concatSubs = util.DataStructureUtil.concat(dest, node.getSubs(orig.length));
			Node destNode = findNode(concatSubs);
			if (destNode == null) {
				tree.set(concatSubs, subnodeValue);
			} else if (subnodeValue != null) {
				destNode.setValue(subnodeValue);
			}
		}

		public void set(Object[] dest, Object[] orig) {
			this.dest = dest;
			this.orig = orig;
		}
	}

	/**
	 * This class was created to reuse subnodes searching method. Dumping the
	 * tree and kill subnodes methods have the same implementation.
	 * 
	 * @author vinicius
	 * 
	 */
	private interface OperationOverNodes {
		void operate(Node node);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7539536412431829452L;

	private static final int MAP_INITIAL_CAPACITY = 2048;
	private AddOnTreeOperationOverNodes addSubnodesOperation = new AddOnTreeOperationOverNodes();

	private int currentStackLevel = 0;

	private EnhancedMap KeyValue;

	private KillOperationOverNodes killSubnodesOperation = new KillOperationOverNodes();

	private MergeOperationOverNodes mergeSubnodesOperation = new MergeOperationOverNodes(this);

	private final StackVariables stackBlockVariables = new StackBlockVariables();

	private final StackVariables stackVariables = new StackVariables();

	public Tree() {
		// We adopted this subscript to the tree because no one mumps variable
		// can be declared about this symbol.
		super(new Object[] { "@" }, null, "@");

		KeyValue = new EnhancedMap(MAP_INITIAL_CAPACITY);
	}

	private void addAllSubnodes(Node node) {
		operateOverSubnodes(node, addSubnodesOperation);
	}

	public boolean contains(Object[] subs) {
		Node node = findNode(subs);
		return node != null && node.getValue() != null;
	}

	public int data(Object[] subs) {
		Node node = findNode(subs);
		if (node == null) {
			return 0;
		}
		int cod = -1;
		boolean hasValue = node.getValue() != null;
		boolean hasChildPopulated = hasPopulatedSubnode(node);
		if (!hasValue && !hasChildPopulated) {
			cod = 0;
		} else if (hasValue && !hasChildPopulated) {
			cod = 1;
		} else if (!hasValue && hasChildPopulated) {
			cod = 10;
		} else if (hasValue && hasChildPopulated) {
			cod = 11;
		}
		return cod;
	}

	public String dump() {
		StringBuilder string = new StringBuilder();
		operateOverSubnodes(this, new DumpOperationOverNodes(string));
		return string.toString();
	}

	public String dumpStackVariables() {
		return stackVariables.dumpStackLevel();
	}

	private Node findLastNode(Node node) {
		while (node.hasNext()) {
			node = node.getNext();
		}
		return node;
	}

	public Object[] findNext(Object[] subs) {
		Node node = findNode(subs);
		if (node == null || !node.hasNext()) {
			return null;
		}
		return node.getNext().getSubs();
	}

	public Node findNode(Object... subs) {
		return findNodeByKey(generateKey(subs));
	}

	public Node findNode(String variableName) {
		return findNodeByKey(variableName);
	}

	private Node findNodeByKey(String key) {
		return (Node) KeyValue.get(key);
	}

	public Object[] findPrevious(Object[] subs) {
		Node node = findNode(subs);
		if (node == null || !node.hasPrevious()) {
			return null;
		}
		return node.getPrevious().getSubs();
	}

	private List<Node> findSubnodesByVarName(Object... variables) {
		if (variables == null) {
			return null;
		}
		List<Node> subnodes = new ArrayList<Node>(30);
		Node node = null;
		for (Object name : variables) {
			node = findNode(name);
			if (node == null) {
				node = setting(new Object[] { name }, null);
			}
			if (node != null && !subnodes.contains(node)) {
				subnodes.add(node);
			}
		}
		return subnodes;
	}

	private List<Node> findSubnodesExceptsByVarName(Object... variables) {
		if (variables == null) {
			return null;
		}

		List<Node> subnodes = this.getFirstLevelSubnodes();
		if (subnodes == null || subnodes.isEmpty()) {
			return null;
		}

		final List<Node> list = new ArrayList<Node>(30);
		subnodes: for (Node node : subnodes) {
			for (int i = 0; i < variables.length; i++) {
				if (variables[i] != null && variables[i].equals(node.getSubscriptAsString())) {
					continue subnodes;
				}
			}
			list.add(node);

		}
		return list;
	}

	private String generateKey(Object... subs) {
		return util.DataStructureUtil.generateKey(subs);
	}

	private Node generateNode(final Node parent, final Object[] subs, int index) {
		index = index + 1;
		final Object[] subnodeArray = Arrays.copyOf(subs, index);
		Node nodefound = findNode(subnodeArray);
		boolean exist = nodefound != null;
		if (!exist) {
			nodefound = new Node(subnodeArray, generateKey(subnodeArray));
			parent.addSubnode(nodefound);
			KeyValue.put(nodefound.getKey(), nodefound);
		}

		if (index < subs.length) {
			nodefound = generateNode(nodefound, subs, index);
		}

		if (nodefound.getParent() == null) {
			throw new IllegalStateException("Ferrou..." + nodefound);
		}

		// Definindo o node de referencia para efetuarmos as buscas atraves do
		// findPreviousNode. Esse eh um dos pontos complicados envolvendo
		// performance pois sempre comecavamos a pesquisa no inicio da lista
		// ligada,
		// mas agora vamos adotar outra estrategia e iniciaremos a pesquisa do
		// "previous node" sempre a partir do ultimo node incluido. Esse
		// mecanismo
		// existira apenas otimizar a inclusao dos nodes na arvore, pois a
		// recuperacao de nodes existentes sera feita atraves do MAP.
		nodefound.setSearchRefNode();
		return nodefound;
	}

	private final Node generateNode(Object[] subs) {
		return generateNode(this, subs, 0);
	}

	public Object get(Object... subs) {
		Node node = findNode(subs);
		if (node != null) {
			return node.getValue();
		}
		return null;
	}

	/*
	 * Metodo criado para execucao de testes unitarios
	 */
	private int getVariablesNumberInMap() {
		Set<String> variables = KeyValue.getFirstLevelKeys();
		int count = 0;
		for (String name : variables) {
			if (!name.contains("~")) {
				count++;
			}
		}
		return count;
	}

	/*
	 * Metodo criado para execucao de testes unitarios
	 */
	private int getVariablesNumberInTree() {
		List<Node> l = getFirstLevelSubnodes();

		return l != null ? l.size() : 0;
	}

	public boolean hasPopulatedSubnode(Node node) {

		boolean isPreenchido = false;
		if (node.hasSubnodes()) {
			for (Node subnode : node.getFirstLevelSubnodes()) {
				if (subnode.getValue() != null) {
					isPreenchido = true;
					break;
				}

				isPreenchido = hasPopulatedSubnode(subnode);
				if (isPreenchido) {
					return true;
				}
			}
		}

		return isPreenchido;

	}

	// The method which returns the sub nodes of the node should be enhanced.
	@TODO
	public boolean hasPopulatedSubnode(Node node, boolean found) {

		if (!found && node.hasSubnodes()) {
			Node subnode = node.getSubnode();
			if (subnode.getValue() != null) {
				found = true;
			}
			found = hasPopulatedSubnode(subnode, found);
		} else if (!found && node.isLeaf() && node.getValue() != null) {
			found = true;
		} else if (!found && node.hasNext()) {
			found = hasPopulatedSubnode(node.getNext(), found);
		}
		return found;
	}

	public boolean isEmpty() {
		return !this.hasSubnodes();
	}

	private boolean isNotPresentOnTree(Node node) {
		return node == null || !node.hasParent();
	}

	/*
	 * Metodo criado para execucao de testes unitarios
	 */
	public boolean isNumberVariablesCoherent() {

		int m = getVariablesNumberInMap();
		int t = getVariablesNumberInTree();
		return m == t;
	}

	private void kill(Node node) {
		if (isNotPresentOnTree(node)) {
			return;
		}
		if (node.isFirstSubnode()) {
			node.getParent().setSubnode(node.getNext());
		} else if (node.hasPrevious()) {
			node.getPrevious().setNext(node.getNext());
		}

		if (node.hasNext()) {
			node.getNext().setPrevious(node.getPrevious());
		}

		/*
		 * Ao removermos um node temos que redefinir o node de referencia que
		 * otimizara as pesquisas na lista ligada.
		 */
		node.resetSearchRef();

		node.killFromInterval();

		/*
		 * Aqui tambem estamos redefindo o searchRef utilizado para as pesquisas
		 * mais performaticas dos nodes na lista ligada.
		 */
		node.cancelReferences();
		// We have to remove from the map all subnodes references, other wise,
		// some another routine can recover them throught get(subs) method.
		killAllSubnodes(node);
	}

	public Node kill(Object... subs) {
		Node node = findNode(subs);
		if (node != null) {
			Node parentNode = node.getParent();
			kill(node);
			if (parentNode.getValue() == null && !parentNode.hasSubnodes()) {
				kill(parentNode);
			}
		}
		return node;
	}

	private void killAllSubnodes(Node node) {
		operateOverSubnodes(node, killSubnodesOperation);
	}

	private void killStackedVariables(Collection<Node> stackedNodes) {
		if (stackedNodes != null && !stackedNodes.isEmpty()) {
			for (Node stackedNode : stackedNodes) {
				/*
				 * First of all, we are to looking for some node with the same
				 * subscritps of the stacked node, then remove it from the tree
				 * and add the stacked node there. At second, we suppose that
				 * each stacked variable has the key as they variable name, for
				 * instance: subs = [%x] has the name variable as %x, then, it
				 * is identical to its generated key.
				 */
				Node nodeOnTheTree = findNode(stackedNode.getKey());
				replaceNode(stackedNode, nodeOnTheTree);
				stackedNode.setStackLevel(0);
			}
		}

	}

	/**
	 * This method was create to supports merge of different kind of variables,
	 * for instance, merge between local and public variables.
	 * 
	 * @param destSubs
	 * @param origNode
	 *            node from another kind of variable
	 */
	public void merge(final Object[] destSubs, final Node origNode) {
		if (origNode == null) {
			return;
		}

		Node destNode = findNode(destSubs);

		if (destNode == null) {
			destNode = setting(destSubs, null);
		}

		if (origNode.getValue() != null) {
			destNode.setValue(origNode.getValue());
		}

		if (origNode.hasSubnodes()) {
			mergeSubnodesOperation.set(destSubs, origNode.getSubs());
			operateOverSubnodes(origNode, mergeSubnodesOperation);
		}

	}

	public void merge(final Object[] destSubs, final Object[] origSubs) {
		merge(destSubs, findNode(origSubs));
	}

	/*
	 * Generic method which be reused by anothers when search subnodes is need.
	 * This may occur when we are dumping the tree and kill all subnodes of some
	 * node, for instance.
	 */
	private void operateOverSubnodes(Node node, OperationOverNodes operation) {
		if (node != null) {
			operation.operate(node);
			Node next = node.getSubnode();
			while (next != null) {
				operateOverSubnodes(next, operation);
				next = next.getNext();
			}
		}
	}

	public Object order(Object[] subs) {
		return order(subs, 1);
	}

	public Object order(Object[] subs, int direction) {
		// Here we are treating the case when we order function is called with
		// empty subscript. At this moment we have to return the first tree
		// subnode.
		Object subscript = "";

		if (subs.length == 1 && "".equals(subs[0])) {
			subscript = this.hasSubnodes() ? this.getSubnode().getSubscript() : "";
		} else {

			Object lastSubscript = subs[subs.length - 1];

			final boolean isEmptyLastSubs = lastSubscript == null || lastSubscript.toString().length() == 0;

			final boolean isFoward = direction > 0;
			// Condition to return to first element on list of subnodes.
			if (isEmptyLastSubs) {
				subs = Arrays.copyOf(subs, subs.length - 1);
			}

			Node node = findNode(subs);

			boolean nullNodeAdded = false;
			if (node == null) {
				node = setting(subs, null);
				nullNodeAdded = true;
			}

			if (isEmptyLastSubs && isFoward && node != null) {
				subscript = node.hasSubnodes() ? node.getSubnode().getSubscript() : "";
			} else if (isEmptyLastSubs && !isFoward && node != null) {
				subscript = node.hasSubnodes() ? findLastNode(node.getSubnode()).getSubscript() : "";
			} else if (isFoward && node != null) {
				subscript = node.hasNext() ? node.getNext().getSubscript() : "";
			} else if (node != null) {
				subscript = node.hasPrevious() ? node.getPrevious().getSubscript() : "";
			}

			if (nullNodeAdded) {
				kill(node);
			}
		}
		// Caso seja $order nas variáveis do processo, desconsidera variáveis
		// especiais ($ZTrap,$Zerror,etc..)
		if (subs.length == 1) {
			Object[] newSubs = new Object[] { subscript };
			if (subscript.toString().startsWith("$")) {
				return order(newSubs, direction);
			}
			Node newNode = findNode(newSubs);
			if ((newNode != null) && (!newNode.hasSubnodes() && (newNode.getValue() == null))) {
				return order(newSubs, direction);
			}
		}
		return subscript;
	}

	private void pushNodesToStack(int stackLevel, int methodStackLevel, StackVariables stack, List<Node> nodes) {
		/*
		 * Iterating over variable names collection. Here we suppose the
		 * variable name is the first subscript of the array.
		 */
		for (Node node : nodes) {
			// Avoid some variables which does not exist into the tree.
			if (node != null) {
				node.setStackLevel(stackLevel);
				node.setMethodStackLevel(methodStackLevel);
				stack.push(node);
				kill(node);
			}
		}
	}

	private void pushNodesToStack(int stackLevel, StackVariables stack, List<Node> nodes) {
		pushNodesToStack(stackLevel, 0, stack, nodes);
	}

	private void replaceNode(Node stackedNode, Node nodeOnTree) {
		if (nodeOnTree == null) {
			// Aqui estamos recuperando o node inserido para reconfigurar o
			// intervalo
			// logo abaixo.
			nodeOnTree = addSubnode(stackedNode);
		} else {
			stackedNode.setNext(nodeOnTree.getNext());
			stackedNode.setPrevious(nodeOnTree.getPrevious());
			stackedNode.setParent(nodeOnTree.getParent());

			if (nodeOnTree.hasPrevious()) {
				nodeOnTree.getPrevious().setNext(stackedNode);
			}

			if (nodeOnTree.isFirstSubnode()) {
				this.setSubnode(stackedNode);
			}

			if (nodeOnTree.hasNext()) {
				nodeOnTree.getNext().setPrevious(stackedNode);
			}
			/*
			 * Ao removermos um node temos que redefinir o node de referencia
			 * que otimizara as pesquisas na lista ligada. No caso que em
			 * estamos substituindo um node da pilha, essa etapa eh muito
			 * importante pois o nodeOnTree sera removido da arvore e se ele for
			 * o searchRef, teremos problemas nas pesquisas seguintes.
			 */
			nodeOnTree.resetSearchRef();

			stackedNode.setInterval(nodeOnTree.getInterval());

			/*
			 * Esse metodo rearranja os nodes que forem excluidos no intervalo.
			 * No caso em que o node seja um dos nodes dos extremos, teremos que
			 * efetuar uma nova amarracao do que eh o inicio e do que eh o fim.
			 * Sera utilizado no caso de chamadas do kill e do stack dos nodes.
			 */
			stackedNode.replaceFromInterval();
			nodeOnTree.cancelReferences();
			killAllSubnodes(nodeOnTree);
		}

		addAllSubnodes(stackedNode);
	}

	public void set(Object[] subs, Object value) {
		setting(subs, value);
	}

	private Node setting(Object[] subs, Object value) {

		if (subs == null || subs.length == 0) {
			return null;
		}

		Node node = findNode(subs);
		if (node == null) {
			node = generateNode(subs);
		}
		node.setValue(value);

		return node;
	}

	public void stacking(Object... variables) {
		currentStackLevel++;
		pushNodesToStack(currentStackLevel, stackVariables, findSubnodesByVarName(variables));
	}

	public void stackingBlock(int blockIndex, int methodStackLevel, Object... variables) {
		pushNodesToStack(blockIndex, methodStackLevel, stackBlockVariables, findSubnodesByVarName(variables));
	}

	public void stackingBlock(int blockIndex, Object... variables) {
		stackingBlock(blockIndex, 1, variables);
	}

	public void stackingExcept(Object... variables) {
		currentStackLevel++;
		pushNodesToStack(currentStackLevel, stackVariables, findSubnodesExceptsByVarName(variables));
	}

	public void stackingExceptBlock(int blockIndex, int methodStackLevel, Object... variables) {
		pushNodesToStack(blockIndex, methodStackLevel, stackBlockVariables, findSubnodesExceptsByVarName(variables));
	}

	public void unstacking() {
		final Collection<Node> stackedNodes = stackVariables.pull(currentStackLevel);
		killStackedVariables(stackedNodes);
		currentStackLevel--;

	}

	public void unstackingBlock(int indexBlock) {
		unstackingBlock(indexBlock, 1);
	}

	public void unstackingBlock(int indexBlock, int methodStackLevel) {
		final Collection<Node> stackedNodes = stackBlockVariables.pull(indexBlock, methodStackLevel);
		killStackedVariables(stackedNodes);
	}

}
