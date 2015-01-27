/*
 * Parse the input String into tokens.
 * */
package mLibrary.implementation.xecute;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.registry.infomodel.ExtrinsicObject;

import mLibrary.mContext;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.node.CommandExpression;
import mLibrary.implementation.xecute.node.Expression;
import mLibrary.implementation.xecute.node.ExpressionFunction;
import mLibrary.implementation.xecute.node.ExpressionLiteral;
import mLibrary.implementation.xecute.node.ExpressionVariable;

public class LexicalTokenizer extends Token{

	public static final Pattern caretNamePattern = Pattern.compile("^(\\^{0,1}[%[a-zA-Z]]{1}[a-zA-Z\\d]*)");
	/** 
	*   Variable names are strings of alphanumeric characters. 	
	*	The first character must be an alphabetic character or a "%" character. Names are case sensitive.
	*   Global variables are distinguished from local variables by the addition of a leading "^" character.
	*   
	*   label naming is similar to variable, except by the first character which can be numeric also.
	*   while routine naming is similar to global.
	*/
	
	public static final Pattern namePattern = Pattern.compile("^([%[a-zA-Z\\d]]{1}[a-zA-Z\\d]*)");
	public int currentPos = 0;
	protected mContext m$;
	char buffer[];
	
	int markPos = 0;
	
	int previousPos = 0;
	
	
	
	public LexicalTokenizer(String data,mContext m$) {
		super();
		this.m$ = m$; 
		buffer = data.toCharArray();
	}

	public mContext getContext(){
		return this.m$;
	}

	public int getCurrentPos() {
		return currentPos;
	}
	public Expression getExpression() {
	boolean meaningfulSpace = false;
	return getExpression(meaningfulSpace);
}

	/***/
	public Expression getExpression(boolean meaningfulSpace) {
	
		Stack<Integer> operatorStack = new Stack<Integer>();  // to keep operators.
	Expression result = null;
	
		
	
	loop: while (hasMoreTokens()) {
		int backupPosition = currentPos;
		if (isSpace()){
			if (meaningfulSpace){
				if(spyNextTrimChar()=='='){
					meaningfulSpace = false;
				}else{
					break loop;
				}
			}
			leftTrim(); //eat spaces ahead
			if (hasMoreTokens() && result!=null && isLetter()){ //perhaps a command will be ahead. Parse it at higher level.
				currentPos = backupPosition;
				break loop;
			}
		}
		

		Expression temp = null;
		
		Token t = nextToken(true); //always get spaces to detect if next token is a command 
		
		switch (t.getType()) {
			case EOL:
				break loop;

			case VARIABLE:
				temp = new ExpressionVariable(t.getSubType(), t.getValue(), this);
				break;

			case LITERAL:
				temp = new ExpressionLiteral(t.getSubType(), t.getValue());
				break;
				
			case FUNCTION:
				temp = new ExpressionFunction(t, this);
				break;
				
			case OPERATOR:
				// TODO - If binary and there is another binary operator in stack, then should be syntax error .
				operatorStack.push( t.getSubType() );
				break;

			case SYMBOL:
				if (isSymbol('(')) {
					currentPos++; // eat '('
					
					temp = getExpression(false); // get expression recursively.
					
					if (!isSymbol(')')) {
						throw new XecuteException("Error. expected ')' "+ showError());
					}
					currentPos++; // eat ')'
					break;
				}					
				if (isTerminatorSymbol(meaningfulSpace)){
					break loop;
				}
				break;
			default:
				throw new XecuteException("Error. Not expected token " + t.toString() + "\n" + showError());
		}
		
		// 
		if (temp != null){ //operand found?
			if (result!=null) { // is there already an operand found before this temp?

				while (!operatorStack.isEmpty()){ //

					int operator = operatorStack.pop();
					
					if ( isUnaryOperator(operator) ) { //check unary operators first
						if (isAmbiguousOperators(operator)){
							if ( operatorStack.isEmpty() ) { // in fact this is a binary operator 
								result = new Expression(operator, result, temp);
								continue; // must stop after this
							}
							//adjust ambiguous operators
							operator = (operator == Expression.OP_ADD) ? Expression.OP_POS : operator;
							operator = (operator == Expression.OP_SUB) ? Expression.OP_NEG : operator; 
						} 
						result = new Expression(operator, result);
						continue; //go for more unary operators
					}
					//from here, should have only binary operator
					result = new Expression(operator, result, temp);
				}
				
			} else {
				
				if (!operatorStack.isEmpty()){ //must be unary
					int operator = operatorStack.pop();
					//adjust ambiguous operators
					operator = (operator == Expression.OP_ADD) ? Expression.OP_POS : operator;
					operator = (operator == Expression.OP_SUB) ? Expression.OP_NEG : operator;
					result = new Expression(operator, temp);
				} else {
					result = temp; // keep this for future use or to return.
				}
			}
		}
	} //end loop: while 

	return result;
}

