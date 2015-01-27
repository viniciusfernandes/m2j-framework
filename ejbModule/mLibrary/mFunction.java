package mLibrary;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import mLibrary.exceptions.UndefinedVariableException;
import util.mFunctionUtil;
import util.mZStripParser;
import br.com.innovatium.mumps2java.todo.REVIEW;
import br.com.innovatium.mumps2java.todo.TODO;
import dataaccess.mDataAccess;

public final class mFunction {
	/**
	 * Converts a character to a numeric code.
	 * 
	 * @param expression
	 * <br>
	 *            expression The character to be converted. <br>
	 *            position Optional — The position of a character within a
	 *            character string, counting from 1. The default is 1.
	 * @return returns the character code value for a single character specified
	 *         in expression.
	 */
	public static Object $ascii(Object expression) {
		return $ascii(expression, 1);
	}

	/**
	 * Converts a character to a numeric code.
	 * 
	 * @param expression
	 * @param position
	 * <br/>
	 *            expression -> The character to be converted. <br/>
	 *            position -> Optional — The position of a character within a
	 *            character string, counting from 1. The default is 1.
	 * @return numeric code
	 */
	public static Object $ascii(Object expression, Object position) {
		if (String.valueOf(expression).length() == 0) {
			return -1;
		}
		Double convertedPosition = mFunctionUtil.numberConverter(position);
		if (String.valueOf(expression).length() < convertedPosition) {
			return -1;
		}
		return Character.codePointAt(String.valueOf(expression).toCharArray(),
				convertedPosition.intValue() - 1);
	}

	/**
	 * Converts the integer value of an expression to the corresponding ASCII or
	 * Unicode character.
	 * 
	 * @param i
	 * @return
	 */
	public static Object $c(Object... intArgs) {
		return $char(intArgs);
	}

	/**
	 * Compares expressions and returns the value of the first matching case.
	 * 
	 * @param args
	 *            </br> target - A literal or expression the value of which is
	 *            to be matched against cases. </br> case - A literal or
	 *            expression the value of which is to be matched with the
	 *            results of the evaluation of target. </br> value - The value
	 *            to be returned upon a successful match of the corresponding
	 *            case. </br> default - Optional — The value to be returned if
	 *            no case matches target.
	 * @return Object
	 */
	public static Object $case(Object... args) {
		Boolean found = false;
		Object returnObj = null;
		if (args != null) {
			if (args.length < 3) {
				throw new IllegalArgumentException(
						"This method requires at least one pair of condition and value that returns as true.");
			}
			Object target = args[0];
			for (int i = 1; i < args.length; i++) {
				if (i == args.length - 1 && args.length % 2 == 0) {
					returnObj = args[args.length - 1];
					break;
				}
				if (i % 2 != 0) {
					if (mOperation.Equal(target, args[i])) {
						found = true;
						returnObj = args[i + 1];
						break;
					}
				}
			}
			if (!found && returnObj == null) {
				throw new IllegalArgumentException(
						"This method requires at least one pair of condition and value.");
			}
		}
		return returnObj;
	}

	/**
	 * Converts the integer value of an expression to the corresponding ASCII or
	 * Unicode character.
	 * 
	 * @param codes
	 * <br>
	 *            expression The integer value to be converted.
	 * @return returns the character that corresponds to the decimal (base-10)
	 *         integer value specified by expression.
	 */
	public static Object $char(Object... codes) {
		if (codes == null || codes.length == 0) {
			return null;
		}
		return mFunctionUtil.characterImpl(mFunctionUtil.castIntArray(codes));
	}

	public static mListBuild $concat(mListBuild... lists) {
		return mListBuild.concat(lists);
	}

	public static int $data(Object var) {
		if (var instanceof mVar) {
			return ((mVar) var).data();
		}
		return var != null ? 1 : 0;
	}

	public static String $extract(Object string) {
		return $extract(string, 1);
	}

	public static String $extract(Object value, Object index) {
		return $extract(value, index, index);
	}

	public static String $extract(Object string, Object from, Object to) {
		if (string == null) {
			return null;
		}

		int start = -1;
		int end = -1;
		start = mFunctionUtil.integerConverter(from);
		String value = mFunctionUtil.toString(string);
		final int length = value.length();

		if (to == null) {
			end = length;
		} else if (from.equals(to)) {
			end = start;
		} else {
			end = mFunctionUtil.integerConverter(to);
		}

		if (end <= 0) {
			return "";
		} else if (start <= 0 && end > 0) {
			start = 1;
		} else if (start > end) {
			return "";
		}
		try {
			if (end > length) {
				end = length;
			}
			if (start > length) {
				return "";
			}

			return value.substring(start - 1, end);
		} catch (Exception e) {
			return null;
		}

	}

	public static int $f(Object string, Object substring) {
		return $find(string, substring);
	}

	public static int $f(Object string, Object substring, Object start) {
		return $find(string, substring, start);
	}

	public static int $find(Object string, Object substring) {
		return $find(string, substring, 1);
	}

	public static int $find(Object string, Object substring, Object start) {
		return mFunctionUtil.findImpl(mFunctionUtil.toString(string),
				mFunctionUtil.castString(substring),
				mFunctionUtil.castInt(start));
	}

	public static Object $fnumber(Object inumber, String format) {
		return $fnumber(inumber, format, null);
	}

	/**
	 * Formats a numeric value with a specified format; optionally rounds to a
	 * specified precision.
	 * 
	 * @param inumber
	 * @param format
	 * @param decimal
	 * @return returns the number specified by inumber in the specified format
	 */
	public static Object $fnumber(Object inumber, String format, Object decimal) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
		DecimalFormat formatter = new DecimalFormat();
		Double inumberDbl = mFunctionUtil.numberConverter(inumber);
		String formatedString = "";

		if (format != null) {
			if (decimal != null) {
				formatter.setMaximumFractionDigits(mFunctionUtil
						.integerConverter(decimal));
				formatter.setMinimumFractionDigits(mFunctionUtil
						.integerConverter(decimal));
			}
			if(!format.isEmpty()){
				if (".".contains(format)) {
					dfs.setGroupingSeparator(String.valueOf(format).charAt(0));
					dfs.setDecimalSeparator(',');
				} else if (",".contains(format)) {
					dfs.setGroupingSeparator(String.valueOf(format).charAt(0));
					dfs.setDecimalSeparator('.');
				} else if (String.valueOf(format).equals("T")) {
					formatter.setNegativeSuffix("-");
				} else if (String.valueOf(format).equals("T+")) {
					formatter.setPositiveSuffix("+");
				} else if (String.valueOf(format).equals("P")) {
					formatter.setNegativePrefix("(");
					formatter.setNegativeSuffix(")");
				} else if ("+".equals(format)) {
					if (!(inumberDbl < 0)) {
						formatter.setPositivePrefix("+");
					} else {
						formatter.setNegativePrefix("");
					}
				} else if ("-".equals(format)) {
					if ((inumberDbl < 0)) {
						formatter.setPositivePrefix("+");
					} else {
						formatter.setNegativePrefix("-");
					}
				}				
			}

			formatter.setDecimalFormatSymbols(dfs);
			formatedString = formatter.format(inumberDbl);
			if(format.equals("")){
		    	formatedString = formatedString.replaceAll(",", "");
			}

		} else {
			formatedString = BigDecimal
					.valueOf(inumberDbl)
					.setScale(mFunctionUtil.integerConverter(decimal),
							BigDecimal.ROUND_HALF_UP).toString();
		}

