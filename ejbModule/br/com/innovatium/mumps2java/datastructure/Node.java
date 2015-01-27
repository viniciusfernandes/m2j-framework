package br.com.innovatium.mumps2java.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.mFunctionUtil;

public class Node implements Comparable<Node>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6381697797559629833L;
	private NodeInterval interval;
	private final String key;
	private Integer methodStackLevel;
	private Node next;
	private Node parent;
	private Node previous;
	private Node searchRef;

	private Integer stackLevel;

	private Node subnode;
	private final Object[] subs;
	private Double subsAsNumeric;
	private Object subscript;

	private Object value;

	private boolean isNumeric;

	public Node(Object[] subs, Object value, String key) {
		if (subs == null) {
			throw new IllegalArgumentException("The subs array must not be null");
		}
		this.subs = subs;
		this.value = value;
		this.key = key;
		final int index = subs.length == 1 ? 0 : subs.length - 1;

		Object subsTemp = subs[index];

		if (subsTemp == null) {
			throw new IllegalArgumentException("Subscript must not be empty. The subs setted is " + Arrays.deepToString(subs)
					+ " and value is " + value);
		}
		if (subsTemp instanceof Integer) {
			isNumeric = true;
			subsAsNumeric = ((Integer) subsTemp).doubleValue();
		}
		else if (subsTemp instanceof Long) {
			isNumeric = true;
			subsAsNumeric = ((Long) subsTemp).doubleValue();
		}
		else if (subsTemp instanceof Double) {
			isNumeric = true;
			subsAsNumeric = ((Double) subsTemp);
		}
		else {
			isNumeric = mFunctionUtil.isCanonicalNumeric(subsTemp.toString());
			if (isNumeric) {
				subsAsNumeric = Double.parseDouble(subsTemp.toString());
			}
		}
		subscript = subs[index];

	}

	public Node(Object[] subs, String key) {
		this(subs, null, key);
	}

	public void cancelReferences() {

		// Canceling all references that node does.
		this.parent = null;
		this.next = null;
		this.previous = null;
		this.interval = null;
	}

	public int compareTo(Node o) {

		boolean boothNumeric = this.isNumeric && o.isNumeric;
		if (boothNumeric) {
			try {
				return subsAsNumeric.compareTo(o.subsAsNumeric);
			}
			catch (ClassCastException e) {
				throw new IllegalArgumentException("There is some inconsistence when was setted the nodes " + this + " and "
						+ o + ". Fail to compare subscript: " + this.subscript + " and this subscript: " + ((Node) o).subscript
						+ ". They must have to be the same type.", e);
			}
		}

		// Tratando os casos em que exite a comparacao entre String e Number
		if (this.isNumeric && !o.isNumeric) {
			return -1;
		}

		// Tratando os casos em que exite a comparacao entre String e Number
		if (!this.isNumeric && o.isNumeric) {
			return 1;
		}

		// Tratando os casos ambos sao String
		return this.subscript.toString().compareTo(o.subscript.toString());
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Node && this.key.equals(((Node) o).key);
	}

	public List<Node> getFirstLevelSubnodes() {

		if (this == null || !this.hasSubnodes()) {
			return null;
		}

		final List<Node> list = new ArrayList<Node>(30);
		Node next = this.getSubnode();
		do {
			list.add(next);
		}
		while ((next = next.getNext()) != null);
		return list;
	}

	public NodeInterval getInterval() {
		return interval;
	}

	public String getKey() {
		return key;
	}

	public Integer getMethodStackLevel() {
		return methodStackLevel;
	}

	public Node getNext() {
		return next;
	}

	public Node getParent() {
		return parent;
	}

	public Node getPrevious() {
		return previous;
	}

	public Integer getStackLevel() {
		return stackLevel;
	}

	public Node getSubnode() {
		return subnode;
	}

	public Object[] getSubs() {
		return subs;
	}

	public Object[] getSubs(int start) {
		return Arrays.copyOfRange(subs, start, subs.length);
	}

	public Object getSubscript() {
		return subscript;
	}

	public Object getSubscriptAsString() {
		return subscript.toString();
	}

	public Object[] getSubsExceptFirst() {
		return Arrays.copyOfRange(subs, 1, subs.length);
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	public boolean hasNext() {
		return next != null && !next.equals(previous);
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	public boolean hasPrevious() {
		return this.previous != null;
	}

	public boolean hasSubnodes() {
		return subnode != null;
	}

	public boolean isAfter(Node node) {
		return this.compareTo(node) > 0;
	}

	public boolean isFirstSubnode() {
		if (parent != null && parent.hasSubnodes()) {
			return parent.getSubnode().equals(this);
		}
		return false;
	}

	public boolean isLeaf() {
		return !isRoot() && !hasSubnodes();
	}

	public boolean isRoot() {
		return parent == null;
	}

	/*
	 * Esse metodo rearranja os nodes que forem excluidos no intervalo. No caso em
	 * que o node seja um dos nodes dos extremos, teremos que efetuar uma nova
	 * amarracao do que eh o inicio e do que eh o fim. Sera utilizado no caso de
	 * chamadas do kill e do stack dos nodes.
	 */
	public void killFromInterval() {
		interval.kill(this);
	}

	/*
	 * Esse metodo rearranja os nodes que forem substituidos no intervalo. No caso
	 * em que o node seja um dos nodes dos extremos, teremos que efetuar uma nova
	 * amarracao do que eh o inicio e do que eh o fim. Sera utilizado no caso de
	 * chamadas do replace node.
	 */
	public void replaceFromInterval() {
		interval.replace(this);
	}

	public void resetInterval(NodeInterval interval) {
		this.interval = interval;

		// Aqui estamos definindo a qual intervalo o node pertence e a
		// implementacao do metodo vai efetuar uma reconfiguracao dos limites
		// desse intervalo, pois pode ser que o node seja um dos limites
		// inferiores/superiores do intervalo. Temos que examinar esse cenario.
		this.interval.resetLimits(this);
	}

	public void resetSearchRef() {

		// Aqui estamos fazendo o tratamento para o caso em que o node seja removido
		// da arvore e ele seja o node de referencia das pesquisas na lista ligada.
		// Ajustamos o previous pois estaremos coerentes com a estrategia da criacao
		// do searchRef que define como referencia o ultimo node acessado, portanto,
		// eh bem provavel que o penultimo node acessado seja o anterior, ou seja, o
		// previous.

		if (parent.searchRef == this) {

			if (this.hasPrevious()) {
				parent.searchRef = this.previous;
			}
			else if (this.hasNext()) {
				parent.searchRef = this.next;
			}else{
				parent.searchRef = parent.subnode;
			}
		
		}

	}

	public void setInterval(NodeInterval interval) {
		this.interval = interval;
	}

	public void setMethodStackLevel(Integer methodStackLevel) {
		this.methodStackLevel = methodStackLevel;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public void setStackLevel(Integer stackLevel) {
		this.stackLevel = stackLevel;
	}

	public void setSubnode(Node subnode) {
		this.subnode = subnode;
	}

	public void setValue(Object value) {
		if (value instanceof Boolean) {
			value = (boolean) value ? "1" : "0";
		}
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("(").append(key != null ? key : "").append(", ").append(value).append(")");

		if (interval != null) {
			string.append("=>").append(interval);
		}

		return string.toString();
	}

	Node addSubnode(Node newSubnode) {
		// Essa linha eh necessaria pois para ajustar o searchPath precisamos do
		// parent do node, sendo que o parent sera ajustado somente adiante
		newSubnode.parent = this;
		if (subnode == null) {
			subnode = newSubnode;
			newSubnode.interval = new NodeInterval(subnode);
		}
		else {
			Node previous = findPreviousNode(searchRef, newSubnode);
			if (previous == null) {
				return null;
			}
			// When previous node is the first sub node into the hierarchy we
			// have to switch its positions to maintain the order mechanism.
			if (previous.isFirstSubnode() && previous.isAfter(newSubnode)) {
				subnode = newSubnode;
				newSubnode.next = previous;
				newSubnode.previous = null;
				previous.previous = newSubnode;
			}
			else {
				newSubnode.next = previous.next;
				previous.next = newSubnode;
				newSubnode.previous = previous;
				if (newSubnode.next != null) {
					newSubnode.next.previous = newSubnode;
				}
			}
			// Aqui estamos definindo a qual intervalo o node pertence e a
			// implementacao do metodo vai efetuar uma reconfiguracao dos limites
			// desse intervalo, pois pode ser que o node seja um dos limites
			// inferiores/superiores do intervalo. Temos que examinar esse cenario.
			newSubnode.resetInterval(previous.interval);
		}

		// Definindo o node de referencia para efetuarmos as buscas atraves do
		// findPreviousNode. Esse eh um dos pontos complicados envolvendo
		// performance pois sempre comecavamos a pesquisa no inicio da lista
		// ligada,
		// mas agora vamos adotar outra estrategia e iniciaremos a pesquisa do
		// "previous node" sempre a partir do ultimo node incluido. Esse mecanismo
		// existira apenas otimizar a inclusao dos nodes na arvore, pois a
		// recuperacao de nodes existentes sera feita atraves do MAP.
		newSubnode.setSearchRefNode();

		// EH MUITO IMPORTANTE REALIZARMOS O SPLIT AQUI POIS ESSE METODO SUPE QUE
		// TODOS OS NODES JA ESTEJAM AMARRADOS E TEREMOS ESSA CONDICAO SATISFEITA
		// APENAS APOIS O TERMINO DO ADDSUBNODE.
		newSubnode.interval.split();
		return newSubnode;
	}

	Node findPreviousNode(Node searchRef, Node newNode) {

		if (newNode == null || searchRef == null ){
			
			System.out.println("caiu");
		}
		int previousCompare = 0;
		int currentCompare = 0;

		// Aqui estamos pesquisando em qual intervalo o node pertence. Assim que
		// encontramos o intervalo pegamos o primeiro node do intervalo para fazer a
		// varredura na lista ligada atraves do node.next.
		try{
		searchRef = searchRef.interval.findBeginNodeFromInterval(newNode);
		}catch(NullPointerException e){
			throw new IllegalArgumentException();
		}

		while (searchRef != null) {

			currentCompare = searchRef.compareTo(newNode);
			// Nao vamos tratar o caso em que currentCompare == 0 pois esse caso ja
			// foi tratado antes do addSubnode quando pegamos o subnode atraves do
			// metodo findNode(subscritos). Nesses casos, isso indica que o node ja
			// existe entao o codigo nunca atingira o metodo addSubnode, sendo assim,
			// nao ha a necessidade de tratarmos o caso currentCompare == 0.
			if (currentCompare > 0) {

				if (searchRef.hasPrevious() && previousCompare < 0) {
					return searchRef.getPrevious();
				}

				return searchRef;
			}
			else if (searchRef.hasNext()) {
				if (searchRef.equals(searchRef.next)) {
					throw new IllegalArgumentException(
							"Previous node is equal the next node, so, we have inconsistente relationship. Node problem: "
									+ searchRef);
				}
				searchRef = searchRef.next;
				previousCompare = currentCompare;
			}
			else {

				return searchRef;
			}
		}

		throw new IllegalArgumentException("Initial node must not be null");
	}

	void setSearchRefNode() {

		// Definindo o node de referencia para efetuarmos as buscas atraves do
		// findPreviousNode. Esse eh um dos pontos complicados envolvendo
		// performance pois sempre comecavamos a pesquisa no inicio da lista ligada,
		// mas agora vamos adotar outra estrategia e iniciaremos a pesquisa do
		// "previous node" sempre a partir do ultimo node incluido. Note que
		// vamos sempre vamos ajustar o searchRef no parent do node, pois o
		// searchRef sempre sera um dos filhos do parent.
		parent.searchRef = this;
	}

}