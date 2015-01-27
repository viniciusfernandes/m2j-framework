package dataaccess;

import util.DataStructureUtil;


public class mDataAccessPublic extends mDataAccessMemory {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4189462428274433309L;

	public mDataAccessPublic(mVariables mVariables){
		super(mVariables, DataStructureUtil.PUBLIC);
	}
}
