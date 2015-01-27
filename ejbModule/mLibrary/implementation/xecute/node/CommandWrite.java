package mLibrary.implementation.xecute.node;

import mLibrary.m$op;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.LexicalTokenizer;
import mLibrary.implementation.xecute.Token;

public class CommandWrite extends CommandExpression {
	
	//so far, just one variable. 
	// TODO list of variables - g.e: Set (var1,var2,var3(99)) = 0
	// TODO Set $Piece and $Extract.
	// 
	private static void parse(CommandWrite c, LexicalTokenizer lt, boolean meaningfulSpace) {
		Token t = lt.nextToken(meaningfulSpace);
		if (t.getType() == Token.EOL) // comments ';' are interpreted as EOL as well. 
			return;

		if (lt.isSymbol(':')) { // ':' Post condition
			if (!meaningfulSpace) { // it should happens only from second command on.
				throw new XecuteException("SYNTAX Error unexpected post condition token ':' - \n" + lt.showError());
			}
			lt.currentPos++; //eat ':'
			c.postCondition = lt.getExpression(meaningfulSpace);
		}
		if(!meaningfulSpace){
			
		}
		if (lt.isSymbol(' ')) {
			lt.currentPos++; //eat space
			//t = lt.nextToken(meaningfulSpace);
			c.value = lt.getExpression();
			int backupPosition = lt.currentPos;
			t = lt.nextToken();
			if (t.isSymbol(',')) { 	// get next set command and put it into next attribute.
				lt.currentPos++; 	// eat commas
				c.next = new CommandWrite(lt, false);
			} else {
				lt.currentPos = backupPosition;
			}			
		}else if (!meaningfulSpace) {
			c.value = lt.getExpression();
			int backupPosition = lt.currentPos;
			t = lt.nextToken();
			if (t.isSymbol(',')) { 	// get next set command and put it into next attribute.
				lt.currentPos++; 	// eat commas
				c.next = new CommandWrite(lt, false);
			} else {
				lt.currentPos = backupPosition;
			}			
		} else {
			if (meaningfulSpace){    // should have one space only for the first command
									// in a line. from second on spaces are not valid tokens
				throw new XecuteException("SYNTAX Error ." + lt.showError());
			}
		}
				
	} 
	private Expression value;
	
	private Expression variable;

	public CommandWrite(LexicalTokenizer lt, boolean meaningfulSpace) {
		super(lt,WRITE);
		parse(this, lt, meaningfulSpace);
	}

	public void eval() {
		if (postCondition != null){
			if (!m$op.Logical(postCondition.eval())){
				return;
			}
		} 
		
		m$.Cmd.Write(this.value.eval());

		if (this.next instanceof CommandWrite){
			this.next.eval();
		}
	}

}
