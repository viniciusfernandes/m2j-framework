package mLibrary.implementation.xecute.node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import mLibrary.mContext;
import mLibrary.mVar;
import mLibrary.exceptions.CommandException;
import mLibrary.exceptions.XecuteException;
import mLibrary.implementation.xecute.LexicalTokenizer;
import mLibrary.implementation.xecute.Token;

public class ExpressionFunction extends Expression {

	/** Expression is a ##class Reference. */
	public static final int CLASSREF = 4;
	/** Expression is an EXTRINSIC function $$ */
	public static final int EXTRINSIC = 2;
	/**
	 * This constants should match the indexes of the above arrays.
	 * functionNames[] and patterns[].
	 */
	public final static int FN_ASCII = 0;

	public final static int FN_CHAR = 1;
	public final static int FN_DATA = 2;
	public final static int FN_EXTRACT = 3;
	public final static int FN_FIND = 4;

	// the attribute 'oper' which was extended from super class, holds this
	// function Type

	public final static int FN_FNUMBER = 5;

	public final static int FN_GET = 6;

	public final static int FN_HOROLOG = 7;

	public final static int FN_INCREMENT = 8;

	public final static int FN_JUSTIFY = 9;

	public final static int FN_LENGTH = 10;

	public final static int FN_LISTBUILD = 11;
	public final static int FN_ORDER = 12;
	public final static int FN_PIECE = 13;
	public final static int FN_QSUBSCRIPT = 15;
	public final static int FN_QUERY = 14;
	public final static int FN_REVERSE = 16;
	public final static int FN_SELECT = 17;
	public final static int FN_TRANSLATE = 18;
	public final static int FN_ZCONVERT = 25;
	public final static int FN_ZDATE = 19;
	public final static int FN_ZDATEH = 20;
	public final static int FN_ZDATETIME = 21;
	public final static int FN_ZDATETIMEH = 22;
	public final static int FN_ZERROR = 23;
	public final static int FN_ZHOROLOG = 24;
	public final static int FN_ZTIME = 27;
	public final static int FN_ZTIMESTAMP = 26;
	public final static int FN_ZUTIL = 28;
	public final static String functionNames[] = { "$ascii", "$char", "$data", "$extract", "$find", "$fnumber", "$get",
			"$horolog", "$increment", "$justify", "$length", "$listbuild", "$order", "$piece", "$query", "$qsubscript",
			"$reverse", "$select", "$translate", "$zdate", "$zdateh", "$zdatetime", "$zdatetimeh", "$zerror", "$zhorolog",
			"$zconvert", "$ztimestamp", "$ztime", "$zutil" };
	/** Expression is an INTRINSIC function $ */
	public static final int INTRINSIC = 1;
	public final static Pattern[] patterns = {

	Pattern.compile("^(ascii|a)\\({1}"), Pattern.compile("^(char|c)\\({1}"), Pattern.compile("^(data|d)\\({1}"),
			Pattern.compile("^(extract|e)\\({1}"), Pattern.compile("^(find|f)\\({1}"),
			Pattern.compile("^(fnumber|fn)\\({1}"), Pattern.compile("^(get|g)\\({1}"), Pattern.compile("^(horolog|h){1}"),
			Pattern.compile("^(increment|i)\\({1}"), Pattern.compile("^(justify|j)\\({1}"),
			Pattern.compile("^(length|l)\\({1}"), Pattern.compile("^(listbuild|lb)\\({1}"),
			Pattern.compile("^(order|o)\\({1}"), Pattern.compile("^(piece|p)\\({1}"), Pattern.compile("^(query|q)\\({1}"),
			Pattern.compile("^(qsubscript|qs)\\({1}"), Pattern.compile("^(reverse|re)\\({1}"),
			Pattern.compile("^(select|s)\\({1}"), Pattern.compile("^(translate|tr)\\({1}"),
			Pattern.compile("^(zdate|zd)\\({1}"), Pattern.compile("^(zdateh|zdh)\\({1}"),
			Pattern.compile("^(zdatetime|zdt)\\({1}"), Pattern.compile("^(zdatetimeh|zdth)\\({1}"),
			Pattern.compile("^(zerror|ze){1}"), Pattern.compile("^(zhorolog|zh){1}"),
			Pattern.compile("^(zconvert|zcvt)\\({1}"), Pattern.compile("^(ztimestamp|zts)\\({1}"),
			Pattern.compile("^(ztime|zt)\\({1}"), Pattern.compile("^(zutil|zu)\\({1}"), };
	/** Expression is a ROUTINE Reference, which is called by DO command. */
	public static final int ROUTINEREF = 3;
	private static void parse(ExpressionFunction fnc, LexicalTokenizer lt) {
		if (!lt.hasMoreTokens())
			return;
		// must not have space between functionName and subscripts
		if (lt.isSymbol('(')) {
			// get arguments

			do {
				lt.currentPos++; // eat '(' or ','
				fnc.listArgs.add(lt.getExpression()); // get arguments
				// recursively.
				lt.nextToken();
			}
			while (lt.isSymbol(','));

			if (!lt.isSymbol(')')) {
				throw new XecuteException("Error" + lt.showError() + "expected ')' ");
			}
			lt.currentPos++;// eat ')'
		}
	}
	protected mContext m$;
	String className;
	String functionName;
	Integer intrinsicType;
	List<Expression> listArgs;
	String methodName;