	public boolean hasMoreTokens() {
		return currentPos < buffer.length;
	}

     /** check if there is a numeric value ahead. In MUMPS numerics can start with dot . */
		  public boolean isNumericAhead() {
			boolean result;
			if (result=isDigitOrDot()){
				if (isSymbol('.')){
					char nextC = nextChar();
					result = ((nextC >= '0') && (nextC <= '9'));
				}
			}
			return result;
		}

    /** Return true if  char is symbol. */
    @Override
	public boolean isSymbol(char c) {
    	try {
    		return (c == buffer[currentPos]);			
		} catch (Exception e) {
			return false;
		}
	}

    /**
     * advance to the first character other than space.
     */
    public void leftTrim() {
    	while (hasMoreTokens() && isSpace())
			currentPos++;
    }

    //REVIEW - what about lookAheadToken or simply getToken
    public Token nextToken() {
    	return nextToken(false);
    }
    
    public Token nextToken(boolean meaningfulSpace) {
    	return nextToken(meaningfulSpace, 0);
    }

    /**
     * This is the main method of this class, return the "next" token from
     * the current tokenizer buffer. If the token isn't recognized
     * a NOTPREDICTED token will be returned.
     * 
     * meaningfulSpace is a flag for use after parsing a command name.
     * 
     */
    public Token nextToken(boolean meaningfulSpace, int expectedToken) {
        /*
         * Always return a token, even if it is just EOL
         */
        if (currentPos >= buffer.length)
            return new Token(EOL,currentPos,'\0');

        previousPos = currentPos;
        
    	if (!meaningfulSpace){
    		leftTrim(); // eat space which have no meaning
    	}
        
    	//SYMBOLs - terminators
    	if (isTerminatorSymbol(meaningfulSpace)){
    		return new Token(SYMBOL,currentPos,buffer[currentPos]);
    	}
			
		//OPERATORs
    	mark();		// mark current position in buffer, in case of operators with 2 or 3 characters.
    	int operatorSubType = operator();
		if (operatorSubType > 0){ //operator found!
			currentPos++; //eat last char of this operator
    		return new Token(OPERATOR, operatorSubType, markPos, currentPos, buffer);
    	}
		
		//LITERALs
    	if (isSymbol('"')){	//String Literal
        	return parseStringLiteral();
    	}
		if (isNumericAhead()){ //Numeric Literal
			return parseNumericLiteral();
		}
		
		//VARIABLES local and global
    	if (isLetter() || isSymbol('%') || isSymbol('^'))
    	{
    	    return parseVariableName();
    	}
  
    	//FUNCTIONs intrinsic ($) and extrinsic ($$)
    	if (isSymbol('$'))
    	{
    		currentPos++; //eat first $
    		if (isSymbol('$')){ // another one, so it's extrinsic function or extrinsic variable.
    			currentPos++; //eat second $
    			return  parseFunctionName();
    		} else {
    			Token t = parseIntrinsicFunctionName();
    			if (t.getType() == NOTPREDICTED){ //Then should be a MUMPS system variable.
    				//TODO
    			}
    			return t;
    		}
    	}
    	//##class
    	if (isSymbol('#')){
    		return  parseClassMethodName();
    	}
    		
    	return new Token(NOTPREDICTED, currentPos, asString());
    }

