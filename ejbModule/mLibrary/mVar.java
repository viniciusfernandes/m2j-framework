package mLibrary;

import java.util.Arrays;

import mLibrary.exceptions.UndefinedVariableException;
import util.mFunctionUtil;
import br.com.innovatium.mumps2java.todo.TODO;
import dataaccess.mDataAccess;

public class mVar {
	private mDataAccess mData;
	/*
	 * The subscripts should be converted to String soon.
	 */
	@TODO
	private Object[] subs;

	public mVar(Object[] subs, mDataAccess mData) {
		this.subs = subs;
		this.mData = mData;
	}

	public String concat(Object value) {
		this.set(m$op.Concat(this.getValue(),value));
		return m$util.toString(this.getValue());
	}

	public int data() {
		return data(1);
	}

	public int data(int direction) {
		return mData.data(subs);
	}

	public Object get() {
		Object result = mData.get(subs);
		if (result==null){
			throw new UndefinedVariableException(Arrays.toString(subs));
		}
		mListBuild lo = mListBuild.getList(result.toString());
		if (lo != null) {
			return lo;
		}			
		return result;
	}

	public mDataAccess getmData() {
		return mData;
	}
	
	public String getName() {
		return subs[0].toString();
	}

	public mClass getORef() {
		Object objRef = get();
		if (objRef instanceof mClass) {
			return (mClass) objRef;
		} else {
			return null;
		}
	}
	
	public Object[] getParameters() {
		return Arrays.asList(subs).subList(1, subs.length).toArray();
	}

	public Object[] getSubs() {
		return subs;
	}

	public Object getSuppressedNullValue() {
		Object result = mData.get(subs);
		return (result == null)?"":result;
	}

	public Object getValue() {
		return mData.get(subs);	
	}

	public void kill() {
		mData.kill(subs);
	}

	public mVar lastVar(Object... subs) {
		return new mVar(mFunctionUtil.concatSinceLastSubscript(this.subs, subs),
				mData);
	}

	public void merge(mVar orig) {
		this.mData.merge(this.subs, orig.subs);
	}

	public Object order() {
		return order(1);
	}

	public Object order(int direction) {
		return mData.order(subs, direction);
	}

	public mVar s$(Object... subs) {
		return new mVar(mFunctionUtil.concat(this.subs, subs), mData);
	}

	public void set(Object value) {
		/*
		if(subs!=null && Arrays.toString(subs).contains("YINHALT") && (String.valueOf(value).equals("21/11/2013") || String.valueOf(value).equals("63147"))){
			mVar.class.getClass();
		}
		if(subs!=null && subs[0].equals("YVOR")){
			mVar.class.getClass();
		}		
		if(subs!=null && subs[0].equals("^WWWSOR") && Arrays.toString(subs).contains("Q")){
			mVar.class.getClass();
		}	
		if(subs!=null && subs[0].equals("YPARA") && Arrays.toString(subs).equals("[YPARA, 2]")){
			mVar.class.getClass();
		}*/
		if (value==null){
			//throw new UndefinedVariableException("Setting NULL value in: "+Arrays.toString(subs));
		}		
		mData.subs(subs).set(value);
	}
	
	public Boolean toBoolean() {
		return m$util.toBoolean(this.getValue());
	}
	
	public Double toDouble() {
		return m$util.toDouble(this.getValue());
	}
	
	public Integer toInt() {
		return m$util.toInt(this.getValue());
	}
	
	public Long toLong() {
		return m$util.toLong(this.getValue());
	}

	public Double toNumber() {
		return m$util.toNumber(this.getValue());
	}

	@Override
	public String toString() {
		return m$util.toString(this.getValue());
	}

	public void zkill() {
		throw new UnsupportedOperationException();
	}

}