		return formatedString;
	}

	public static Object $get(Object content) {
		return $get(content, "");
	}

	public static Object $get(Object content, Object defaultValue) {
		if (content == null) {
			//throw new IllegalArgumentException("Content must not be null");
		}
		if (content instanceof mVar) {
			try {
				content = ((mVar) content).getValue();
			} catch (Exception e) {
				content = null;
			}
		}

		if (content == null && defaultValue != null) {
			return defaultValue;
		} else if (content == null && defaultValue == null) {
			return "";
		} else {
			return content;
		}
	}

	public static Object $horolog() {
		Calendar cal = Calendar.getInstance();
		Double daysMumps = mFunctionUtil.dateTimeJavaToMumpsDouble(
				cal.getTimeInMillis(), TimeZone.getDefault());
		Double sec = (daysMumps - daysMumps.longValue()) * 24d * 60d * 60d;

		return daysMumps.longValue() + "," + sec.longValue();
	}

	public static Object $increment(mVar var) {
		return $increment(var, 1);
	}

	public static Object $increment(mVar var, Object increment) {
		Object dbl = mOperation.Add(var.getValue(), increment);
		var.set(dbl);
		return dbl;
	}

	public static Object $isobject(Object object) {
		int isObject = 0;
		if (object != null && object instanceof Object) {
			if (!object.equals("")) {
			  isObject = 1;
			}
		}
		return isObject;
	}

	public static boolean $isvalidnum(Object num) {
		if (num == null) {
			return false;
		}
		return num.toString().matches(
				"[\\+{0,1}\\-{0,1}]{0,1}\\d+(\\.{0,1}\\d+E{0,1}\\d+){0,1}");
	}

	public static Object $job() {
		return Thread.currentThread().getId();
	}

	/**
	 * Returns the value of an expression right-aligned within the specified
	 * width.
	 * 
	 * @param expression
	 * @param width
	 * @param decimal
	 * @return <br/>
	 *         expression -> The value that is to be right-aligned. It can be a
	 *         numeric value, a string literal, the name of a variable, or any
	 *         valid Caché ObjectScript expression. <br/>
	 *         width -> The number of characters within which expression is to
	 *         be right-aligned. It can be a positive integer value, the name of
	 *         an integer variable, or any valid Caché ObjectScript expression
	 *         that evaluates to a positive integer. <br/>
	 *         decimal -> Optional — The position at which to place the decimal
	 *         point within the width. It can be a positive integer value, the
	 *         name of an integer variable, or any valid Caché ObjectScript
	 *         expression that evaluates to a positive integer.
	 */
	public static Object $justify(Object expression, int width, Object decimal) {
		if (expression == null) {
			return null;
		}
		if (decimal != null) {
			expression = mFunctionUtil.round(
					mFunction.numberConverter(expression),
					mFunction.integerConverter(decimal));
		}
		String result = mFunctionUtil.toString(expression);
		int length = result.length();

		width = width > length ? width : length;
		String strFormated = new String(new char[width - length]).replace("\0",
				" ").concat(result);
		return strFormated;
	}

	public static Object $justify(Object expression, Object width) {
		int widthInt = mFunctionUtil.integerConverter(width);
		return $justify(expression, widthInt, null);
	}

	public static Object $justify(String expression, int width) {
		return $justify(expression, width, null);
	}

	public static mListBuild $lb(Object... elements) {
		return $listbuild(elements);
	}

	public static Object $length(Object expression) {
		if (expression == null) {
			throw new UndefinedVariableException("Null values are not allowed");
		}
		return mFunctionUtil.toString(expression).length();
	}

	public static Object $length(Object expression, Object delimiter) {

		if (expression == null) {
			throw new UndefinedVariableException("Null values are not allowed");
		}

		return mFunctionUtil.toString(expression).split(
				Pattern.quote(String.valueOf(delimiter)), -1).length;
	}

	public static Object $list(Object list) {
		return $listget(list, 1);
	}

	public static Object $list(Object list, Object init) {
		return $listget(list, init);
	}

	public static mListBuild $list(Object list, Object init, Object end) {
		if (list instanceof mListBuild) {
			return ((mListBuild) list).sublist(
					util.mFunctionUtil.integerConverter(init) - 1,
					util.mFunctionUtil.integerConverter(end));
		}
		return null;
	}

	public static mListBuild $listbuild(Object... elements) {
		return mListBuild.add(elements);
	}

	public static Object $listfind(Object $listbuild, Object object) {
		return $listfind($listbuild, object, 0);
	}

	public static Object $listfind(Object $listbuild, Object object,
			Object startAfter) {
		if ($listbuild instanceof mListBuild) {
			mListBuild lo = (mListBuild) $listbuild;
			return lo.find(object, startAfter);
		} else {
			return 0;
		}
	}

	public static Object $listget(Object list) {
		return $listget(list, 1);
	}

	public static Object $listget(Object list, Object position) {
		return $listget(list, position, "");
	}

	public static Object $listget(Object list, Object index, Object defaultValue) {
		if (defaultValue == null) {
			throw new IllegalArgumentException(
					"Default value is required to get list function.");
		}
		// position convert to integer
		Object result = defaultValue;
		if (list instanceof mVar) {
			mVar var =(mVar)list;
			list = var.get();
		}
		if (list instanceof mListBuild) {
 
			int position = mFunctionUtil.integerConverter(index);
			mListBuild listValue = (mListBuild) list;
			if (position <= listValue.length()) {
				result = listValue.element(position);
				if (result == null && defaultValue != null) {
					return defaultValue;
				} else if (result == null && defaultValue == null) {
					return "";
				} else {
					return result;
				}
			} else {
				return defaultValue;
			}
		}
		return result;
	}

	public static Object $listlength(Object list) {
		if (list instanceof mListBuild) {
			return ((mListBuild) list).length();
		} else if (list.toString().isEmpty()) {
			return 0;
		}

		if (String.valueOf(list).isEmpty()) {
			return 0;
		}

		throw new IllegalArgumentException(
				"Arguments must be a listObject and it was " + list.getClass());
	}

	/**
	 * Determines if an expression is a list.
	 * 
	 * @param object
	 * @return $LISTVALID determines whether exp is a list, and returns a
	 *         Boolean value: If exp is a list, $LISTVALID returns 1; if exp is
	 *         not a list, $LISTVALID returns 0. <br>
	 *         exp Any valid expression.
	 */
	public static Object $listvalid(Object object) {
		if (object instanceof mListBuild) {
			return 1;
		} else {
			return 0;
		}
	}

	public static Object $name(mVar variable) {
		return $name(variable, variable.getSubs().length - 1);
	}

	public static Object $name(mVar variable, Object integer) {
		int subscriptPosition = mFunctionUtil.integerConverter(integer);
		/*
		 * String var = String.valueOf(variable); if (var.isEmpty()){ throw new
		 * IllegalArgumentException("Missing variable name"); } boolean isGlobal
		 * = var.startsWith("^");
		 * 
		 * String result = "";
		 */
		Object[] qsubscripts = variable.getSubs();

		String result = mFunctionUtil.toString(qsubscripts[0]);
		if (subscriptPosition > 0) {
			if (qsubscripts.length > 1) {
				result = result + "(";
				String strsub;
				for (int i = 1; ((i < qsubscripts.length) && (i <= subscriptPosition)); i++) {
					strsub = mFunctionUtil.toString(qsubscripts[i]);
					if (mFunctionUtil.isCanonicalNumeric(strsub)) {
						result = result + ((i > 1) ? "," : "") + strsub;
					} else {
						result = result + ((i > 1) ? "," : "") + "\""
								+ mFunctionUtil.escapeValue(strsub) + "\"";
					}
				}
				result = result + ")";
			}
		}

		return result;
	}

	public static Object $piece(Object string, Object delimiter) {
		return mFunctionUtil.pieceImpl(string,
				mFunctionUtil.castString(delimiter), 1);
	}

	public static Object $piece(Object string, Object delimiter, Object index) {
		return mFunctionUtil.pieceImpl(string,
				mFunctionUtil.castString(delimiter),
				mFunctionUtil.integerConverter(index));
	}

	public static Object $piece(Object string, Object delimiter, Object from,
			Object to) {

		if (from == to) {
			return $piece(string, delimiter, to);
		}

		return mFunctionUtil.pieceImpl(string,
				mFunctionUtil.castString(delimiter),
				mFunctionUtil.integerConverter(from),
				mFunctionUtil.integerConverter(to));
	}

	public static Object $principal() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public static int $qlength(Object nameValue) {
		String name = String.valueOf(nameValue);

		if (name.isEmpty()) {
			throw new IllegalArgumentException("Missing variable name");
		}
		List<String> subscriptsList = mFunctionUtil.getSubscriptList(name);
		return subscriptsList.size();
	}

	public static Object $qsubscript(Object nameValue, Object intCode) {
		int code = mFunctionUtil.integerConverter(intCode);
		if (code < -1) {
			throw new IllegalArgumentException(
					"These numbers are reserved for future extensions.");
		}
		String name = String.valueOf(nameValue);
		boolean isGlobal = (name.indexOf("^") >= 0);

		Object result = null;
		switch (code) {
		case -1: // Returns the namespace name if a global variable namevalue
					// includes one; otherwise, returns the null string ("").
			result = isGlobal ? $znspace() : "";
			break;
		case 0: // Returns the variable name. Returns ^NAME for a global
				// variable.
			result = name.substring(0, name.indexOf("("));
			break;
		default:
			List<String> subscriptsList = mFunctionUtil.getSubscriptList(name);
			result = (code > subscriptsList.size()) ? "" : subscriptsList
					.get(code - 1);
			break;
		}
		return result;
	}

	/**
	 * Returns a pseudo-random integer value in the specified range.
	 * 
	 * @param range
	 * <br/>
	 *            range A nonzero positive integer used to calculate the random
	 *            number.
	 * @return returns a pseudo-random integer value between 0 and range-1
	 *         (inclusive).
	 */
	public static Object $random(Object range) {
		return new Random().nextInt(mFunctionUtil.numberConverter(range)
				.intValue());
	}

	public static String $replace(Object string, Object oldSubstring,
			Object newSubstring) {
		if (string == null) {
			return null;
		}
		String strParam = String.valueOf(string);

		if (oldSubstring == null) {
			return strParam;
		}

		return strParam.replace(oldSubstring.toString(),
				newSubstring.toString());
	}

	public static String $reverse(Object string) {
		if (string == null) {
			return null;
		}
		return new StringBuilder(mFunctionUtil.toString(string)).reverse()
				.toString();
	}

	/**
	 * Returns the value associated with the first true expression.
	 * 
	 * @param args
	 * <br/>
	 *            expression - The select test for the associated value
	 *            parameter. <br/>
	 *            value - The value to be returned if the associated expression
	 *            evaluates to true.
	 * @return Object associated
	 */
	public static Object $select(Object... args) {
		Boolean hasTrue = false;
		Object returnObj = null;
		if (args != null) {
			if (args.length % 2 != 0) {
				throw new IllegalArgumentException(
						"This method requires at least one pair of  condition and value that returns as true.");
			}
			for (int i = 0; i < args.length; i++) {
				if (i % 2 == 0) {
					boolean bool = mFunctionUtil.booleanConverter(args[i]);
					if (bool) {
						hasTrue = bool;
						returnObj = args[i + 1];
						break;
					}
				}
			}
			if (!hasTrue) {
				throw new IllegalArgumentException(
						"This method requires at least one pair of  condition and value that returns as true.");
			}
		}
		return returnObj;
	}

	public static Object $setextract(Object value, Integer from, Integer to,
			Object value2) {
		Object result = null;
		if (value == null) {
			result = value2;
		} else {
			String strValue = mFunctionUtil.toString(value);
			String ini = strValue.substring(0, from - 1);
			String end = strValue.substring(to);

			result = ini + mFunctionUtil.toString(value2) + end;
		}
		return result;
	}

	public static Object $setpiece(Object string, Object delimiter,
			Object position, Object value) {
		return $setpiece(string, delimiter, position, value, null);
	}

	public static Object $setpiece(Object string, Object delimiter,
			Object position, Object value, Integer to) {
		return mFunctionUtil.setPieceImpl(mFunctionUtil.castString(string),
				mFunctionUtil.castString(delimiter),
				mFunctionUtil.castInt(position), value, to);
	}

	public static Object $stack(Object... objs) {
		// TODO REVISAR IMPLEMENTAÇÃO DA FUNÇÃO
		return "";
	}

	public static Object $storage() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object $tlevel() {
		return m$.getTLevel();
	}

	//
	public static Object $translate(Object string, Object identifier) {
		return $translate(string, identifier, null);
	}

	public static Object $translate(Object string, Object oldCharsequence,
			Object newCharsequence) {
		return mFunctionUtil.translateImpl(mFunctionUtil.toString(string),
				mFunctionUtil.castString(oldCharsequence),
				mFunctionUtil.castString(newCharsequence));
	}

	/**
	 * Absolute value function.
	 * 
	 * @param n
	 * @return returns the absolute value of n.
	 */
	public static Object $zabs(Object n) {
		return Math.abs(mFunctionUtil.numberConverter(n));
	}

	public static Object $zconvert(Object string, String mode) {
		String zconverted = String.valueOf(string);
		if (mode.equalsIgnoreCase("U")) {
			zconverted = String.valueOf(string).toUpperCase();
		}
		if (mode.equalsIgnoreCase("L")) {
			zconverted = String.valueOf(string).toLowerCase();
		}
		return zconverted;
	}

	public static Object $zconvert(Object string, String mode, String trantable) {
		// TODO REVISAR IMPLEMENTAÇÃO DO MÉTODO
		String toString = mFunctionUtil.toString(string);
		if (trantable.equals("JS")) {
			return mFunctionUtil.escapeJS(toString);
		} else if (trantable.equals("URL")) {
			if (mode.equalsIgnoreCase("O")) {
				return mFunctionUtil.escapeURL(toString);
			} else if (mode.equalsIgnoreCase("I")) {
				// return
				// ListObject.isList(mFncUtil.unescapeURL(String.valueOf(string),"UTF-32"));
				return mFunctionUtil.unescapeURL(toString);
			}
			throw new UnsupportedOperationException();
		} else if (trantable.equals("HTML")) {
			if(toString.startsWith("&#x")){
				toString = toString.replace("&#x", "%FE%FF%").replace(";", "");
				toString = new StringBuilder(toString).insert(9, "%").toString();
				toString = mFunctionUtil.unescapeURL(toString,"UTF-16");
			}
			return toString;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public static Object $zcrc(Object string, int mode) {
		Integer checksum = null;
		if (mode == 0) {
			char[] charArray = String.valueOf(string).toCharArray();
			for (int j = 0; j < charArray.length; j++) {
				checksum = (checksum != null ? checksum : 0)
						+ Integer.valueOf(String.valueOf($ascii(charArray[j])));
			}
			return checksum;
		} else if (mode == 7) {
			CRC32 crc = new CRC32();
			crc.update(String.valueOf(string).getBytes());
			return crc.getValue();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public static Object $zdate(Object hdate) {
		return $zdate(hdate, null, null, null, null, null, null, null, null);
	}

	public static Object $zdate(Object... args) {
		throw new UnsupportedOperationException();
	}

	public static Object $zdate(Object hdate, Object dformat) {
		return $zdate(hdate, dformat, null, null, null, null, null, null, null);
	}

	public static Object $zdate(Object hdate, Object dformat, Object monthlist,
			Object yearopt) {
		return $zdate(hdate, dformat, monthlist, yearopt, null, null, null,
				null, null);
	}

	/**
	 * Validates date and converts format.
	 * 
	 * @param hdate
	 * @param dformat
	 * @param monthlist
	 * @param yearopt
	 * @param startwin
	 * @param endwin
	 * @param mindate
	 * @param maxdate
	 * @param erropt
	 * @return
	 */
	public static Object $zdate(Object hdate, Object dformat, Object monthlist,
			Object yearopt, Object startwin, Object endwin, Object mindate,
			Object maxdate, Object erropt) {
		TimeZone tz = TimeZone.getDefault();
		TimeZone.setDefault(mFunctionUtil.getTimeZone());
		GregorianCalendar dt = new GregorianCalendar(
				mFunctionUtil.getTimeZone());
		dt.setTimeInMillis(mFunctionUtil.dateMumpsToJava(hdate).longValue());
		dt.setLenient(false);
		SimpleDateFormat sdf = new SimpleDateFormat(
				mFunctionUtil.dateCodeFormatMumpsToJava(dformat));
		String dateFormated = sdf.format(dt.getTime());
		TimeZone.setDefault(tz);
		return dateFormated;
	}

	public static Object $zdateh(Object date) {
		return $zdateh(date, 1);// TODO REVISAR FORMATO LOCAL
	}

	@TODO
	public static Object $zdateh(Object... args) {
		throw new UnsupportedOperationException();
	}

	public static Object $zdateh(Object date, Object dformat) {

		SimpleDateFormat sdf = new SimpleDateFormat(
				mFunctionUtil.dateCodeFormatMumpsToJava(dformat, date
						.toString().length()), DateFormatSymbols.getInstance());
		sdf.setTimeZone(mFunctionUtil.getTimeZone());
		String returnDate = null;
		try {
			returnDate = String.valueOf(mFunctionUtil.dateJavaToMumps(
					sdf.parse(String.valueOf(date)).getTime()).longValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	public static Object $zdateh(Object date, Object dformat, Object monthlist,
			Object yearopt, Object startwin, Object endwin, Object mindate,
			Object maxdate, Object erropt) {

		SimpleDateFormat sdf = new SimpleDateFormat(
				mFunctionUtil.dateCodeFormatMumpsToJava(dformat));
		String returnDate = erropt != null ? String.valueOf(erropt) : null;
		try {
			returnDate = String.valueOf(mFunctionUtil.dateJavaToMumps(
					sdf.parse(String.valueOf(date)).getTime()).longValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	public static Object $zdatetime(Object hdatetime) {
		return $zdatetime(hdatetime, 1, 1, null, null, null, null, null, null,
				null, null);
	}

	public static Object $zdatetime(Object hdatetime, Object dformat) {
		return $zdatetime(hdatetime, dformat, 1, null, null, null, null, null,
				null, null, null);
	}

	public static Object $zdatetime(Object hdatetime, Object dformat,
			Object tformat) {
		return $zdatetime(hdatetime, dformat, tformat, null, null, null, null,
				null, null, null, null);
	}

	public static Object $zdatetime(Object hdatetime, Object dformat,
			Object tformat, Object precision) {
		return $zdatetime(hdatetime, dformat, tformat, precision, null, null,
				null, null, null, null, null);
	}

	public static Object $zdatetime(Object hdatetime, Object dformat,
			Object tformat, Object precision, Object monthlist, Object yearopt,
			Object startwin, Object endwin, Object mindate, Object maxdate,
			Object erropt) {
		return $zdate($piece(hdatetime, ",", 1), dformat, monthlist, yearopt,
				startwin, endwin, mindate, maxdate, erropt)
				+ " "
				+ $ztime($piece(hdatetime, ",", 2), tformat, precision, erropt);
	}

	public static Object $zdatetimeh(Object hdatetime, Object dformat) {
		return $zdatetimeh(hdatetime, dformat, 1);
	}

	public static Object $zdatetimeh(Object hdatetime, Object dformat,
			Object tformat) {
		String datePattern = mFunctionUtil.dateCodeFormatMumpsToJava(dformat);
		if (String.valueOf(hdatetime).length() > datePattern.length()) {
			datePattern = datePattern.concat(" ").concat(
					mFunctionUtil.timeCodeFormatMumpsToJava(tformat));
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		sdf.setTimeZone(mFunctionUtil.getTimeZone());
		Double daysMumps = null;
		try {
			sdf.setLenient(false);
			daysMumps = mFunctionUtil.dateTimeJavaToMumpsDouble(sdf.parse(
					String.valueOf(hdatetime)).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BigDecimal sec = BigDecimal.valueOf(
				(daysMumps - daysMumps.longValue()) * 24d * 60d * 60d)
				.setScale(3, BigDecimal.ROUND_HALF_UP);
		Integer fra = Double.valueOf(
				(sec.doubleValue() - sec.longValue()) * 1000d).intValue();
		String hStr = String.valueOf(daysMumps.longValue());
		hStr = hStr.concat("," + sec.longValue());

		if (fra > 0) {
			hStr = hStr.concat("." + fra.longValue());
		}
		return hStr;
	}

	public static Object $zhex(Object num) {
		if (num instanceof String) {
			return Integer.decode(num.toString());
		}
		if (num instanceof Number) {
			return Integer.toHexString(mFunctionUtil.integerConverter(num));
		}
		throw new IllegalArgumentException(
				"The required argument is Integer or String");
	}

	public static Object $zhorolog() {
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 01, 01);
		return Double
				.valueOf(mFunctionUtil.dateTimeJavaToMumpsDouble(
						cal.getTimeInMillis(), TimeZone.getDefault()) * 24d * 60d * 60d);
	}

	public static Object $zjob() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@REVIEW(description = "Esclarecer conceito de namespace.")
	public static Object $znspace() {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException();
		return "DEFAULT";
	}

	/**
	 * Verifica se o diretorio existe!
	 * 
	 * 
	 * */

	public static Object $zsearch(Object string) {
		try {
			String path = (String) string;
			File directory = new File(path);

			if (directory.exists()) {

				return directory.getName();
			} else {

				return "";
			}

		} catch (Exception e) {

			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Removes types of characters and individual characters from a specified
	 * string.
	 * 
	 * @param object
	 * @param string
	 * @return <BR>
	 *         string -> The string to be stripped. <BR>
	 *         action -> What to strip from string. A string consisting of an
	 *         action code followed by one or more mask codes. Specified as a
	 *         quoted string. <BR>
	 *         remchar -> Optional � A string of specific character values to
	 *         remove. Generally, these are additional characters not covered by
	 *         the action parameter�s mask code. <BR>
	 *         keepchar -> Optional � A string of specific character values to
	 *         not remove that are designated for removal by the action
	 *         parameter�s mask code.
	 */
	public static Object $zstrip(Object targetString, Object maskCode) {
		return $zstrip(targetString, maskCode, "", "");
	}

	public static Object $zstrip(Object targetString, Object maskCode,
			Object remChars) {
		return $zstrip(targetString, maskCode, remChars, "");
	}

	public static Object $zstrip(Object targetString, Object maskCode,
			Object remChars, Object keepChars) {
		return mZStripParser.zstrip(String.valueOf(targetString),
				String.valueOf(maskCode), String.valueOf(remChars),
				String.valueOf(keepChars));
	}

	public static Object $ztime(Object htime) {
		return $ztime(htime, 1, -1, null);
	}

	public static Object $ztime(Object htime, Object tformat) {
		return $ztime(htime, tformat, -1, null);
	}

	public static Object $ztime(Object htime, Object tformat, Object precision,
			Object erropt) {
		TimeZone tz = TimeZone.getDefault();
		TimeZone.setDefault(mFunctionUtil.getTimeZone());
		GregorianCalendar dt = new GregorianCalendar(
				mFunctionUtil.getTimeZone());
		dt.setTimeInMillis(m$util.toLong(htime) * 1000);
		dt.setLenient(false);
		SimpleDateFormat sdf = new SimpleDateFormat(
				mFunctionUtil.timeCodeFormatMumpsToJava(tformat));
		String dateFormated = sdf.format(dt.getTime());
		TimeZone.setDefault(tz);
		return dateFormated;
	}

	public static String $ztimestamp() {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(
				SimpleTimeZone.UTC_TIME, "UTC"));
		Double daysMumps = mFunctionUtil.dateJavaToMumps(cal.getTimeInMillis());
		Double sec = (daysMumps - daysMumps.longValue()) * 24d * 60d * 60d;
		Double fra = (sec - sec.longValue()) * 1000d;

		return daysMumps.longValue() + "," + sec.longValue() + "."
				+ fra.longValue();
	}

	public static String $zts() {
		throw new UnsupportedOperationException();
	}

	public static mListBuild $zu(int i) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public static Object $zutil(int i) {
		if (i == 5) {
			return "DEFAULT";
		} else if (i == 110) {
			return mFunctionUtil.hostName();
		}else if(i == 100){
		   if(System.getProperty("os.name").equals("Windows 7")||System.getProperty("os.name").equals("Windows 8")||System.getProperty("os.name").equals("Windows 9"))
		      return 0;
		   else{
			  return 1;
		   }
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static Object $zutil(Object obj1, Object obj2, Object obj3,
			Object obj4, Object obj5) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public static Object $zversion() {
		// TODO REVISAR IMPLEMENTAÇÃO PROVISÓRIA
		//return "Cache for Windows (x86-32) 2008.2.3 (Build 933) Tue May 12 2009 15:11:50 EDT";
		return "Oracle";
	}

	public static Boolean booleanConverter(Object obj) {
		Boolean bool = false;
		if (obj instanceof Boolean) {
			bool = Boolean.parseBoolean(String.valueOf(obj));
		} else {
			Double dbl = numberConverter(obj);
			if (dbl != 0) {
				bool = true;
			}
		}
		return bool;
	}

	public static Double castDouble(Object obj) {
		try {
			return (Double) obj;
		} catch (ClassCastException e) {
			return 0d;
		}
	}

	public static Integer castInt(Object obj) {
		try {
			return (Integer) obj;
		} catch (ClassCastException e) {
			return 0;
		}
	}

	public static Integer[] castIntArray(Object... obj) {
		if (obj == null) {
			return null;
		} else if (obj.length == 0) {
			return new Integer[] {};
		}

		final Integer[] array = new Integer[obj.length];
		for (int i = 0; i < obj.length; i++) {
			array[i] = castInt(obj[i]);
		}
		return array;
	}

	public static String castString(Object obj) {
		try {
			return (String) obj;
		} catch (ClassCastException e) {
			return null;
		}
	}

	public static String characterImpl(Integer... codes) {
		if (codes == null) {
			return null;
		}

		if (codes.length == 1 && codes[0] == null) {
			return null;
		}

		final Character[] chars = new Character[codes.length];
		for (int i = 0; i < codes.length; i++) {
			// Apenas os inteiros nao-negativos deve ser convertidos.
			if (codes[i] != null && codes[i] > 0) {
				chars[i] = Character.toChars(codes[i])[0];
			}
		}

		return generateString(chars, null, true);
	}

	public static String extractImpl(String string, int from, int to) {
		if (string == null) {
			return null;
		}
		try {
			return string.substring(from - 1, to);
		} catch (Exception e) {
			return null;
		}

	}

	public static int findImpl(String string, String substring, int start) {
		if (string == null || string.trim().length() == 0 || substring == null
				|| substring.length() == 0) {
			return 0;
		}

		if (start < 1 || start >= string.length()) {
			return 0;
		}

		string = string.substring(start);

		char[] substringChar = substring.toCharArray();
		char[] stringChar = string.toCharArray();

		int j = 0;
		int max = substringChar.length;
		for (int i = 0; i < stringChar.length; i++) {
			if (stringChar[i] == substringChar[j]) {
				j++;
			} else {
				j = 0;
			}

			if (j == max) {
				return ++i + (start + 1);
			}
		}
		return 0;
	}

	public static String generateString(Object[] array, String delimiter) {
		return generateString(array, delimiter, false);
	}

	public static String generateString(Object[] array, String delimiter,
			boolean avoidNull) {
		final StringBuilder result = new StringBuilder();
		final int indexToInsert = array.length - 1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = "";
			}
			if (avoidNull) {
				continue;
			}
			result.append(array[i]);
			if (delimiter != null && i < indexToInsert) {
				result.append(delimiter);
			}
		}
		return result.toString();
	}

	public static Integer integerConverter(Object number) {
		return numberConverter(number).intValue();
	}

	public static Object list(mListBuild list) {
		return list.firstElement();
	}

	public static Object list(mListBuild list, int position) {
		return list.element(position);
	}

	public static int listData(mListBuild list, int position) {
		return hasElementOnListObject(list, position) ? 1 : 0;
	}

	public static Object listGet(mListBuild list, int position,
			String defaultValue) {
		if (hasElementOnListObject(list, position)) {
			return list(list, position);
		} else {
			return defaultValue;
		}
	}

	public static Double numberConverter(Object obj) {
		Double dbl = null;
		try {
			dbl = Double.valueOf(String.valueOf(obj));
		} catch (NumberFormatException nfe) {
			String result = "";
			char[] charArray = obj.toString().toCharArray();
			boolean startNumber = false;
			boolean hasPoint = false;
			for (int i = 0; i < charArray.length; i++) {
				char c = charArray[i];
				if (!startNumber && (c == '+' || c == '-')) {
					if (result.isEmpty()) {
						result = String.valueOf(c);
					} else {
						if (result.equals(String.valueOf(c))) {
							result = "+";
						} else {
							result = "-";
						}
					}
					continue;
				}
				if (Character.isDigit(c)) {
					startNumber = true;
					result = result.concat(String.valueOf(c));
					continue;
				}
				if (c == '.') {
					startNumber = true;
					if (!hasPoint) {
						hasPoint = true;
						result = result.concat(String.valueOf(c));
						continue;
					}
				}
				if (result.isEmpty() || !startNumber) {
					result = "0";
				}
				break;
			}
			dbl = Double.valueOf(result);
		}
		return dbl;
	}

	public static String pieceImpl(String string, String delimiter, int index) {

		if (string == null) {
			return null;
		}

		return string.split(delimiter)[index - 1];
	}

	public static String pieceImpl(String string, String delimiter, int from,
			int to) {
		if (string == null) {
			return null;
		}

		if (from > to) {
			return null;
		}

		final String[] array = Arrays.copyOfRange(string.split(delimiter),
				from - 1, to);

		return generateString(array, delimiter);
	}

	public static String setPieceImpl(String string, String delimiter,
			Integer position, Object value) {
		Object[] array = string == null ? new Object[position] : string
				.toString().split(delimiter);
		if (position > array.length) {
			array = Arrays.copyOfRange(array, 0, position);
		}
		if (value == null) {
			value = "";
		}

		array[position - 1] = value.toString();

		return generateString(array, delimiter);
	}

	public static String translateImpl(String string, String oldCharsequence,
			String newCharsequence) {

		if (string == null || oldCharsequence == null
				|| oldCharsequence.length() == 0) {
			return string;
		}

		/*
		 * Esse mecanismo foi escolhido para evidar de criar muitas strings no
		 * pool e evitar o consumo de memoria excessivo.
		 */
		char[] stringChars = string.toCharArray();
		char[] oldChars = oldCharsequence.toCharArray();
		char[] newChars = (newCharsequence != null ? newCharsequence : "")
				.toCharArray();

		// Aqui vamos completar o newChars de substiticao pois quando
		// os tamanhos nao forem identicos isso indicara que os valores
		// excedentes devem ser substituidos pelo caracter "vazio".
		if (newChars.length < oldChars.length) {
			newChars = Arrays.copyOf(newChars, oldChars.length);
		}

		final StringBuilder result = new StringBuilder();
		char copy;
		for (int i = 0; i < stringChars.length; i++) {
			copy = stringChars[i];
			for (int j = 0; j < oldChars.length; j++) {
				if (copy == oldChars[j]) {
					if (newCharsequence != null) {
						copy = newChars[j];
					} else {
						copy = '\0';
					}
					break;
				}
			}

			// Caso o caracter seja vazio nao devemos inclui-lo na string
			// gerada.
			if ('\0' != copy) {
				result.append(copy);
			}

		}

		return result.toString();
	}

	public static Object zconvert(Object string, Object mode) {
		return zconvertImpl(castString(string), castString(mode));
	}

	public static String zconvertImpl(String string, String mode) {
		if (mode == null || mode.trim().length() == 0) {
			return string;
		}

		if ("L".equalsIgnoreCase(mode)) {
			string = string.toLowerCase();
		} else if ("U".equalsIgnoreCase(mode)) {
			string = string.toUpperCase();
		}
		return string;
	}

	private static boolean hasElementOnListObject(mListBuild list, int position) {
		Object elemet = list(list, position);
		return elemet != null && !"".equals(elemet.toString().trim());
	}

	private final mContext m$;

	public mFunction(mContext m$) {
		this.m$ = m$;
	}

	public Object $io() {
		return m$.getIO();
	}

	public Object $justify() {
		throw new UnsupportedOperationException();
	}

	public Object $lg(Object list) {
		return $listget(list, 1);
	}

	public Object $lg(Object list, Object position) {
		return $listget(list, position);
	}

	public Object $listfromstring(Object object) {
		return $listfromstring(object, ",");
	}

	public Object $listfromstring(Object object, Object delimiter) {
		return mListBuild.listFromString(String.valueOf(object), String.valueOf(delimiter));
	}

	public Object $listtostring(Object object, String string) {
		throw new UnsupportedOperationException();
	}

	public Object $normalize(Object num, int scale) {
		return $fnumber(num, "", scale);
	}

	public Object $number(String $extract) {
		throw new UnsupportedOperationException();
	}

	public Object $order(mVar var) {
		return $order(var, 1);
	}

	public Object $order(mVar var, Object dir) {
		final Object[] subs = var.getSubs();
		final int lenght = subs.length;

		final boolean isEmpty = lenght == 1 && subs[0].toString().length() == 0;
		final boolean isOrderOverVariables = lenght == 1 && !isEmpty;
		Object next = null;
		if (isOrderOverVariables) {
			boolean isPublic = subs[0].toString().indexOf("%") >= 0;
			boolean isGlobal = subs[0].toString().indexOf("^") >= 0;
			if (isGlobal) {
				throw new UnsupportedOperationException(
						"Does not be able order over global variables");
			}
			if (isPublic) {
				next = m$.getmDataPublic().order(subs,
						mFunctionUtil.numberConverter(dir).intValue());
			} else {
				next = m$.getmDataLocal().order(subs,
						mFunctionUtil.numberConverter(dir).intValue());
			}

			boolean isEndPublic = isPublic && "".equals(next);
			if (isEndPublic) {
				next = m$.getmDataLocal().order(new Object[] { "" },
						mFunctionUtil.numberConverter(dir).intValue());
			}

		} else if (isEmpty) {
			mDataAccess mdata = m$.hasPublicVariables() ? m$.getmDataPublic()
					: m$.getmDataLocal();
			next = new mVar(new Object[] { "" }, mdata).order((Integer) dir);
		} else {
			next = var.order(mFunctionUtil.numberConverter(dir).intValue());
		}
		return next;
	}

	public Object $query(mVar reference) {
		return $query(reference, 1, null);
	}

	public Object $query(mVar reference, Object direction) {
		return $query(reference, direction, null);
	}

	public Object $query(mVar reference, Object direction, mVar target) {
		int _direction = (mFunctionUtil.numberConverter(direction) == -1) ? -1
				: 1;
		mVar qreference = reference;
		Object[] qsubscripts = reference.getSubs();
		Object qorder = qsubscripts[qsubscripts.length - 1];
		boolean first = true;
		if (_direction > 0) {
			while (true) {
				if ((qorder == null) || (qorder.equals(""))) {
					if (!first) {
						if (qsubscripts.length <= 1) {
							return "";
						}
						Object[] qnewsubscripts = new Object[qsubscripts.length - 1];
						for (int i = 0; i < qsubscripts.length - 1; i++) {
							qnewsubscripts[i] = qsubscripts[i];
						}
						qsubscripts = qnewsubscripts;
						qreference = new mVar(qsubscripts, reference.getmData());
					}
					if (qsubscripts.length <= 1) {
						return "";
					}
					qorder = $order(qreference, _direction);
					qsubscripts[qsubscripts.length - 1] = qorder;
				} else {
					qreference = qreference.s$("");
					qsubscripts = qreference.getSubs();
					qorder = $order(qreference, _direction);
					qsubscripts[qsubscripts.length - 1] = qorder;
				}
				first = false;
				if ((qorder == null) || (qorder.equals(""))) {
					continue;
				}
				qreference = new mVar(qsubscripts, reference.getmData());
				if (qreference.getValue() == null) {
					continue;
				}
				break;
			}
		} else {
			while (true) {
				if ((first) && (qorder != null) && !(qorder.equals(""))) {
					qorder = $order(qreference, _direction);
					qsubscripts[qsubscripts.length - 1] = qorder;
				}
				if ((qorder == null) || (qorder.equals(""))) {
					if (qsubscripts.length <= 1) {
						return "";
					}
					Object[] qnewsubscripts = new Object[qsubscripts.length - 1];
					for (int i = 0; i < qsubscripts.length - 1; i++) {
						qnewsubscripts[i] = qsubscripts[i];
					}
					qsubscripts = qnewsubscripts;
					qreference = new mVar(qsubscripts, reference.getmData());
					if (qsubscripts.length <= 1) {
						return "";
					}
					qorder = qsubscripts[qsubscripts.length - 1];
				} else {
					qreference = qreference.s$("");
					qsubscripts = qreference.getSubs();
					qorder = $order(qreference, _direction);
					qsubscripts[qsubscripts.length - 1] = qorder;
					first = false;
				}
				if ((qorder == null) || (qorder.equals(""))) {
					continue;
				}
				qreference = new mVar(qsubscripts, reference.getmData());
				if (qreference.getValue() == null) {
					continue;
				}
				break;
			}
		}
		String result = mFunctionUtil.toString(qsubscripts[0]);
		if (qsubscripts.length > 1) {
			result = result + "(";
			String strsub;
			for (int i = 1; i < qsubscripts.length; i++) {
				strsub = mFunctionUtil.toString(qsubscripts[i]);
				if (mFunctionUtil.isCanonicalNumeric(strsub)) {
					result = result + ((i > 1) ? "," : "") + strsub;
				} else {
					result = result + ((i > 1) ? "," : "") + "\""
							+ mFunctionUtil.escapeValue(strsub) + "\"";
				}
			}
			result = result + ")";
		}

		if (target != null) {
			target.set(m$.indirectVar(result));
		}
		return result;
	}

	public Object $sortbegin(Object object) {
		throw new UnsupportedOperationException();
	}

	public Object $sortend(Object object) {
		throw new UnsupportedOperationException();
	}

	@TODO
	public Object $test() {
		mVar $test = m$.var("$test");
		Object resp = $test.getValue();
		if (resp == null) {
			resp = 1;
			$test.set(1);
		} else if (m$op.Equal(resp, 0)) {
			$test.set(1);
		}
		return resp;
	}

	public Object $text(Object object) {
		throw new UnsupportedOperationException();
	}

	public Object $view(Object $zutil, Object negative, int i) {
		throw new UnsupportedOperationException();
	}

	public Object $x() {
		throw new UnsupportedOperationException();
	}

	public Object $y() {
		throw new UnsupportedOperationException();
	}

	public Object $zbitget(Object $zversion, int i) {
		throw new UnsupportedOperationException();
	}

	public Object $zboolean(Object arg1, Object arg2, int bit_op) {
		switch (bit_op) {
		case 1:
			return (mFunctionUtil.booleanConverter(mFunctionUtil
					.integerConverter(arg1)
					& mFunctionUtil.integerConverter(arg1))) ? 1 : 0;
		default:
			break;
		}
		// if (bit_op == ) {

		// }
		throw new UnsupportedOperationException();
	}

	public Object $zeof() {
		mVar $ZEOF = m$.var("$ZEOF");
		if ($ZEOF.getValue() == null) {
			$ZEOF.set(0);
		}
		return $ZEOF.get();
	}

	public Object $zerror() {
		return m$.var("$ZERROR").get();
	}

	public Object $zf(int i, int j, Object object) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object $zf(int i, int j, Object object, Object object2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object $zf(int i, Object object, Object object2, Object $get,
			Object $get2, Object $get3, Object $get4, Object $get5,
			Object $get6, Object $get7, Object $get8, Object $get9,
			Object $get10) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Object $zf(Object negative, Object concat) {// Este comando só
														// funciona no windows
														// Hilde 26/08/2014
		if (negative.equals(-1)) {

			try {
				Runtime.getRuntime().exec("cmd /c " + concat.toString());

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return 1;
	}

	public Object $zlog(Object divide) {
		throw new UnsupportedOperationException();
	}

	public Object $zobjnext(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public Object $zobjclassmethod(Object classname, String methodname, Object... args) {
		return m$.fnc$(m$util.toString(classname)+"."+methodname,args);
	}

	public Object $zobjmethod(Object object, String methodname, Object... args) {
		return m$.fnc$(object,methodname,args);
	}

	public Object $zobjproperty(Object object, Object property) {
		return m$.prop(object, m$util.toString(property)).get();
	}

	public Object $zorder(mVar reference) {
		return $query(reference);
	}

	public Object $zreference() {
		mVar lastVar = m$.lastVar();
		if (lastVar == null) {
			return "";
		}
		String zref = lastVar.getName();
		Object[] subs = lastVar.getParameters();
		for (int i = 0; i < subs.length; i++) {
			String x = mFunctionUtil.toString(subs[i]);
			zref += ((i == 0) ? "(" : ",");
			if (mFunctionUtil.isCanonicalNumeric(x)) {
				zref += x;
			} else {
				zref += "\"" + x + "\"";
			}
		}
		zref += ((subs.length > 0) ? ")" : "");
		return zref;
	}

	public Object $ztimeh(Object object, int i) {
		throw new UnsupportedOperationException();
	}

	public Object $ztimeh(Object object, int i, String string) {
		throw new UnsupportedOperationException();
	}

	public Object $ztrap() {
		return m$.var("$ZTRAP").getSuppressedNullValue();
	}

	public Object $zutil(int i, Object... obj) {
		if (i == 139) {
			return null;
		}
		if (i == 28) {
			Object param1 = obj.length > 0 && obj[0] != null ? obj[0] : "";
			Object param2 = obj.length > 1 && obj[1] != null ? obj[1] : "";
			Object param3 = obj.length > 2 && obj[2] != null ? obj[2] : "";
			if (param2.toString().equals("5")) {
				return param1.toString().toUpperCase();
			}
		}
		if (i == 12 &&  obj.length > 0 && obj[0] != null) {
			Object param1 = obj[0];
			if (!param1.toString().contains("\\")) {
				return "c:\\alphalinc\\sesdf-bazaar\\database\\".concat(param1.toString()).concat("\\");
			} else {
				return "c:\\".concat(param1.toString()).concat("\\");
			}
		}
		if(i == 54){
			Object param1 = obj.length > 0 && obj[0] != null ? obj[0] : "";
			if(param1.toString().equals("0")){
				return mFunctionUtil.hostName();				
			}
		}
		if (i == 147 && obj.length > 0 && obj[0] != null) {
			Object param1 = obj[0];
			Path path = FileSystems.getDefault().getPath(param1.toString());
			return path.toAbsolutePath();
		}
		if (i == 140) {
			Object param1 = obj.length > 0 && obj[0] != null ? obj[0] : "";
			Object param2 = obj.length > 1 && obj[1] != null ? obj[1] : "";
			if(param1.equals(4)){
				File file = new File(param1.toString());
				if(file.exists()){
					return 0;
				}else{
					return -2;
				}
			}
		}
		if (i == 68) {
			Object param1 = obj.length > 0 && obj[0] != null ? obj[0] : "";
			Object param2 = obj.length > 1 && obj[1] != null ? obj[1] : "";
			Object param3 = obj.length > 2 && obj[2] != null ? obj[2] : "";
			if (param1.toString().equals("40") && param2.toString().equals("1")) {
				return $zeof();
			}
		} 
		throw new UnsupportedOperationException();	
	}

	public Object $zversion(int i) {
		String version = Objects.toString(System.getProperties().get("os.name"),"");
		if(i==1){
			if(version.toLowerCase().contains("vms")){
				return 1;
			}
			if(version.toLowerCase().contains("windows")){
				return 2;
			}
			if(version.toLowerCase().contains("unix")){
				return 3;
			}		
			throw new UnsupportedOperationException();
		}else{
			throw new UnsupportedOperationException();
		}
	}

	public Object length(Object string) {
		return length(castString(string));
	}

	public Object length(Object string, Object delimiter) {
		return lengthImpl(castString(string), castString(delimiter));
	}

	public int lengthImpl(String string) {
		if (string == null) {
			return 0;
		}

		return string.length();
	}
	
	public Object lengthImpl(String string, String delimiter) {
		if (delimiter == null && string == null) {
			return 0;
		}

		if (delimiter != null && string == null) {
			return 1;
		}

		return string.split(delimiter).length;
	}

	public Object pieceImpl(String string, String delimiter) {
		return $piece(string, delimiter, 1);
	}	

	
}