    public Token parseFunctionName() {
    	return parseFunctionName(ExpressionFunction.EXTRINSIC);
    }
    
    public Token parseFunctionName(int subtype) {
			previousPos = currentPos ; //save position
			
			String data = asString();
			Matcher m = namePattern.matcher(data);
			if (m.find()) { // label
				currentPos+= m.end(); //eat label name part
			}
			if (isSymbol('^')){ // Routine part.
				data = asString();
				m = caretNamePattern.matcher(data);
				m.find();
				currentPos+= m.end(); //eat routine name part
			}
			
			return new Token(FUNCTION, subtype ,previousPos, currentPos, buffer);
		}

    /**
     * show the point where an error happened.
     */
    public String showError() {
        int errorPos = previousPos;
        currentPos = 0;
        String txt = asString();
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(txt);
        sb.append("\n");
        for (int i = 0; i < errorPos; i++) {
            sb.append('-');
        }
        sb.append('^');
        return sb.toString();
    }
	public char spyNextTrimChar() {
    	int next = currentPos+1;
    	if(buffer.length-1>next){
    		return String.valueOf(buffer).substring(next).trim().charAt(0);
    	}else{
    		return Character.toChars(0)[0];
    	}
    }
    /**
     * Give back the last token, basically a reset to this token's start. 
     */
    public void unGetToken() {
        if (currentPos != previousPos) {
            currentPos = previousPos;
        }
    }
    
    private boolean isAmbiguousOperators(int operator) {
		return (operator == Expression.OP_POS || operator == Expression.OP_NEG
				|| operator == Expression.OP_ADD || operator == Expression.OP_SUB);
	}

    /** Return true if char is between 0 and 9 */
    private boolean isDigit() {
    	char c = buffer[currentPos];
        return ((c >= '0') && (c <= '9'));
    }

    /** Return true if char is between 0 and 9 or is a dot '.' */
    private boolean isDigitOrDot() {
        return (isDigit() || (buffer[currentPos] == '.'));
    }
    
