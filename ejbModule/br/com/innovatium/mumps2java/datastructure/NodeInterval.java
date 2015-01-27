package br.com.innovatium.mumps2java.datastructure;

public class NodeInterval {

	/*
	 * Esse valor indica quanto nodes existirao no intervalo, sendo que a partir
	 * desse valor o intervalo sera subdividido atraves da funcao split.
	 */
	public static final int STEP = 20;
	private Node begin;

	private int countNodes;
	private Node end;

	private NodeInterval next;
	private NodeInterval previous;

	public NodeInterval(Node node) {
		this(node, node);
	}

	public NodeInterval(Node begin, Node end) {
		if (begin == null || end == null) {
			throw new IllegalArgumentException("O inicio e/ou fim do intervalo nao devem ser nulos. Inicio: " + begin
					+ " Fim: " + end);
		}

		if (begin.compareTo(end) < 0) {
			throw new IllegalArgumentException(
					"Problemas de consistencia com a criacao do intervalo. O inicio deve ser anterior ao fim. Inicio: " + begin
							+ " Fim: " + end);
		}

		setBegin(begin);
		setEnd(end);

		this.begin.setInterval(this);
		this.end.setInterval(this);

		countNodes = begin == end ? 1 : 2;
	}

	public void cancelIntervalReferences() {

		if (hasNext()) {
			// Efetuando a amaracao entre os intervalos anterior e posterior ao
			// corrente.
			if (hasPrevious()) {
				this.previous.next = this.next;
			}

			this.next.previous = this.previous;

			// Eliminando as referencias do intervalo corrente.
			this.previous = null;
			this.next = null;
		}
		else if (hasPrevious()) {
			this.previous.next = null;
			this.previous = null;
		}
		end.setInterval(null);
		begin.setInterval(null);

		this.begin = null;
		this.end = null;
	}

	public Node findBeginNodeFromInterval(Node node) {
		return findInterval(node).begin;
	}

	public NodeInterval findInterval(Node node) {
		NodeInterval interval = this;
		NodeInterval previous = this;

		if (isAfterEnd(node)) {
			do {
				if (!interval.hasNext()) {
					return interval;
				}
				previous = interval;
				interval = interval.next;

			}
			while (interval.isAfterEnd(node));
			return !interval.isAfterBegin(node) ? previous : interval;
		}

		if (isBeforeBegin(node)) {
			do {
				if (!interval.hasPrevious()) {
					return interval;
				}
				interval = interval.previous;
			}
			while (interval.isBeforeBegin(node));
			return interval;
		}
		else {
			// A condicao default indica que o node esta dentro desse intervalo.
			return this;
		}
	}

	public Node getBegin() {
		return begin;
	}

	public Node getEnd() {
		return end;
	}

	public NodeInterval getNext() {
		return next;
	}

	public NodeInterval getPrevious() {
		return previous;
	}

	public boolean hasNext() {
		return this.next != null;
	}

	public boolean hasPrevious() {
		return this.previous != null;
	}

	public boolean isAfterBegin(Node node) {
		return node.compareTo(begin) > 0;
	}

	public boolean isAfterEnd(Node node) {
		return node.compareTo(end) > 0;
	}

	public boolean isBeforeBegin(Node node) {
		return node.compareTo(begin) < 0;
	}

	public boolean isBeforeEnd(Node node) {
		return node.compareTo(end) < 0;
	}

	public boolean isBegin(Node node) {
		return this.begin.compareTo(node) == 0;
	}

	public boolean isEnd(Node node) {
		return this.end.compareTo(node) == 0;
	}

	public boolean isInto(Node node) {
		return begin.compareTo(node) >= 0 && end.compareTo(node) <= 0;
	}

	/*
	 * Esse metodo rearranja os nodes que forem excluidos no intervalo. No caso em
	 * que o node seja um dos nodes dos extremos, teremos que efetuar uma nova
	 * amarracao do que eh o inicio e do que eh o fim. Sera utilizado no caso de
	 * chamadas do kill e do stack dos nodes.
	 */
	public void kill(Node node) {
		int beginCompare = node.compareTo(begin);
		int endCompare = node.compareTo(end);

		// Quando o inicio eh o mesmo que o fim, isso indica que devemos remover o
		// intervalo, alem disso temos que remover a amaracao entre o next e o
		// previous dos intervalos para manter a coerencia.
		if (beginCompare == 0 && endCompare == 0) {
			cancelIntervalReferences();
			return;
		}

		boolean isInto = beginCompare >= 0 && endCompare <= 0;
		if (!isInto) {
			return;
		}

		// Verificando se o node eh algum dos extremos do intervalo, pois para
		// substir esse node do intervalo temos que redefinir quem sera o begin e
		// end. Esse caso ocorrera no kill das variaveis.
		if (beginCompare == 0) {
			this.setBegin(node.getNext());
		}
		else if (endCompare == 0) {
			this.setEnd(node.getPrevious());
		}

		decrement();
	}

