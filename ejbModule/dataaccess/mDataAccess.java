package dataaccess;

import java.io.Serializable;

import br.com.innovatium.mumps2java.datastructure.Tree;

public abstract class mDataAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7863964942675852841L;
	Object[] currentSubs;
	boolean firstExecutionOrder = true;
	final mVariables mVariables;
	boolean subsChanged;
	final Tree tree;

	public mDataAccess(mVariables mVariables, int type) {
		this.mVariables = mVariables;
		this.tree = mVariables.getVariables(type);
	}

	public abstract int data(Object... subs);

	public String dump() {
		return tree.dump();
	}

	public abstract Object get(Object... subs);

	public Object[] getCurrentSubs() {
		return currentSubs;
	}

	public abstract boolean isEmpty();

	public abstract void kill(Object[] subs);
	
	public void merge(Object[] dest, Object[] orig) {
		mVariables.merge(dest, orig);
	}

	public abstract Object order(Object... subs);

	public abstract Object order(Object[] subs, int direction);

	public Object orderVars(Object[] subs, int direction) {
		return this.mVariables.orderVariables(subs, direction);
	}

	public void set(Object value) {
		tree.set(currentSubs, value);
	}

	public abstract void stacking(Object... variables);

	public abstract void stackingBlock(int blockIndex, int dispatchIndex,
			Object... variables);

	public void stackingBlock(int indexBlock, Object... variables) {
		stackingBlock(indexBlock, 1, variables);
	}

	public abstract void stackingExcept(Object... variables);

	public abstract void stackingExceptBlock(int blockIndex, int dispatchIndex,
			Object... variables);

	public void stackingExceptBlock(int blockIndex, Object... variables) {
		stackingExceptBlock(blockIndex, 1, variables);
	}

	public final mDataAccess subs(Object... subs) {
		currentSubs = subs;
		return this;
	}

	public abstract void unstacking();

	public void unstackingBlock(int indexBlock) {
		unstackingBlock(indexBlock, 1);
	}

	public abstract void unstackingBlock(int indexBlock, int indexDispatch);
}
