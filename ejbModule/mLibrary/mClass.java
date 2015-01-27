package mLibrary;

public class mClass {
	public mContext m$;
	public mFunction m$fn;
	
	public mClass() {
	}

	public mClass(mContext context) {
		this.init(context);
	}

	public String $ClassName(Object... args) {
		Boolean fullName = false;
		if (args.length > 0) {
			fullName = m$op.Logical(args[0]);
		}
		if (fullName) {
			return this.getClass().getName();
		}
		else {
			String[] arrayName = this.getClass().getName().split("\\.",-1);
			return arrayName[arrayName.length-1];
		}
	}

	public Object $Extends(Object... args) {
		//TODO NÃO IMPLEMENTADO 
		return null;
	}

	public Object $IsA(Object... args) {
		//TODO NÃO IMPLEMENTADO 
		return null;
	}

	public void init(mContext context) {
		this.m$ = context;
		this.m$fn = context.getFunction();
	}

	public mContext getContext() {
		return m$;
	}

	public mVar m$piece(mVar var, Object delimiter, Object from) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public mVar m$piece(mVar var, Object delimiter, Object from, Object to) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