	/*
	 * Esse metodo rearranja os nodes que forem substituidos no intervalo. No caso
	 * em que o node seja um dos nodes dos extremos, teremos que efetuar uma nova
	 * amarracao do que eh o inicio e do que eh o fim. Sera utilizado no caso de
	 * chamadas do kill e do stack dos nodes.
	 */
	public void replace(Node node) {

		// Verificando se o node pertence ao intervalo
		int beginCompare = node.compareTo(begin);
		int endCompare = node.compareTo(end);
		boolean isInto = beginCompare >= 0 && endCompare <= 0;
		if (!isInto) {
			node.setInterval(new NodeInterval(node));
			return;
		}
		// Aqui estamos apenas substituindo um node pelo outro. Esse caso ocorrera
		// quando realizarmos o unstack das variaveis
		if (beginCompare == 0) {
			setBegin(node);
		}
		if (endCompare == 0) {
			setEnd(node);
		}

	}

	public void resetLimits(Node node) {
		int beginCompare = node.compareTo(this.begin);
		int endCompare = node.compareTo(this.end);
		/*
		 * Estamos garantindo que o novo node eh realmente menor do que o limite
		 * inferior do intervalo, caso contrario nao podemos inclui-lo.
		 */
		if (beginCompare == 0 && endCompare == 0) {
			setBegin(node);
			setEnd(node);
		}
		else if (beginCompare <= 0) {
			setBegin(node);
		}
		else if (endCompare >= 0) {
			setEnd(node);
		}
		// Vamos adicionar uma contagem de nodes dentro do intervalo apenas se ele
		// for diferente dos nodes do extremo. Isso eh devido ao fato de que nesse
		// ponto o node ja foi incluido ao intervalo.
		if (beginCompare != 0 || endCompare != 0) {
			increment();
		}
	}

	public void setBegin(Node begin) {
		if (begin == null) {
			throw new IllegalArgumentException("Problemas ao tentar ajustar o inicio do intervalo " + this
					+ ". O inicio nao pode ser nulo");
		}
		this.begin = begin;
		begin.setInterval(this);
	}

	public void setEnd(Node end) {
		if (end == null) {
			throw new IllegalArgumentException("Problemas ao tentar ajustar o fim do intervalo " + this
					+ ". O fim nao pode ser nulo");
		}
		this.end = end;
		end.setInterval(this);
	}

	public void setNext(NodeInterval next) {
		this.next = next;
	}

	public void setPrevious(NodeInterval previous) {
		this.previous = previous;
	}

	public int size() {
		return countNodes;
	}

	public void split() {
		if (countNodes <= STEP) {
			return;
		}

		Node newEnd = this.end.getPrevious();
		// Aqui estamos supondo que o begin ja exista no intervalo e que um segundo
		// node tambem ja foi incluido nesse intervalo, isto eh, temos ao menos um
		// numero maior ou igual ao numero de limiteque chamamos de STEP, portanto,
		// sempre teremos um begin.next.
		if (newEnd == null) {
			throw new IllegalStateException("Intervalo com problemas: " + this + ". Foi encontrado que o node " + this.end
					+ " nao possui \"previous\", o que implica em um estado incoerente do intervalo.");
		}

		/*
		 * Aqui estamos configurando o novo intervalo e redefinindo os limites.
		 */
		NodeInterval nextInterval = this.next;
		if (nextInterval == null) {
			nextInterval = new NodeInterval(this.end, this.end);
			// Todo intervalo novo tera apenas um node.
			nextInterval.countNodes = 1;

			this.setNext(nextInterval);
			nextInterval.setPrevious(this);
		}
		else if (nextInterval.countNodes >= STEP) {
			nextInterval = new NodeInterval(this.end, this.end);
			// Todo intervalo novo tera apenas um node.
			nextInterval.countNodes = 1;

			nextInterval.setNext(this.getNext());
			nextInterval.setPrevious(this);

			this.getNext().setPrevious(nextInterval);
			this.setNext(nextInterval);

		}
		else if (nextInterval.countNodes < STEP) {
			// Vamos ajustar o nextInterval para incluir o novo node, sendo que o novo
			// node pertencera a um novo intervalo.
			nextInterval.resetLimits(this.end);
			this.end.setInterval(nextInterval);
		}

		// Reconfigurando o limite superior do intervalo, ja que o limite superior
		// sera o limite inferior do proximo intervalo.
		this.end = newEnd;
		decrement();
	}

	public String toString() {
		StringBuilder string = new StringBuilder();
		if (begin == null || end == null) {
			throw new IllegalStateException("node interval com problemas ");
		}
		string.append("(").append(begin.getSubscript()).append(", ").append(end.getSubscript()).append(")(")
				.append(countNodes).append(")");

		return string.toString();
	}

	private void decrement() {
		countNodes -= 1;
	}

	private void increment() {
		countNodes += 1;
	}

}