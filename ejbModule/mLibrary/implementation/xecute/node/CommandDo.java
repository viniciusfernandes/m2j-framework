package mLibrary.implementation.xecute.node;

import mLibrary.m$op;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.LexicalTokenizer;
import mLibrary.implementation.xecute.Token;

public class CommandDo extends CommandExpression {

	private static void parse(CommandDo c, LexicalTokenizer lt,
			boolean meaningfulSpace) {
		Token t;

		if (meaningfulSpace) {
			t = lt.nextToken(meaningfulSpace);
		} else {
			t = lt.parseFunctionName(ExpressionFunction.ROUTINEREF);
		}

		if (t.getType() == Token.EOL) // comments ';' are interpreted as EOL as
										// well.
			return;

		if (lt.isSymbol(':')) { // ':' Post condition
			if (!meaningfulSpace) { // if is second command in line. it should
									// happens only from second command on.
				throw new XecuteException(
						"SYNTAX Error unexpected post condition token ':' - \n"
								+ lt.showError());
			}
			lt.currentPos++; // eat ':'
			c.postCondition = lt.getExpression(meaningfulSpace);
		}

		if (lt.isSymbol(' ')) {
			lt.currentPos++; // eat space
			t = lt.parseFunctionName(ExpressionFunction.ROUTINEREF);
		} else {
			if (meaningfulSpace) { // should have one space only for the first
									// command
									// in a line. from second on spaces are not
									// valid tokens
				throw new XecuteException("SYNTAX Error ." + lt.showError());
			}

		}

		if (!t.isFunction()) {
			throw new XecuteException("SYNTAX Error - Function expected. "
					+ lt.showError());
		}

		c.function = new ExpressionFunction(t, lt);
		int backUpPosition = lt.currentPos;
		t = lt.nextToken();
		if (t.isSymbol(',')) { // get next set command and put it into next
								// attribute.
			lt.currentPos++; // eat commas
			lt.leftTrim();
			c.next = new CommandDo(lt, false);
		} else {
			lt.currentPos = backUpPosition;
		}
	}

	ExpressionFunction function;

	public CommandDo(LexicalTokenizer ltr, boolean meaningfulSpace) {
		super(ltr, DO);
		parse(this, lt, meaningfulSpace);
	}

	public void eval() {
		// CommandExpression c = this;
		if (postCondition != null) {
			if (!m$op.Logical(postCondition.eval())) {
				return;
			}
		}
		;
		try {
			m$.Do(this.function.getFunctionName(),
					this.function.evalArguments());
		} catch (Exception e) {
			throw new IllegalStateException(
					"Fail to execute command do from xecute. Function: "
							+ this.function.getFunctionName()
							+ " and parameters: "
							+ this.function.evalArguments());
		}
		if (this.next instanceof CommandDo) {
			this.next.eval();
		}
	}

}
