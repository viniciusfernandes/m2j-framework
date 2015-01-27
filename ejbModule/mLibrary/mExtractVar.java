package mLibrary;

import br.com.innovatium.mumps2java.todo.TODO;

public class mExtractVar extends mVar {
	private Integer from;
	private Integer to;

	public mExtractVar(mVar var) {
		this(var,1,1);
	}
	
	public mExtractVar(mVar var, Object from) {
		this(var,from,from);
	}
	
	public mExtractVar(mVar var, Object from, Object to) {
		super(var.getSubs(), var.getmData());
		this.from = util.mFunctionUtil.integerConverter(from);
		this.to = util.mFunctionUtil.integerConverter(to);
	}

	@TODO
	public void set(Object value) {
		if (value instanceof mVar) {
			value = ((mVar) value).get();
		}
		super.set(mFunction.$setextract(this.getValue(), from, to, value));
	}
}
