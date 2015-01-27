package mLibrary.implementation.xecute;

import mLibrary.implementation.xecute.node.CommandExpression;
import mLibrary.implementation.xecute.node.Expression;
import mLibrary.implementation.xecute.node.ExpressionLiteral;
import mLibrary.implementation.xecute.node.ExpressionVariable;

public class Token {
	
	/** Token is a command */
	public static final int COMMAND = 1;

    /**
     * This constants should match the indexes of the above names.
     */
    
	/** Token indicates the end of an input line */
    public static final int EOL = 0;
    
    /** Token is a function extrinsic ($$) or intrinsic ($) */
    public static final int FUNCTION = 2;

    /** Token has a string or numeric value */
    public static final int LITERAL = 4;
    
    /** Not predicted Token */
    public static final int NOTPREDICTED = 7;

    /** Token is an operator in an expression. */
    public static final int OPERATOR = 5;

    /** Token is a special symbol */
    public static final int SYMBOL = 3;

    /** Token is a variable local or global*/
    public static final int VARIABLE = 6;

    final static String names[] = {
		  	"eol","command", "function", "symbol",
		      "literal", "operator", "variable", "token not predicted"
		  };


    // if type is LITERAL -> holds the end position where this token was found.
		private int intrinsicFunctionType = -1;
    private int 	startPos;   	// initial position where it was found.
    private int     subType = 0;    // this token's specialization type, except for SYMBOL which has no subType.
	private int     type;     		// this token's type
									private String  value;      	// if type is SYMBOL  -> holds the textual representation of the token.
							    	
	
	public Token() {
	}
	
	/** construct token SYMBOL */
	public Token(int type, int pos, Character value) {
		this.type = type;
		this.startPos = pos;
		//this.endPos = pos;
		this.value = value.toString();
	}
 	
	/** construct token VARIABLE */
	public Token(int type, int startPos, int endPos, char[] buffer) {
			this.startPos = startPos;
			this.type = VARIABLE;
			//this.endPos = endPos;
			this.subType = (isSymbol('^')) ? ExpressionVariable.GLOBAL :  ExpressionVariable.LOCAL;
			int length = endPos - startPos; 
			this.value = new String(buffer, startPos, length);
			
	}
	
	/** construct token COMMAND / OPERATOR / LITERAL / FUNCTION (not intrinsic) */
	public Token(int type, int subType, int startPos, int endPos, char[] buffer) {
		this.type = type;
		this.subType = subType;
		this.startPos = startPos;
		//this.endPos = endPos;
		int length = endPos - startPos;
		if ( isLiteral() && subType == ExpressionLiteral.STRING) {
			startPos++ ; 	//eat first double quote
			length-= 2 ;  	//eat last double quote
		}
		this.value = new String(buffer, startPos, length);
	}
	/** construct token FUNCTION (intrinsic) */
	public Token(int type, int subType, int intrinsicFunctionType,
			int startPos, int endPos, char[] buffer) {
		this.type = type;
		this.subType = subType;
		this.intrinsicFunctionType = intrinsicFunctionType;
		this.startPos = startPos;
		int length = endPos - startPos;
		this.value = new String(buffer, startPos, length);
	}
	/** construct token FUNCTION (CLASSREF) */
	public Token(int function, int previousPos, int classref, String name) {
		this.type = function;
		this.startPos = previousPos;
		this.subType = classref; 
		this.value = name;
	}
	/**
	 * Constructors.
	 */
	/**construct token NOTPREDICTED*/
	public Token(int type, int pos, String value) {
		this.type = type;
		this.startPos = pos;
		this.value = value;
	}

	public Integer getIntrinsicType() {
		return intrinsicFunctionType;
	}

    
	public int getSubType() {
		return this.subType;
	}
    
    public int getType() {
		      return type;
		  }
    
    public String getValue() {
        return value;
    }
    
    public boolean isFunction() {
			return (type == FUNCTION);
		}

    public boolean isLiteral(){
    	return (type == LITERAL);
    }

    public boolean isOperator(char c) {
        return ( isOperator() && c == this.value.charAt(0) );
    }
    
    public boolean isSymbol(char c) {
        return ( this.type == SYMBOL && c == this.value.charAt(0) );
    }
    
    public boolean isVariable() {
			return (type == VARIABLE);
		}
    
    /**************/
		
		public String toString() {
		  	
		      if (isCommand()){
		      	return (names[type] + " - position : " + startPos + " value: " + CommandExpression.commandNames[getSubType()]);
		      }
		      return value;
		  	//return (names[type] + " - position : " + startPos + " value: " + value);
		  }

	private boolean isCommand() {
	return (type == COMMAND);
}

	final boolean isOperator() {
	    return (type == OPERATOR);
	}

	final boolean isUnaryOperator(int subType) {
		switch (subType){
		case Expression.OP_NOT:
		case Expression.OP_POS:
		case Expression.OP_ADD:
		case Expression.OP_NEG:
		case Expression.OP_SUB:	
		case Expression.OP_IND:
			return true;
	}
	return false; 
}

}