	int subType;

	/** Constructor for FUNCTION. */
	public ExpressionFunction(Token t, LexicalTokenizer lt) {
		super(t.getType(), null);
		this.subType = t.getSubType();
		this.intrinsicType = t.getIntrinsicType();
		String name = t.getValue();
		if (subType == CLASSREF) {
			int lastDot = name.lastIndexOf(".");
			this.className = name.substring(0, lastDot);
			this.methodName = name.substring(lastDot + 1);
		}
		else {
			this.functionName = t.getValue();
		}

		this.listArgs = new ArrayList<Expression>();
		this.m$ = lt.getContext();
		if (t.getSubType() == EXTRINSIC || t.getSubType() == ROUTINEREF || t.getSubType() == CLASSREF) {
			parseArguments(this, lt);
		}
		else {
			parse(this, lt);
		}
	}

	//
	public Object[] evalArguments() {
		return evalArguments(-1);
	}

	public Object[] evalArguments(int except) {
		Object[] result = new Object[listArgs.size()];
		int i = 0;
		for (Expression exp : listArgs) {
			if (i == except) {
				continue;
			}
			if (exp instanceof ExpressionVariable) {
				ExpressionVariable var = (ExpressionVariable) exp;
				if (var.oper == ExpressionVariable.BYREF) {
					result[i] = var.evalToMVar();
				}
				else {
					result[i] = var.eval();
				}

			}
			else {
				result[i] = exp.eval();
			}

			i++;
		}
		return result;
	}

	public Class[] evalArgumentTypes(Object[] args) {
		Class<Object>[] result = new Class[args.length];
		int i = 0;
		for (Object exp : args) {
			// result[i] = exp.eval().getClass();
			result[i] = (Class<Object>) exp.getClass();
			i++;
		}
		return result;
	}

	//
	public Class[] evalArgumentTypesOld() {
		Class<Object>[] result = new Class[listArgs.size()];
		int i = 0;
		for (Expression exp : listArgs) {
			// result[i] = exp.eval().getClass();
			result[i] = Object.class;
			i++;
		}
		return result;
	}

	/** convert functionName in MUMPS format to mumps2java-api format. */
	public String getFunctionName() {
		String result;
		if (subType == CLASSREF) {
			result = className + "." + methodName;
		}
		else {
			if (functionName.contains("^")) {
				String[] names = functionName.split("\\^");
				result = names[1] + "." + (names[0].isEmpty() ? "main" : names[0]);
			}
			else {
				result = functionName;
			}
		}
		return result;
	}

