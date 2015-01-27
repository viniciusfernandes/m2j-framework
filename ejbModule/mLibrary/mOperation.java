package mLibrary;

import util.mFunctionUtil;
import util.mRegExConverter;


public class mOperation {
	
	public static Object Add(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return (d1 + d2);
	}

	public static boolean And(Object x, Object y) {
		boolean a = mFunctionUtil.booleanConverter(x);
		boolean b = mFunctionUtil.booleanConverter(y);
		return a & b;
	}

	public static Object Concat(Object ... args) {
		Object result = null;
		for (Object arg : args) {
			if (result == null){
				result = arg;
			} else {
				result = Concat(result, arg);
			}
		}
		
		return result;
	}

	public static Object Concat(Object string1, Object string2) {
		if (string1 instanceof mListBuild) {
			mListBuild lo1 = (mListBuild) string1;
			mListBuild lo2 = null;
			if (string2 == null || string2.toString().isEmpty()) {
				return lo1;
			} else if (string2 instanceof mListBuild) {
				lo2 = (mListBuild)string2;				
				return mListBuild.concat(lo1, lo2);
			}else{
				string1 = lo1.getString();			
			}
		} else if (string2 instanceof mListBuild) {
			mListBuild lo2 = (mListBuild) string2;
			mListBuild lo1 = null;
			if (string1 == null || string1.toString().isEmpty()) {
				return lo2;
			} else if (string1 instanceof mListBuild) {
				lo1 = (mListBuild)string1;
				return mListBuild.concat(lo1, lo2);
			}else{
				string2 = lo2.getString();
			}
		}
		return mFunctionUtil.toString(string1).concat(mFunctionUtil.toString(string2));
	}

	public static boolean Contains(Object str1, Object str2) {
		return String.valueOf(str1).contains(String.valueOf(str2));
	}

	public static Object Divide(Object num1, Object num2) {
		//return IntegerDivide(num1, num2);
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return Double.valueOf(d1 / d2);
	}

	public static boolean Equal(Object x, Object y) {
		if (x instanceof Double && (((Double) x) % 1 == 0)) {
			x = ((Double) x).longValue();
		}
		if (y instanceof Double && (((Double) y) % 1 == 0)) {
			y = ((Double) y).longValue();
		}
		if (x instanceof Boolean) {
			x = ((Boolean) x) ? 1 : 0;
		}
		if (y instanceof Boolean) {
			y = ((Boolean) y) ? 1 : 0;
		}
		if (String.valueOf(x).equals(String.valueOf(y))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean Follows(Object object, Object object2) {
		throw new UnsupportedOperationException();
	}

	public static boolean Greater(Object object, Object object2) {
		return mFunctionUtil.numberConverter(object) > mFunctionUtil
				.numberConverter(object2);

	}

	public static boolean GreaterOrEqual(Object object, Object object2) {
		return mFunctionUtil.numberConverter(object) >= mFunctionUtil
				.numberConverter(object2);
	}

	public static Object IntegerDivide(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return Double.valueOf(d1 / d2).intValue();
	}

	public static boolean Less(Object object, Object object2) {
		return mFunctionUtil.numberConverter(object) < mFunctionUtil
				.numberConverter(object2);
	}

	public static boolean LessOrEqual(Object object, Object object2) {
		return mFunctionUtil.numberConverter(object) <= mFunctionUtil
				.numberConverter(object2);
	}

	public static boolean Logical(Object object) {
		return mFunctionUtil.booleanConverter(object);
	}

	public static Boolean Match(Object string, Object regex) {
		String regexJava;
		regexJava = mRegExConverter.convertPattern(String.valueOf(regex));
		return String.valueOf(string).matches(regexJava);
	}

	public static Object Modulus(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return (d1 % d2);
	}

	public static Object Multiply(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return (d1 * d2);
	}

	public static Object Negative(Object num) {
		if (num == null) {
			return null;
		}
		double d1 = mFunctionUtil.numberConverter(num);
		d1 = d1 * -1;
		return d1;
	}

	/*
	 * public static Object Select(Object x, Object y) { throw new
	 * UnsupportedOperationException(); }
	 */
	public static boolean Not(Object obj) {
		return !Logical(obj);
	}

	public static boolean NotContains(Object str1, Object str2) {
		return !Contains(str1, str2);
	}

	public static boolean NotEqual(Object x, Object y) {
		return !Equal(x, y);
	}

	public static boolean NotFollows(Object object, Object object2) {
		throw new UnsupportedOperationException();
	}

	public static boolean NotGreater(Object object, Object object2) {
		return !Greater(object, object2);
	}

	public static boolean NotLess(Object object, Object object2) {
		return !Less(object, object2);
	}

	public static Object NotSortsAfter(Object object, Object object2) {
		return !SortsAfter(object, object2);
	}

	public static boolean Or(Object x, Object y) {
		boolean a = mFunctionUtil.booleanConverter(x);
		boolean b = mFunctionUtil.booleanConverter(y);
		return a | b;
	}

	public static Object Positive(Object num) {
		if (num == null) {
			return null;
		}
		double d1 = mFunctionUtil.numberConverter(num);
		return d1;
	}

	public static Object Pow(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return Math.pow(d1,d2);
	}
	
	public static boolean SortsAfter(Object object, Object object2) {

		String var1 = String.valueOf(object);
		String var2 = String.valueOf(object2);
		boolean isNumVar1 = mFunction.$isvalidnum(var1);
		boolean isNumVar2 = mFunction.$isvalidnum(var2);

		boolean isAfter = false;

		if (isNumVar1 && isNumVar2) {
			isAfter = Double.parseDouble(var1) > Double.parseDouble(var2);
		} else if (isNumVar1 && !isNumVar2) {
			isAfter = false;
		} else if (!isNumVar1 && isNumVar2) {
			isAfter = true;
		} else {
			isAfter = var1.compareTo(var2) > 0;
		}
		return isAfter;
	}
	
	public static Object Subtract(Object num1, Object num2) {
		double d1 = mFunctionUtil.numberConverter(num1);
		double d2 = mFunctionUtil.numberConverter(num2);
		return (d1 - d2);
	}

	protected mOperation() {
	}

}
