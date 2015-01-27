package br.com.innovatium.mumps2java.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StackBlockVariables extends StackVariables {
	@Override
	public List<Node> pull(final Integer stackLevel,
			final Integer methodStackLevel) {
		int lastLevel = getLastLevel(methodStackLevel);
		if (lastLevel == 0) {
			return null;
		}

		// Aqui estamos supondo que o ultimo nivel da pilha sempre existira e
		// sempre sera a maior. Se o stackLevel desejado for maior do que o
		// ultimo nivel da pilha isso indica que o stackLevel nao existe, entao
		// retornaremos pois nao ha nada a fazer.
		if (stackLevel > lastLevel) {
			return null;
		}

		final List<Node> nodes = new ArrayList<>();
		Collection<Node> stack = null;

		int level = stackLevel > lastLevel ? lastLevel : stackLevel;
		for (; lastLevel >= level; lastLevel--) {
			stack = getStack(level, methodStackLevel);
			if (stack != null) {
				nodes.addAll(stack);
				removeStack(level, methodStackLevel);
			}
		}
		if (nodes.isEmpty()) {
			return null;
		}

		return nodes;
	}

}
