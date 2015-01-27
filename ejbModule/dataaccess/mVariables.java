package dataaccess;

import static util.DataStructureUtil.GLOBAL;
import static util.DataStructureUtil.LOCAL;
import static util.DataStructureUtil.PUBLIC;
import static util.DataStructureUtil.getVariableType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.innovatium.mumps2java.datastructure.Node;
import br.com.innovatium.mumps2java.datastructure.Tree;

public final class mVariables implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6939149258248038749L;
	private final Tree globals = new Tree();
	private final Tree locals = new Tree();
	private final Tree publics = new Tree();
	private final Map<Integer, Tree> variablesMap;

	public mVariables() {
		variablesMap = new HashMap<Integer, Tree>();
		variablesMap.put(PUBLIC, publics);
		variablesMap.put(GLOBAL, globals);
		variablesMap.put(LOCAL, locals);
	}

	public Tree getVariables(int type) {
		return variablesMap.get(type);
	}

	public void merge(Object[] dest, Object[] orig) {
		Tree destTree = variablesMap.get(getVariableType(dest));
		Tree origTree = variablesMap.get(getVariableType(orig));

		Node origNode = origTree.findNode(orig);
		destTree.merge(dest, origNode);
	}

	public Object orderVariables(Object[] subs, int direction) {
		final int type = getVariableType(subs);
		Object next = null;
		final boolean isEmpty = subs.length == 1 && subs[0].toString().length() == 0;

		if (isEmpty) {
			Tree variables = !publics.isEmpty() ? publics : locals;
			return variables.order(subs, direction);
		}

		if (GLOBAL == type) {
			throw new UnsupportedOperationException("Does not be able order over global variables");
		}
		if (PUBLIC == type) {
			next = publics.order(subs, direction);
		}
		else {
			next = locals.order(subs, direction);
		}

		boolean isEndPublic = PUBLIC == type && "".equals(next);
		if (isEndPublic) {
			next = locals.order(new Object[] { "" }, direction);
		}
		return next;
	}
}
