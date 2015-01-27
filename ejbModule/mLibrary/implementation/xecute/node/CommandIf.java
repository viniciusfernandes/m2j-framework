package mLibrary.implementation.xecute.node;

import mLibrary.m$op;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.LexicalTokenizer;
import mLibrary.implementation.xecute.Token;

public class CommandIf extends CommandExpression {

	/**
	 * IF <Expression> [','<Expression>] 
	 * if has just an expression to evaluate.
	 * ',' commas are like && in if conditions.
	 * 
	 * TODO - parse blocks { }
	 * */ 
	private static void parse(CommandIf c, LexicalTokenizer lt, boolean meaningfulSpace) {
		Token t = lt.nextToken(meaningfulSpace);
		if (t.getType() == Token.EOL) // comments ';' are interpreted as EOL as well. 
			return;
		//must have a space
		if (!lt.isSymbol(' ')) {
			throw new XecuteException("SYNTAX Error ." + lt.showError());
		}
		lt.currentPos++; //eat space
		
		// we use postCondition attribute to hold the actual condition, since if has not post conditional.
		c.postCondition = lt.getExpression(meaningfulSpace);
		if (c.postCondition == null){
			throw new XecuteException("SYNTAX Error - if condition expected." + lt.showError());
		}
		int backupPosition = lt.getCurrentPos();
		t = lt.nextToken();
		if (t.isSymbol(',')){
			do { 	// get next condition.
				lt.currentPos++; 	// eat commas
				c.postCondition = new Expression(Expression.OP_SAND, c.postCondition, lt.getExpression());
				backupPosition = lt.getCurrentPos();
				t = lt.nextToken();
				if (!t.isSymbol(',')){
					lt.currentPos = backupPosition;
				}
			} while (t.isSymbol(','));
		} else { //command ahead
			lt.currentPos = backupPosition;
		}
	}
	

	public CommandIf(LexicalTokenizer ltr, boolean meaningfulSpace) {
		super(ltr, IF);
		parse(this, ltr, meaningfulSpace);
	}
		 
	public void eval() {
		if (postCondition != null){
			if (!m$op.Logical(postCondition.eval())){
				return;
				// Apparently there is not ELSE in 'IF' inside xecute.
			}
		} 
		//true
		//then evaluate next statements
		next.eval();
	}



}
