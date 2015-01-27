package mLibrary.implementation.xecute.node;

import java.util.regex.Pattern;

import mLibrary.mContext;
import mLibrary.implementation.xecute.LexicalTokenizer;

public class CommandExpression {

	/**
	 * These are all the commands we can parse.
	 */
	public final static String commandNames[] = { "if", "do", "set", "write" };
	public final static int DO = 1;
	/**
	 * constants for type of commands. This constants should match the indexes of
	 * the above arrays. commandNames[] and patterns[].
	 */
	public final static int IF = 0;
	public final static Pattern[] patterns = { Pattern.compile("^(IF|I)[ ]{1}"), Pattern.compile("^(DO|D)[ :]{1}"),
			Pattern.compile("^(SET|S)[ :]{1}"), Pattern.compile("^(WRITE|W)[ :]{1}"), Pattern.compile("^(USE|U)[ :]{1}") };
	public final static int SET = 2;

	public final static int USE = 4;

	public final static int WRITE = 3;

	protected int command; // type of command.

	protected LexicalTokenizer lt;

	protected mContext m$;

	protected CommandExpression next; // for use in case of chained commands.
	protected Expression postCondition; // postConditional of most commands and
																			// the actual condition of IF command.
	public CommandExpression() {
	}
	public CommandExpression(LexicalTokenizer ltr) {
		lt = ltr;
		m$ = ltr.getContext();
	}
	/**
	 * Constructors
	 */
	public CommandExpression(LexicalTokenizer ltr, int commandType) {
		lt = ltr;
		m$ = ltr.getContext();
		command = commandType;
	}

	public void eval() {
		CommandExpression c = this.next;
		do {
			c.eval();
		}
		while ((c = getNextDifferentCommand(c)) != null);
	}

	/**
	 * get the last command inserted. In this case "next" must be pointing at null
	 */
	public CommandExpression getLastCommand() {
		return (this.next == null) ? this : this.next.getLastCommand();
	}

	public void push(CommandExpression c) {
		getLastCommand().next = c;
	}

	/**
	 * Return a string representation of this.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MUMPS Command :");
		sb.append(commandNames[command]);
		if (next != null) {
			sb.append(" ");
			sb.append(next.toString());
		}
		return sb.toString();
	}

	private CommandExpression getNextDifferentCommand(CommandExpression c) {

		if (c.next == null || c instanceof CommandIf) {
			return null;
		}
		String type = c.getClass().getName();
		if (type.equals(c.next.getClass().getName())) {
			return c.next.getNextDifferentCommand(c.next);
		}
		return c.next;
	};

}
