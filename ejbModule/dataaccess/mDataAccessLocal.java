package dataaccess;

import util.DataStructureUtil;

public class mDataAccessLocal extends mDataAccessMemory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113748713459799613L;

	public mDataAccessLocal(mVariables mVariables) {
		super(mVariables, DataStructureUtil.LOCAL);
	}
}
