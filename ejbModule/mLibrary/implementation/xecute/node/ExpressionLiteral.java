package mLibrary.implementation.xecute.node;

import br.com.innovatium.mumps2java.todo.REVIEW;

public class ExpressionLiteral extends Expression {
	
	/** Expression has a null value */
	public static final int NULL = 3;

	/** Expression has a numeric value */
    public static final int NUMERIC = 1;

    /** Expression has a string value */
    public static final int STRING = 2;

    String literalVale;
    
    public ExpressionLiteral(int literalType, String literalValue) {
		super(literalType, null);
		//Remove surrounding quotes if STRING done before.
		//this.literalVale = (this.oper == STRING) ? literalValue.substring(0, literalValue.length() - 2) : literalValue ;
		this.literalVale = literalValue ;
	}

	@Override
	public String toString() {
		return "\"" + String.valueOf(literalVale) + "\"";
	}

	@Override
	@REVIEW(description="numérico ou não???",date="26/08/2014",author="Fernando Viana Maia")
	Object eval() {
		/*
		 * 
		if (oper == NUMERIC){
			return Double.valueOf(literalVale);
		}
		*/
		return literalVale;
	}

	
}
