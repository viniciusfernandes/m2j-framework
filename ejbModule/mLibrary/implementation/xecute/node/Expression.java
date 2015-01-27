package mLibrary.implementation.xecute.node;

import java.io.PrintStream;

import util.mFunctionUtil;
import mLibrary.m$op;
import mLibrary.exceptions.XecuteException;

public class Expression {

	/**
	 * These are the valid operator types.
	 */
	// Math
	/** Addition (Binary)    '+' */
	public final static int OP_ADD  = 1;
	// Logical
	/** Logical AND          '&' */
	public final static int OP_AND  = 8;

	// Aggregation
	/** Concatenation '_' */
	public final static int OP_CONC = 28;  
	/** Contains 		'[' */
	public final static int OP_CONT = 20;  
	/** Division             '/' */
	public final static int OP_DIV  = 4;  
	// Comparison
	/** Equality '=' */
	public final static int OP_EQ   = 14;  
	/** Exponentiation       '**' */
	public final static int OP_EXP  = 6;  
	/** Follows 		']' */
	public final static int OP_FOLL = 22;  
	/** Greater than '>' */
	public final static int OP_GT   = 16;  
	
	/** Integer Division     '\' */
	public final static int OP_IDIV = 5;  
	/** Indirection          '@' */
	public final static int OP_IND  = 32;  
	/** Less than '<' */
	public final static int OP_LT   = 18; 
	/** Modulus (remainder)  '#' */
	public final static int OP_MOD  = 7; 
	/** Multiplication       '*' */
	public final static int OP_MUL  = 3; 
	/** Logical NOT AND          "'&" */
	public final static int OP_NAN  = 9; 
	
	/** Not Contains  "'[" */
	public final static int OP_NCON = 21; 
	/** Negative (Unary)     '-' */
	public final static int OP_NEG  = 31; 
	/** Non-equality "'=" */
	public final static int OP_NEQ  = 15; 
	/** Not Follows    "']" */
	public final static int OP_NFOL = 23; 
	/** Not Greater than (less than or equal) "'>" */
	public final static int OP_NGT  = 17; 
	/** Not Less than (greater than or equal) "'<" */
	public final static int OP_NLT  = 19; 

	/** Logical NOT OR       "'!" */
	public final static int OP_NOR  = 12; 
	// Unary
	/** Unary negation       "'" */
	public final static int OP_NOT  = 29; 
	/** Pattern does not match "'?" */
	public final static int OP_NPAT = 27; 
	/** Not Sorts After "']]" */
	public final static int OP_NSOR = 25; 
	/** Logical OR           '!' */
	public final static int OP_OR   = 11; 
	/** Pattern match      '?' */
	public final static int OP_PATT = 26; 
	
	/** Positive (Unary)     '+' */
	public final static int OP_POS  = 30; 
	/** Logical AND (short-circuit) '&&' */
	public final static int OP_SAND = 10; 
	
	/** Logical OR (short-circuit) '||' */
	public final static int OP_SOR  = 13; 
	
	/** Sorts After    ']]' */
	public final static int OP_SORA = 24; 
	/** Subtraction (Binary) '-' */
	public final static int OP_SUB  = 2; 
	/**
     * the constants above should match the indexes of this opCodes.
     */
	public final static String opCodes[] = { null, "+", "-", "*", "/", "\\",
			"**", "#", "&", "'&", "&&", "!", "'!", "||", "=", "'=", ">", "'>", "<",
			"'<", "[", "'[", "]", "']", "]]", "']]", "?","'?", "_", "'", "+", "-", "@" }; 
	Expression left, right; 

	// oper can assume 'operator' , 'literal type' or ...
	// $<FUNCNAME>
	int oper;

	
	/**
	 * Create a unary expression.
	 */
	public Expression(int op, Expression right) {
		oper = op;
		this.right = right;
	}

	public Expression(int op, Expression left, Expression right) {
		this.left = left;
		this.right = right;
		oper = op;
	}

	/**
	 * Constructors.
	 */
	protected Expression() {
	}

	public Object[] evalVariableName(){
		return null; //to be implemented in child classes.
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		if (left != null)
			sb.append(left.toString());
		sb.append(opCodes[oper]);
		sb.append(right.toString());
		sb.append(")");
		return sb.toString();
	}