	/** return true if char is between a-z or A=Z */
	private boolean isLetter() {
		char c = buffer[currentPos];
	    return (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')));
	}

	/** Return true if  char is whitespace. */
	private boolean isSpace() {
		char c = buffer[currentPos];
	    return ((c == ' ') || (c == '\t'));
	}

	private boolean isTerminatorSymbol(boolean meaningfulSpace) {
		return (
				isSymbol(';') || 
				isSymbol(',') || 
				isSymbol(':') || 
				isSymbol(')') ||
				isSymbol('(') ||
				(isSymbol(' ') && meaningfulSpace)
				); 
	
	}

	/**
	 *  If there is not char ahead returns end of string '\0'.
	 */
	private char nextChar() {
		char result='\0';
		int originalPosition = currentPos; //keep original position
		currentPos++;
		if (hasMoreTokens()){
			result = buffer[currentPos];	
		}
		currentPos = originalPosition; //return to original position
		return result;
	}

	/**
	 * try to extract an operator. returns 0 if not found.
	 */
	private final int operator()                                  
	{ 
	  boolean not = false;
	  char c = buffer[currentPos];
	  if (c == '\''){ // a NOT? 
	    not = true;							// set the not flag
	    currentPos++;                  		// get next char
	  }
		
	  switch(c)
	  { 
	  	case '\'':							// can't have two
	  		if (not){
	  			currentPos--;
	  			return Expression.OP_NOT;		
	  		}
	  	case '+':                                   // add (binary) or positive (unary)
	      if (not) return 0;						// a not here is junk
	      return Expression.OP_ADD;                 
	    case '-':                                   //subtract (binary) or negative(unary)
	      if (not) return 0;			
	      return Expression.OP_SUB;
	    case '*':
	      if (not) return 0;
	      if (nextChar() == '*'){                   // if there is another it's a power (Exponentiation)
			  currentPos++;
	    	  return Expression.OP_EXP;
		  }
	      return Expression.OP_MUL;                 // set as a multiply
	    case '/':                                   
	      if (not) return 0;			
	      return Expression.OP_DIV;     
	    case '\\':                                  // back-slash
	      if (not) return 0;
	      return Expression.OP_IDIV;                // integer divide
	    case '#':                                   // hash
	      if (nextChar() == '#'){
	    	  return 0;			// Perhaps a class method (##class), not an operator.
	      }
	      if (not){
	    	  return 0;
	      }
	      return Expression.OP_MOD;                 // modulus
	    case '_':                                   // underscore
	      if (not) return 0;						
	      return Expression.OP_CONC;                // concatenate
	    case '=':                                   // equal or not
	      return not ? Expression.OP_NEQ : Expression.OP_EQ;	
	    case '<':                                   // less than or not
	      return not ? Expression.OP_NLT : Expression.OP_LT;	
	    case '>':                                   // greater than or not
	      return not ? Expression.OP_NGT: Expression.OP_GT;              
	    case '&':                                   // AND short circuit, AND or Not AND
		  if (nextChar() == '&'){
			if (not) return 0;
			currentPos++;
			return Expression.OP_SAND;
		  }
	      return not ? Expression.OP_NAN : Expression.OP_AND;              
	    case '!':                                   // or or nor
	      return not ? Expression.OP_NOR : Expression.OP_OR;   
		case '|':									
			if (not) return 0;
			if (nextChar() == '|'){					// or short circuit.
				currentPos++;
				return Expression.OP_SOR;
			}
	    case '[':                                   // contains or not
	      return not ? Expression.OP_NCON : Expression.OP_CONT;              
	    case ']':                                   
	      if (nextChar() == ']'){               	// if there is another
	      	currentPos++;                           // advance the pointer
	        return not ? Expression.OP_NSOR : Expression.OP_SORA;	// sorts after or not
	      }
	      return not ? Expression.OP_NFOL : Expression.OP_FOLL;     // follows or not
	    case '?':                                   
	      return not ? Expression.OP_NPAT : Expression.OP_PATT;		// matches or not
	    
	  }
	  if (not) {
			currentPos--;
	  		return Expression.OP_NOT;	
	  }
	  return 0;                                 // Not an operator.
	}

	// Parse names like ##class(package.class).methodName(
	private Token parseClassMethodName() {
		StringBuffer result = new StringBuffer();
		
		previousPos = currentPos ; //save position
	
		currentPos+= 8; //eat '##class('
	
		String data = asString().toLowerCase();
	Pattern closeParetheses = Pattern.compile("^([^\\)]*)");
	Matcher m = closeParetheses.matcher(data);
	
	if (!m.find()) { 
		throw new XecuteException("Unexpected class name: " + showError());
	}
	
	result.append(new String(buffer, currentPos, m.end()));
	
	currentPos+= m.end(); //eat class Name
	currentPos++;		 // eat )	
	
	if (!isSymbol('.')){ //Must be followed by a dot.
		throw new XecuteException("Unexpected classMethod reference: " + showError());
	}
	result.append(".");
	currentPos++;	
	// Method part.
	data = asString();
	m = namePattern.matcher(data);
	if (!m.find()){
		throw new XecuteException("Unexpected Method name: " + showError());
	}
	result.append(new String(buffer, currentPos, m.end()));
	
	currentPos+= m.end(); //eat method name part
	
	return new Token(FUNCTION, previousPos, ExpressionFunction.CLASSREF , result.toString());
}

	private Token parseIntrinsicFunctionName() {
    		Token result;
    		
    		previousPos = currentPos ; //save position
    		
    		if ( (currentPos >= buffer.length) || (isSymbol(';')) ){
    			return new Token(EOL,currentPos,'\0');
    		}
    		
    		String data = asString().toLowerCase();
    		boolean match = false;
    		Matcher m = null;
    		int intrinsicFunctionType;
    		
    		for (intrinsicFunctionType = 0; intrinsicFunctionType < ExpressionFunction.patterns.length; intrinsicFunctionType++) { //for each command try
    			m = ExpressionFunction.patterns[intrinsicFunctionType].matcher(data);
    			if (m.find()) {
    				match = true;
    				break;
    			}
    		}
    		//
    		if (!match) {
    			result = new Token(NOTPREDICTED, 0, previousPos, currentPos, buffer);
    		} else {
    			int functionNameLength = m.end() - (data.contains("(")?1:0); //back 1 position for removing '(' that always follow an intrinsic function.
    			currentPos+= functionNameLength; 
    			result = new Token(FUNCTION, ExpressionFunction.INTRINSIC ,intrinsicFunctionType, previousPos, currentPos, buffer);
    		}
    		return result;
	}

    private Token parseNumericLiteral() {
			previousPos = currentPos; // save position
			;
			int dotCounter=0;
			while (hasMoreTokens() && isDigitOrDot()){
				if (isSymbol('.')) {
					dotCounter++;
					if (dotCounter > 1){
						break;
					}
				}
				currentPos++;
			}
			return new Token(LITERAL, ExpressionLiteral.NUMERIC , previousPos , currentPos, buffer);
		}
    
   
    private Token parseStringLiteral() {
			previousPos = currentPos; // save position
			currentPos++; // eat first double quotes " .
			boolean endFound = false;
			while (hasMoreTokens()){
				if (isSymbol('"')){	
					if (nextChar() == '"'){//escaped in MUMPS style.
						currentPos++;
						continue;
					}
					
					endFound = true;
					break;
				}
				currentPos++;
			}
			if (!endFound){
				throw new XecuteException("End of literal string not found: \n"+ showError());
			}
			currentPos++;
			return new Token(LITERAL, ExpressionLiteral.STRING , previousPos , currentPos, buffer);
		}
    
    private Token parseVariableName() {
		  	previousPos = currentPos; // save position
		  	String data = asString();
			Matcher m = caretNamePattern.matcher(data);
			if (!m.find()) {
				throw new XecuteException("Unexpected variable name: " + showError());
			}
			currentPos+= m.end(); 
			return new Token(VARIABLE, previousPos, currentPos, buffer);
			
		}
	
	/**
     * Return the buffer from the current position to the end as a string.
     */
    String asString() {
        int pos = currentPos;
        while (pos < buffer.length)
        	pos++;
        return new String(buffer, currentPos, pos - currentPos);
    }
	/**
	 * Set's the current "mark" so that the line can be re-parsed from this
	 * point.
	 */
	void mark() {
		markPos = currentPos;
	}


	/** 
	 * try to find a command name. 
	 * returns just EOL or COMMAND tokens*/
	Token parseCommandName() {
	Token result;
	
	previousPos = currentPos ; //save position
	
	if ( (currentPos >= buffer.length) || (isSymbol(';')) ){
		return new Token(EOL,currentPos,'\0');
	}
	
	String data = asString().toUpperCase();
	boolean match = false;
	Matcher m = null;
	int commandSubType;
	for (commandSubType = 0; commandSubType < CommandExpression.patterns.length; commandSubType++) { //for each command try
		m = CommandExpression.patterns[commandSubType].matcher(data);
		if (m.find()) {
			match = true;
			break;
		}
	}
	//
	if (!match) {
		result = new Token(NOTPREDICTED, 0, previousPos, currentPos, buffer);
	} else {
		int commandLength = m.end() - 1; //1 for ' ' or ':' that always follow a command token.
		currentPos+= commandLength; 
		result = new Token(COMMAND, commandSubType,previousPos, currentPos, buffer);
	}
	
	return result;
}
	
    /**
		 * Reset the line pointer to the mark for re-parsing.
		 */
		void resetToMark() {
			currentPos = markPos;
		}
	
	


}