	// For use in SET $piece and $extract
	public mVar getMvarAt(int Position) {
		if (!hasArguments()) {
			return null;
		}

		return ((ExpressionVariable) listArgs.get(Position)).evalToMVar();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (subType == INTRINSIC) {
			// sb.append("$");
			sb.append(functionNames[intrinsicType]);
		}
		else if (subType == EXTRINSIC) {
			sb.append("$$");
			sb.append(functionName);
		}
		else if (subType == CLASSREF) {
			sb.append("##class(");
			sb.append(className);
			sb.append(").");
			sb.append(methodName);
		}
		else {
			sb.append(functionName);
		}
		if (!this.listArgs.isEmpty()) {
			sb.append("(");
			for (Expression element : listArgs) {
				sb.append(element.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		return sb.toString();
	}

	private Object dispatchIntrinsicFunction(Object... args) {
		Object result = null;

		// Class[] argTypes = evalArgumentTypes(args); //{ String[].class };
		Method intrinsicFunction;
		try {
			String methodName = functionNames[intrinsicType];
			Method[] methodArray = m$.getFunction().getClass().getDeclaredMethods();
			for (int i = 0; i < methodArray.length; i++) {
				intrinsicFunction = methodArray[i];
				if (intrinsicFunction.getName().equals(methodName)) {
					try {
						result = intrinsicFunction.invoke(m$.getFunction(), args);
						break;
					}
					catch (IllegalArgumentException e) {
						continue;
					}
				}
			}
			if (result == null) {
				throw new CommandException("Xecute Method " + methodName + " with parameters " + Arrays.toString(args)
						+ " not found in mFunction");
			}
			// getDeclaredMethod(functionNames[intrinsicType], argTypes);

		}
		catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private Object[] evalArguments(String fncName) {
		List<Object> result = new ArrayList<Object>();
		result.add(fncName);
		result.addAll(Arrays.asList(evalArguments(-1)));
		return result.toArray();
	}

	private Object evalIntrinsic() {
		switch (intrinsicType) {
		case FN_PIECE:

			return dispatchIntrinsicFunction(evalArguments());

		case FN_GET:
			Object[] args = evalArguments(0); // except first (0) argument which
																				// should be an mVar.
			args[0] = getMvarAt(0);
			return dispatchIntrinsicFunction(args);

		case FN_ASCII:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_CHAR:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_DATA:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_EXTRACT:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_FIND:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_FNUMBER:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_HOROLOG:
			return dispatchIntrinsicFunction(new Object[] {});
		case FN_INCREMENT:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_JUSTIFY:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_LENGTH:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_LISTBUILD:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ORDER:
			return dispatchIntrinsicFunction(getMvarAt(0));
		case FN_QUERY:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_QSUBSCRIPT:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_REVERSE:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_SELECT:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_TRANSLATE:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZDATE:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZDATEH:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZDATETIME:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZDATETIMEH:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZERROR:
			return dispatchIntrinsicFunction(new Object[] {});
		case FN_ZHOROLOG:
			return dispatchIntrinsicFunction(new Object[] {});
		case FN_ZCONVERT:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZTIMESTAMP:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZTIME:
			return dispatchIntrinsicFunction(evalArguments());
		case FN_ZUTIL:
			return dispatchIntrinsicFunction(evalArguments());
		default:
			throw new XecuteException("FUNCTION " + functionNames[intrinsicType] + " not implemented in Xecute!");
		}
	}

	private boolean hasArguments() {
		return (!this.listArgs.isEmpty());
	}

	private void parseArguments(ExpressionFunction fnc, LexicalTokenizer lt) {
		if (lt.hasMoreTokens() && lt.isSymbol('(')) {
			// get arguments

			do {
				lt.currentPos++; // eat first '(' or ','
				lt.leftTrim();
				if (lt.isSymbol(')')) {
					break;
				}
				Expression argument;
				if (lt.isSymbol(',')) { // commas again, so the argument was omitted
					argument = new ExpressionLiteral(ExpressionLiteral.NULL, null);
				}
				else if (lt.isSymbol('.') && !lt.isNumericAhead()) { // argument byRef
					lt.currentPos++; // eat dot
					argument = lt.getExpression();
					if (!(argument instanceof ExpressionVariable)) {
						throw new XecuteException("Error parsing ByRef variable " + lt.showError());
					}
					// change the argument subType
					((ExpressionVariable) argument).makeByRef();
				}
				else {
					argument = lt.getExpression(); //
					if (argument == null) { // close parentheses ')' was found right after
																	// the open. There is no arguments
						argument = new ExpressionLiteral(ExpressionLiteral.NULL, null);
					}
				}
				// put in listArgs
				fnc.listArgs.add(argument); //

				lt.nextToken();
			}
			while (lt.isSymbol(','));

			if (!lt.isSymbol(')')) {
				throw new XecuteException("Error" + lt.showError() + "expected ')' ");
			}
			lt.currentPos++;// eat last ')'
		}
	}

	@Override
	Object eval() {
		switch (subType) {
		case INTRINSIC:
			return evalIntrinsic();
		case EXTRINSIC:
			return m$.call(evalArguments(getFunctionName()));
		case ROUTINEREF:
			throw new XecuteException("NOT IMPLEMENTED! ");
		case CLASSREF:
			return m$.call(evalArguments(getFunctionName()));
		default:
			throw new XecuteException("NOT IMPLEMENTED! ");
		}

	}

}