	Object eval() {
		switch (oper) {
		case OP_ADD: // Addition (Binary) '+'
			return m$op.Add(left.eval(), right.eval());

		case OP_SUB: // Subtraction (Binary) '-'
			return m$op.Subtract(left.eval(), right.eval());

		case OP_MUL: // Multiplication '*'
			return m$op.Multiply(left.eval(), right.eval());

		case OP_DIV: // Division '/'
			if ((int) right.eval() == 0) {
				// throw new XecuteException("divide by zero!");
			}
			return m$op.Divide(left.eval(), right.eval());

		case OP_EXP: // Exponentiation '**'
			throw new XecuteException("Exponentiation '**' Not Implemented!");

		case OP_MOD: // Modulus (remainder) '#'
			return m$op.Modulus(left.eval(), right.eval());

		case OP_NOT: // Unary negation "'"
			return m$op.Not(right.eval());

		case OP_AND: // Logical AND '&'
			return m$op.And(left.eval(), right.eval());
			///FIXME - there is no correspondence in mOperation
		case OP_NAN: // Logical NOT AND "'&"
			return m$op.Not(m$op.And(left.eval(), right.eval()));
			
		case OP_SAND: // Logical AND (short-circuit) '&&'
			return (mFunctionUtil.booleanConverter(left.eval()) && mFunctionUtil
					.booleanConverter(right.eval()));

		case OP_OR: // Logical OR '!'
			return m$op.Or(left.eval(), right.eval());

		case OP_NOR: // Logical NOT OR "'!"
			return m$op.Not(m$op.Or(left.eval(), right.eval()));

		case OP_SOR: // Logical OR (short-circuit) '||'
			return (mFunctionUtil.booleanConverter(left.eval()) || mFunctionUtil
					.booleanConverter(right.eval()));

		case OP_EQ: // Equality '='
			return m$op.Equal(left.eval(), right.eval());

		case OP_NEQ: // Non-equality "'="
			return m$op.NotEqual(left.eval(), right.eval());

		case OP_GT: // Greater than '>'
			return m$op.Greater(left.eval(), right.eval());

		case OP_NGT: // Not Greater (less than or equal) "'>"
			return m$op.LessOrEqual(left.eval(), right.eval());

		case OP_LT: // Less than '<'
			return m$op.Less(left.eval(), right.eval());

		case OP_NLT: // Not Less (greater than or equal) "'<"
			return m$op.GreaterOrEqual(left.eval(), right.eval());

		case OP_CONT: // Contains '['
			return m$op.Contains(left.eval(), right.eval());
		
		case OP_NCON: // Not Contains "'["
			return m$op.NotContains(left.eval(), right.eval());

		case OP_FOLL: // Follows ']'
			return m$op.Follows(left.eval(), right.eval());

		case OP_NFOL: // Not Follows "']"
			return m$op.NotFollows(left.eval(), right.eval());
		
		case OP_SORA: // Sorts After ']]'
			return m$op.SortsAfter(left.eval(), right.eval());

		case OP_NSOR: // Not Sorts After "']]"
			return m$op.NotSortsAfter(left.eval(), right.eval());

		case OP_PATT: // Pattern match '?'
			return m$op.Match(left.eval(), right.eval());

		case OP_NPAT: // Pattern does not match "'?"
			return m$op.Not(m$op.Match(left.eval(), right.eval()));

		case OP_CONC: // Concatenation '_'
			return m$op.Concat(left.eval(), right.eval());

		case OP_POS: // Positive (Unary) '+'
			return m$op.Positive(right.eval());

		case OP_NEG: // Negative (Unary) '-'
			return m$op.Negative(right.eval());

		case OP_IND: // Indirection '@'
			return right.right.eval();

		default:
			throw new XecuteException("Illegal operator in expression!");

		}
	}

	void print(PrintStream p) {
		p.print("(");
		// unary expressions don't have an left.
		if (left != null)
			left.print(p);
		p.print(opCodes[oper]);
		right.print(p);
		p.print(")");
		return;
	}

	String unparse() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		if (left != null) {
			sb.append(left.unparse());
			sb.append(" " + opCodes[oper] + " ");
		}
		sb.append(right.unparse());
		sb.append(")");
		return sb.toString();
	}
		
}
