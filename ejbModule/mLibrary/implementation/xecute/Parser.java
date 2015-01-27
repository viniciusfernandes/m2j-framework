package mLibrary.implementation.xecute;

import mLibrary.mContext;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.node.CommandDo;
import mLibrary.implementation.xecute.node.CommandExpression;
import mLibrary.implementation.xecute.node.CommandIf;
import mLibrary.implementation.xecute.node.CommandSet;
import mLibrary.implementation.xecute.node.CommandUse;
import mLibrary.implementation.xecute.node.CommandWrite;

public class Parser {

	public static void main(String... args) {
		mContext m = mContext.createSemAcesso();

		Parser p = new Parser(m, "i 1,2 set x=5 set y=2, z=x+y");
		p.parse();
		// System.out.println(m.var("a").getSuppressedNullValue() + "\n" +
		// m.var("b").getSuppressedNullValue() + "\n" + m.var("c").get() );
	}

	// /private List<CommandExpression> commandList; // all commands in a line
	private CommandExpression commandList;
	private String lineCommand; // Input string

	protected final mContext m$;

	public Parser(mContext m, String data) {
		m$ = m;
		lineCommand = data;
		// commandList = new ArrayList<CommandExpression>();
	}

	/*
	 * private void run() {
	 * 
	 * for (CommandExpression command : commandList) { // Estourar laço quando
	 * for IF command. command.eval(); }
	 * 
	 * }
	 */

	/**
	 * START Parse/Execution HERE
	 */
	public void parse() {
		// TODO implementar estratégia de cache para ganho performance aqui.
		commands();

		commandList.eval();
		// run();
	}

	/**
	 * Populates a command chain -> 'commandList'.
	 */
	private void commands() {
		LexicalTokenizer lt = new LexicalTokenizer(lineCommand, m$);
		commandList = new CommandExpression();
		while (lt.hasMoreTokens()) {

			CommandExpression c = doParse(lt);

			if (c == null) {
				break;
			}

			commandList.push(c);
		}
	}

	/**
	 * This method returns a parsed command, it throws an exception if an error
	 * occurred.
	 */
	private CommandExpression doParse(LexicalTokenizer lt) {
		CommandExpression c = null;
		Token t;
		int backupPosition = lt.currentPos;
		lt.leftTrim(); // eat spaces before testing command names.

		t = lt.parseCommandName();

		if (t.getType() == Token.EOL) { // comments ';' are interpreted as EOL as
																		// well.
			lt.currentPos += lt.asString().length(); // enforce end of line, in case
																								// of comments
			return c;
		}

		if (t.getType() != Token.COMMAND) {
			lt.previousPos = backupPosition;
			m$.var("$zerror").set("<Java Exception>" + lt.showError());
			throw new XecuteException("Error :" + lt.showError() + " - command was expected");
		}
		try {
			switch (t.getSubType()) {
			case CommandExpression.SET:
				c = new CommandSet(lt, true);
				break;
			case CommandExpression.IF:
				c = new CommandIf(lt, true); // first set in a line must have space
																			// between 'SET' and 'VARIABLE'
				break;
			case CommandExpression.DO:
				c = new CommandDo(lt, true); // first set in a line must have space
																			// between 'SET' and 'VARIABLE'
				break;
			case CommandExpression.USE:
				c = new CommandUse(lt, true); // first set in a line must have space
																			// between 'SET' and 'VARIABLE'
				break;
			case CommandExpression.WRITE:
				c = new CommandWrite(lt, true); // first set in a line must have space
																				// between 'SET' and 'VARIABLE'
				break;
			default:
				throw new XecuteException("Invalid command: " + t.toString() + "\n : " + lt.showError());
			}
		}
		catch (Exception e) {
			throw new UnsupportedOperationException("Fail in Parse:", e);
		}
		return c;
	}
}
