package mLibrary.implementation.xecute.node;

import java.util.ArrayList;
import java.util.List;

import mLibrary.mContext;
import mLibrary.mVar;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.LexicalTokenizer;

public class ExpressionVariable extends Expression {

	/** Expression is a SYSTEM variable */
	public static final int BYREF = 3;
	/** Expression is a GLOBAL variable */
	public static final int GLOBAL = 2;
	/** Expression is a LOCAL  variable */
    public static final int LOCAL = 1;
	
	//the attribute 'oper' which was extended from super class, holds this VARIABLE subType

	/** Expression is a SYSTEM variable */
	public static final int SYSTEM = 4;

    private static void parse(ExpressionVariable var, LexicalTokenizer lt) {
			if (!lt.hasMoreTokens()) return;
			//must not have space between variableName and subscripts
			if (lt.isSymbol('(')){ //get subscripts
				
				do {
					lt.currentPos++; //eat '(' or ','
					var.subscripts.add(lt.getExpression()); // get subscripts recursively.
					lt.nextToken();
				} while (lt.isSymbol(','));
				
				if (!lt.isSymbol(')')){
					throw new XecuteException("Error. expected ')' ." + lt.showError());
				}
				lt.currentPos++;// eat ')'
			}
		}
    
    protected mContext m$;

    List<Expression> subscripts;
    

    String variableName;

	public ExpressionVariable(int variableType, String variableName, LexicalTokenizer lt) {
	super(variableType, null);
	this.variableName = variableName;
	this.subscripts = new ArrayList<Expression>();
	this.m$ = lt.getContext();
	parse(this,lt);
}

	public Object[] evalVariableName(){
		return concatArray();
	}

	// change this variable type
	public void makeByRef(){
		this.oper = BYREF;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(variableName);
		if (hasSubscripts()){
			sb.append("(");
			for (Expression element : subscripts) {
				sb.append(element.toString());
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	private Object[] concatArray() {
		Object[] result = new Object[subscripts.size() + 1];
		result[0] = variableName;
		for (int i = 0; i < subscripts.size(); i++) {
			result[i + 1] = subscripts.get(i).eval();
		}
		return result;
	}
	
	private boolean hasSubscripts() {
		return (!this.subscripts.isEmpty());
	}
	
	@Override
	Object eval() {
		return m$.var(evalVariableName()).get();
	}

	mVar evalToMVar(){
		return m$.var(evalVariableName());
	}
}
