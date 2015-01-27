package mLibrary.implementation.xecute.node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import mLibrary.mVar;
import mLibrary.exceptions.CommandException;
import mLibrary.implementation.xecute.LexicalTokenizer;
import mLibrary.implementation.xecute.Token;

public class ExpressionPieceVariable extends ExpressionFunction {

	public ExpressionPieceVariable(Token t, LexicalTokenizer lt) {
		super(t, lt);
		if(listArgs.size()>0 && listArgs.get(0) instanceof ExpressionVariable ){
			((ExpressionVariable)listArgs.get(0)).makeByRef();			
		}
		// TODO Auto-generated constructor stub
	}

	private mVar dispatchPieceVar(Object...args) {
		Object result = null;
		
		//Class[] argTypes = evalArgumentTypes(args); //{ String[].class };
		Method intrinsicFunction;
		try {
			String methodName = "pieceVar";
			Method[] methodArray = m$.getClass().getDeclaredMethods();
			for (int i = 0; i < methodArray.length; i++) {
				 intrinsicFunction = methodArray[i];
				if(intrinsicFunction.getName().equals(methodName)){
					try {
						result = intrinsicFunction.invoke(m$, args);	
						break;
					} catch (IllegalArgumentException e) {
						continue;
					}
				}
			}
			if(result==null){
				throw new CommandException("Xecute Method "+methodName+" with parameters "+Arrays.toString(args)+" not found in mFunction");
			}
			//getDeclaredMethod(functionNames[intrinsicType], argTypes);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return (mVar)result;
	}
	@Override
	mVar eval() {
		// TODO Auto-generated method stub
		return dispatchPieceVar(evalArguments());
	}
	
    
}
