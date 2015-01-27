package dataaccess;

import util.DataStructureUtil;

public class mDataAccessMemory extends mDataAccess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5577472348701494675L;

	public mDataAccessMemory() {
		super(new mVariables(), DataStructureUtil.LOCAL);
	}
	
	public mDataAccessMemory(mVariables mVariables, int type) {
		super(mVariables, type);
	}

	public int data(Object... subs) {
		return tree.data(subs);
	}

	public String dump() {
		return tree.dump();
	}

	public Object get(Object... subs) {
		return tree.get(subs);
	}

	@Override
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	public void kill(Object[] subs) {
		tree.kill(subs);
	}

	public Object order(Object... subs) {
		return order(subs, 1);
	}

	public Object order(Object[] subs, int direction) {
		return tree.order(subs, direction);
	}

	public void stacking(Object... variables) {
		tree.stacking(variables);
	}

	@Override
	public void stackingBlock(int blockIndex, int indexDispath,
			Object... variables) {
		tree.stackingBlock(blockIndex, indexDispath, variables);
	}

	public void stackingExcept(Object... variables) {
		tree.stackingExcept(variables);
	}

	public void stackingExceptBlock(int blockIndex, int dispatchIndex, Object... variables) {
		tree.stackingExceptBlock(blockIndex, dispatchIndex, variables);
	}

	public void unstacking() {
		tree.unstacking();
	}

	public void unstackingBlock(int indexBlock, int indexDispatch) {
		tree.unstackingBlock(indexBlock, indexDispatch);
	}
}


