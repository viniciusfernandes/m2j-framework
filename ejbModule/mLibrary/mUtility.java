package mLibrary;

import util.mFunctionUtil;

public class mUtility {

	public static Boolean toBoolean(Object value) {
		return mFunctionUtil.booleanConverter(value);
	}
	
	public static Double toDouble(Object value) {
		return mFunctionUtil.numberConverter(value);
	}
	
	public static Float toFloat(Object value) {
		return mFunctionUtil.numberConverter(value).floatValue();
	}
	
	public static Integer toInt(Object value) {
		return mFunctionUtil.numberConverter(value).intValue();
	}
	
	public static Long toLong(Object value) {
		return mFunctionUtil.numberConverter(value).longValue();
	}
	
	public static Double toNumber(Object value) {
		return mFunctionUtil.numberConverter(value);
	}

	public static String toString(Object value) {
		return mFunctionUtil.toString(value);
	}

}