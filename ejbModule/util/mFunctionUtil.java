package util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mLibrary.mFunction;
import mLibrary.mListBuild;
import mLibrary.exceptions.MethodNotFoundException;
import br.com.innovatium.mumps2java.todo.TODO;

public final class mFunctionUtil {

	public static Boolean booleanConverter(Object obj) {
		Boolean bool = false;
		if (obj != null) {
			if (obj instanceof Boolean) {
				bool = Boolean.parseBoolean(String.valueOf(obj));
			}
			else {
				Double dbl = numberConverter(obj);
				if (dbl != 0) {
					bool = true;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Null can not be Boolean.");
		}
		return bool;
	}

	public static Double castDouble(Object obj) {
		try {
			return Double.valueOf(String.valueOf(obj));
		}
		catch (java.lang.NumberFormatException e) {
			return null;
		}
		catch (ClassCastException e) {
			return null;
		}
	}

	public static Integer castInt(Object obj) {
		try {
			if (obj.toString().isEmpty()) {
				return 0;
			}
			return castDouble(obj).intValue();
		}
		catch (java.lang.NullPointerException e) {
			return null;
		}
		catch (ClassCastException e) {
			return null;
		}
	}

	public static Integer[] castIntArray(Object... obj) {
		if (obj == null) {
			return null;
		}
		else if (obj.length == 0) {
			return new Integer[] {};
		}

		final Integer[] array = new Integer[obj.length];
		for (int i = 0; i < obj.length; i++) {
			array[i] = castInt(obj[i]);
		}
		return array;
	}

	public static String castString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public static String characterImpl(Integer... codes) {
		if (codes == null) {
			return null;
		}

		if (codes.length == 1 && codes[0] == null) {
			return null;
		}

		final String[] chars = new String[codes.length];
		for (int i = 0; i < codes.length; i++) {
			// Apenas os inteiros nao-negativos deve ser convertidos.
			if (codes[i] != null && codes[i] >= 0) {
				chars[i] = String.valueOf(Character.toChars(codes[i])[0]);
			}
		}

		return generateString(chars, null, true);
	}

	public static Object[] concat(Object[] dest, Object[] orig) {
		return DataStructureUtil.concat(dest, orig);
	}

	public static Object[] concatSinceLastSubscript(Object[] dest, Object[] orig) {
		return DataStructureUtil.concatSinceLast(dest, orig);
	}

	public static String convertMumpsSqlFieldToJavaSqlField(String mumpsSql) {
		// [
		// $$RemoveMark^COMViewSQL(SQLUser.WWW004.ApplicationName,"0","default")

		// $$RemoveMark^COMViewSQL(%upper(SQLUser.MEDPatient.Tel),"0","default"
		if (mFunctionUtil.isMatcher(mumpsSql, "%upper(+$piece(", ",\".\",1))")) {
			return mFunctionUtil.matcher(mumpsSql, "%upper(+$piece(", ",\".\",1))")[0];
		}
		else if (mFunctionUtil.isMatcher(mumpsSql, "(%upper(+$piece(", ",\".\",1))")) {
			return mFunctionUtil.matcher(mumpsSql, "(%upper(+$piece(", ",\".\",1))")[0];
		}
		else if (mFunctionUtil.isMatcher(mumpsSql, " $$RemoveMark^COMViewSQL(%upper(", "),\"0\",\"", "\")")) {
			return mFunctionUtil.matcher(mumpsSql, " $$RemoveMark^COMViewSQL(%upper(", "),\"0\",\"", "\")")[0];
		}
		else if (mFunctionUtil.isMatcher(mumpsSql, " $$RemoveMark^COMViewSQL(", ",\"0\",\"", "\")")) {
			return mFunctionUtil.matcher(mumpsSql, " $$RemoveMark^COMViewSQL(", ",\"0\",\"", "\")")[0];
		}
		else if (mFunctionUtil.isMatcher(mumpsSql, "%upper(", ")")) {
			return mFunctionUtil.matcher(mumpsSql, "%upper(", ")")[0];
		}
		else {
			return mumpsSql;
			// throw new
			// UnsupportedOperationException("Criteria not implemented for "+mumpsSql);
		}
	}

	public static String convertMumpsSqlValueToJavaSqlValue(String mumpsSql) {
		if (mFunctionUtil.isMatcher(mumpsSql, "\"", "\"")) {
			return mFunctionUtil.matcher(mumpsSql, "\"", "\"")[0];
		}
		else {
			return mumpsSql;
			// throw new
			// UnsupportedOperationException("Criteria not implemented for "+mumpsSql);
		}
	}

	public static String dateCodeFormatMumpsToJava(Object dFormat) {
		return dateCodeFormatMumpsToJava(dFormat, 0);
	}

	public static String dateCodeFormatMumpsToJava(Object dFormat, int length) {
		String dFormatStr = "MM/dd/yy";
		String dFormatCod = String.valueOf(dFormat);
		if (dFormatCod.equals("-1")) {
			// dFormatStr = "";
		}
		else if (dFormatCod.equals("1")) {
			dFormatStr = "MM/dd/yy";
		}
		else if (dFormatCod.equals("2")) {
			dFormatStr = "dd MMM yy";
		}
		else if (dFormatCod.equals("3")) {
			dFormatStr = "yyyy-MM-dd";
		}
		else if (dFormatCod.equals("4")) {
			dFormatStr = "dd/MM/yy";
		}
		else if (dFormatCod.equals("5")) {
			dFormatStr = "MMM d, yyyy";
		}
		else if (dFormatCod.equals("6")) {
			dFormatStr = "MMM d yyyy";
		}
		else if (dFormatCod.equals("7")) {
			dFormatStr = "MMM dd yy";
		}
		else if (dFormatCod.equals("8")) {
			if (length == 6) {
				dFormatStr = "yyMMdd";
			}
			else {
				dFormatStr = "yyyyMMdd";
			}
		}
		else if (dFormatCod.equals("9")) {
			dFormatStr = "MMMM d, yyyy";
		}
		else if (dFormatCod.equals("10")) {
			dFormatStr = "F";
		}
		else if (dFormatCod.equals("11")) {
			dFormatStr = "EEE";
		}
		else if (dFormatCod.equals("12")) {
			dFormatStr = "EEEE";
		}
		else if (dFormatCod.equals("13")) {
			dFormatStr = "ddd";
		}
		else if (dFormatCod.equals("14")) {
			dFormatStr = "D";
		}
		else {
			throw new UnsupportedOperationException();
		}
		return dFormatStr;
	}

	public static Double dateJavaToMumps(Object internalDate) {
		Calendar cal1 = Calendar.getInstance(getTimeZone());
		cal1.clear();
		cal1.set(1840, 11, 31, 0, 0, 0);
		cal1.setLenient(false);

		Calendar cal2 = Calendar.getInstance(getTimeZone());
		cal2.clear();
		cal2.setTimeInMillis(0);
		cal2.setLenient(false);

		Long dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		Long millis = Long.valueOf(String.valueOf(internalDate));
		Double days = (millis + dateDif) / 24d / 60d / 60d / 1000d;

		return days - (days % 1);
	}

	public static Double dateMumpsToJava(Object internalDate) {
		Calendar cal1 = Calendar.getInstance(getTimeZone());
		cal1.clear();
		cal1.set(1840, 11, 31, 0, 0, 0);
		cal1.setLenient(false);

		Calendar cal2 = Calendar.getInstance(getTimeZone());
		cal2.clear();
		cal2.setTimeInMillis(0);
		cal2.setLenient(false);

		Long dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		Double days = Double.valueOf(String.valueOf(numberConverter(internalDate)));
		Double dateMilli = days * 24d * 60d * 60d * 1000d - dateDif;

		return dateMilli;

	}
	public static Date dateMumpsToJavaDate(Object internalDate) {
		TimeZone tz = TimeZone.getDefault();
		TimeZone.setDefault(mFunctionUtil.getTimeZone());
		GregorianCalendar dt = new GregorianCalendar(
				mFunctionUtil.getTimeZone());
		dt.setTimeInMillis(mFunctionUtil.dateMumpsToJava(internalDate).longValue());
		dt.setLenient(false);
		Date returnDate = dt.getTime();
		TimeZone.setDefault(tz);		
		//new Date(dateMumpsToJava(internalDate));
		return returnDate;
	}

	public static String dateTimeJavaToMumps(Object internalDate) {
		Double daysMumps = dateTimeJavaToMumpsDouble(internalDate);
		Double sec = (daysMumps - daysMumps.longValue()) * 24d * 60d * 60d;
		return daysMumps.longValue() + "," + sec.longValue();
	}

	public static Double dateTimeJavaToMumpsDouble(Object internalDate) {
		return dateTimeJavaToMumpsDouble(internalDate, getTimeZone());
	}

	public static Double dateTimeJavaToMumpsDouble(Object internalDate, TimeZone timeZone) {
		Calendar cal1 = Calendar.getInstance(timeZone);
		cal1.clear();
		cal1.set(1840, 11, 31, 0, 0, 0);
		cal1.setLenient(false);

		Calendar cal2 = Calendar.getInstance(timeZone);
		cal2.clear();
		// cal2.setTimeInMillis(0);
		cal2.setLenient(false);

		Long dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		Long millis = Long.valueOf(String.valueOf(internalDate));
		Double secs = (millis + dateDif) / 24d / 60d / 60d / 1000d;

		return secs;
	}

	public static Double dateTimeMumpsToJava(Object internalDate) {
		Calendar cal1 = Calendar.getInstance(getTimeZone());
		cal1.clear();
		cal1.set(1840, 11, 31, 0, 0, 0);
		cal1.setLenient(false);

		Calendar cal2 = Calendar.getInstance(getTimeZone());
		cal2.clear();
		cal2.setTimeInMillis(0);
		cal2.setLenient(false);

		Long dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		String[] internalValue = toString(internalDate).split(",", -1);
		Double days = 0D, secs = 0D;

		if (internalValue.length > 0) {
			days = Double.valueOf(String.valueOf(numberConverter(internalValue[0])));
		}
		if (internalValue.length > 1) {
			secs = Double.valueOf(String.valueOf(numberConverter(internalValue[1])));
		}

		Double dateMilli = days * 24d * 60d * 60d * 1000d - dateDif + secs * 1000d;

		return dateMilli;
	}

	public static String escapeJS(String string) {
		String str = String.valueOf(string);
		StringBuffer writer = new StringBuffer(str.length() * 2);

		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);

			// handle unicode
			if (ch > 0xfff) {
				writer.append("\\u");
				writer.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
			}
			else if (ch > 0xff) {
				writer.append("\\u0");
				writer.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
			}
			else if (ch > 0x7f) {
				writer.append("\\u00");
				writer.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
			}
			else if (ch < 32) {
				switch (ch) {
				case '\b':
					writer.append('\\');
					writer.append('b');
					break;
				case '\n':
					writer.append('\\');
					writer.append('n');
					break;
				case '\t':
					writer.append('\\');
					writer.append('t');
					break;
				case '\f':
					writer.append('\\');
					writer.append('f');
					break;
				case '\r':
					writer.append('\\');
					writer.append('r');
					break;
				default:
					if (ch > 0xf) {
						writer.append("\\u00");
						writer.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
					}
					else {
						writer.append("\\u000");
						writer.append(Integer.toHexString(ch).toUpperCase(Locale.ENGLISH));
					}
					break;
				}
			}
			else {
				switch (ch) {
				case '\'':
					// If we wanted to escape for Java strings then we would
					// not need this next line.
					writer.append('\\');
					writer.append('\'');
					break;
				case '"':
					writer.append('\\');
					writer.append('"');
					break;
				case '\\':
					writer.append('\\');
					writer.append('\\');
					break;
				default:
					writer.append(ch);
					break;
				}
			}
		}

		return writer.toString();
	}

	public static String escapeURL(String url) {
		try {
			return URLEncoder.encode(String.valueOf(url), "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		}
	}

	public static String escapeValue(String value) {
		if (value == null)
			return value;
		return value.replace("\"", "\"\"");
	}

	public static boolean existsToDispath(String methodName) {
		int lastIndex = methodName.lastIndexOf(".");
		final String clazz = methodName.substring(0, lastIndex);
		final String method = methodName.substring(lastIndex + 1);

		Method m = null;
		try {
			Class<?> clazz1 = Class.forName(clazz);
			while (clazz1 != null) {

				Method[] methods = Class.forName(clazz).getMethods();
				// Method[] methods = Macros.class.getMethods();
				for (Method met : methods) {
					if (method.equals(met.getName())) {
						m = met;
						break;
					}

				}
				if (m != null) {
					break;
				}
				clazz1 = clazz1.getSuperclass();

			}
		}
		catch (Exception e) {
			return false;
		}
		return m != null;

	}

	public static int findImpl(String string, String substring, int start) {
		if (string == null || string.trim().length() == 0 || substring == null) {
			return 0;
		}

		if (start < 1 || start >= string.length()) {
			return 0;
		}

		if (substring.isEmpty()) {
			return start;
		}
		int pos = string.indexOf(substring, start - 1);
		if (pos >= 0) {
			return pos + substring.length() + 1;
		}

		return 0;
	}

	public static Map<String, String> generateSqlSentenceMap(String sql) {
		Map<String, String> sqlMap = new HashMap<String, String>();
		String key = "";
		// INSERIR OS COMANDOS NA ORDEM DOS ÚLTIMOS PARA OS PRIMEIROS.
		key = " order by ";
		if (sql.contains(key)) {
			String suffix = sql.split(key)[1];
			sqlMap.put(key, suffix);
			sql = sql.replace(key.concat(suffix), "");
		}
		key = " where ";
		if (sql.contains(key)) {
			String suffix = sql.split(key)[1];
			sqlMap.put(key, suffix);
			sql = sql.replace(key.concat(suffix), "");
		}
		return sqlMap;
	}

	// Recupera a lista de subscritos da global. utilizado em $qsubscript e
	// $qlength
	// já remove o escape de aspas duplas "" do mumps colocado propositalmente
	// no $query ou $name.
	public static List<String> getSubscriptList(String nameValue) {

		List<String> resultList = new ArrayList<String>();

		int initialSubscriptPosition = nameValue.indexOf("(");

		if (initialSubscriptPosition != -1) {
			initialSubscriptPosition++;
			int lastSubscriptCharacter = nameValue.length() - 1;

			String subscripts = nameValue.substring(initialSubscriptPosition, lastSubscriptCharacter);
			char doubleQuote = '"';
			boolean inLiteral = false;
			char c;
			char nextC;
			final StringBuilder element = new StringBuilder();

			for (int i = 0; i < subscripts.length(); i++) {
				c = subscripts.charAt(i);
				if (c == doubleQuote) {
					if (inLiteral) {
						nextC = (i + 1 < subscripts.length()) ? subscripts.charAt(i + 1) : ',';
						if (nextC == doubleQuote) { // escaped
							element.append(c);
							i++;
							continue;
						}
					}
					inLiteral = inLiteral ? false : true;
					continue;
				}
				if (c == ',' && !inLiteral) {
					resultList.add(element.toString());
					element.delete(0, element.length()); // reinicializa element
					continue;
				}
				element.append(c);

			}
			resultList.add(element.toString());
		}
		return resultList;
	}

	public static TimeZone getTimeZone() {
		return new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC");
	}

	public static int getVariableType(Object[] subs) {
		return DataStructureUtil.getVariableType(subs);
	}

	public static String hostName() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostName();
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Integer integerConverter(Object number) {
		return numberConverter(number).intValue();
	}

	public static boolean isCanonicalNumeric(String val) {
		if (val.length() == 0) {
			return false;
		}
		int initialDigit = (val.charAt(0) == '-') ? 1 : 0;

		int dotCount = 0;
		for (int i = initialDigit; i < val.length(); i++) {
			char c = val.charAt(i);
			if (c <= '9' && c >= '0')
				continue;
			if (c == '.') { // Conforme o NLS o separador de decimais pode
				// mudar. FIXME
				dotCount++;
				if (dotCount > 1)
					return false;
				continue;
			}
			return false;
		}
		// Neste ponto garante-se que são apenas digitos e até um '.', e/ou até
		// um sinal '-' em val

		if (val.endsWith(".") || val.endsWith("-"))
			return false;

		// verifica se o numero é decimal iniciado ou terminado com zeros não
		// significativos.
		if (dotCount > 0 && (val.charAt(initialDigit) == '0' || val.endsWith("0")))
			return false; // ex. 0.01 ou 0.001 ou 2.20

		if (val.charAt(initialDigit) == '0' && val.length() > 1) { // 0 -> ok,
			// 00 ->
			// nok, -0
			// -> nok
			return false;
		}
		return true;

	}

	public static boolean isMatcher(String line, String... patterns) {
		return matcher(line, false, 0, patterns) != null;
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

	public static Object listGet(mListBuild list, int position, String defaultValue) {
		if (hasElementOnListObject(list, position)) {
			return list(list, position);
		}
		else {
			return defaultValue;
		}
	}

	public static String[] matcher(String line, boolean replaceLast, Object group, String... patterns) {

		// String to be scanned to find the pattern.
		// String line = "This order was placed for QT3000! OK?";
		// String pattern = "(.*)(\\d+)(.*)";

		// Create a Pattern object
		String pattern = "";
		for (int i = 0; i < patterns.length; i++) {
			if (i > 0) {
				pattern = pattern.concat(".*?");
			}
			pattern = pattern.concat("(" + Pattern.quote(patterns[i]) + "{1,1})");
		}
		Pattern r = Pattern.compile(pattern);
		StringBuilder lineTemp = new StringBuilder(line);
		// Now create matcher object.
		Matcher m = r.matcher(line);
		if (m.find()) {
			if (group instanceof Integer) {
				return new String[] { m.group((Integer) group) };
			}
			if (group instanceof String) {
				return new String[] { m.group((String) group) };
			}
			String del = "\n\n\n";
			for (int i = 1; i <= m.groupCount(); i++) {
				String str = m.group(i);
				int idxStart;
				if (replaceLast) {
					idxStart = lineTemp.lastIndexOf(str);
				}
				else {
					idxStart = lineTemp.indexOf(str);
				}
				lineTemp.replace(idxStart, idxStart + str.length(), del);
			}
			String[] strArray = lineTemp.toString().split(del);
			List<String> lstr = new ArrayList<String>();
			for (int i = 0; i < strArray.length; i++) {
				if (!strArray[i].isEmpty()) {
					lstr.add(strArray[i]);
				}
			}
			return lstr.toArray(new String[] {});
		}
		return null;
	}

	public static String[] matcher(String line, String... patterns) {
		return matcher(line, false, null, patterns);
	}

	public static String[] matcherLast(String line, String... patterns) {
		return matcher(line, true, null, patterns);
	}

	public static String normalizeClassname(String className) {
		String result;
		if (className.contains(".")) {
			result = className;
		}
		else if (!className.startsWith("$")) {
			result = "User.".concat(className);
		}
		else {
			result = className.replaceFirst("\\$", "$Library.");
		}
		return result;
	}

	public static Object normalizeCollation(Object val) {
		if (val instanceof String) {
			String stringValue = String.valueOf(val);
			if (isCanonicalNumeric(stringValue)) {
				return Double.parseDouble(stringValue);
			}
		}
		return val;
	}

	public static Double numberConverter(Object obj) {
		if (obj == null || obj instanceof mListBuild) {
			return 0d;
		}
		Double dbl = null;
		if (obj instanceof Boolean) {
			return (Boolean.parseBoolean(String.valueOf(obj)) ? 1d : 0d);
		}
		if (obj instanceof Double) {
			return (Double) obj;
		}
		if (obj instanceof Long) {
			return ((Long) obj).doubleValue();
		}
		if (obj instanceof Integer) {
			return ((Integer) obj).doubleValue();
		}
		try {
			dbl = Double.valueOf(String.valueOf(obj));
		}
		catch (NumberFormatException nfe) {
			String result = "";
			char[] charArray = obj.toString().toCharArray();
			boolean startNumber = false;
			boolean hasPoint = false;
			for (int i = 0; i < charArray.length; i++) {
				char c = charArray[i];
				if (!startNumber && (c == '+' || c == '-')) {
					if (result.isEmpty()) {
						result = String.valueOf(c);
					}
					else {
						if (result.equals(String.valueOf(c))) {
							result = "+";
						}
						else {
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
				else if (hasPoint && !startNumber) {
					result = "";
				}
				if (c == '.') {
					if (!hasPoint) {
						hasPoint = true;
						result = result.concat(String.valueOf(c));
						continue;
					}
				}
				break;
			}
			if (result.isEmpty() || !startNumber) {
				result = "0";
			}
			dbl = Double.valueOf(result);
		}
		return dbl;
	}

	public static Object pieceImpl(Object string, String delimiter, int index) {

		if (string == null) {
			return null;
		}
		if (string instanceof mListBuild) {
			if (index != 1) {
				return "";
			}
			return string;
		}
		String stringCast = mFunctionUtil.toString(string);
		String[] splitStr = stringCast.split(Pattern.quote(delimiter));
		if (splitStr.length >= index && index > 0) {
			return splitStr[index - 1];
		}
		else {
			return "";
		}
	}

	public static Object pieceImpl(Object string, String delimiter, int from, int to) {
		if (string == null) {
			return null;
		}

		if (from > to) {
			return "";
		}

		if (string instanceof mListBuild) {
			return string;
		}
		String stringCast = string.toString();
		if (from > stringCast.length()) {
			return "";
		}
		String[] strSplit = stringCast.split(Pattern.quote(delimiter), -1);
		if (to > strSplit.length) {
			to = strSplit.length;
		}
		final String[] array = Arrays.copyOfRange(strSplit, from - 1, to);

		return generateString(array, delimiter);
	}

	public static Object pieceImpl(String string, String delimiter) {
		return mFunction.$piece(string, delimiter, 1);
	}

	/*
	 * Esse metodo foi criado para ser reaproveitado nos casos em que temos
	 * operacoes artimeticas executadas no mumps e o resultado retornado fica fora
	 * do padrao de arredondamento, por exemplo: no mumps temos 0 + 0 = 0, mas no
	 * java temos a soma de doubles 0 + 0 = 0.0. Assim resolveremos esse conflito
	 * na classe mOperation.java
	 */
	public static Object round(Double value) {
		Double dbl = value;
		Long decimal = dbl.longValue();
		int index = dbl.toString().indexOf(".");
		int decLen = index > 0 && dbl > decimal ? dbl.toString().substring(index + 1).length() : 0;

		BigDecimal result = BigDecimal.valueOf(dbl).setScale(decLen, BigDecimal.ROUND_HALF_UP);
		if (decLen > 0) {
			result = result.stripTrailingZeros();
		}

		return result.toString();
	}

	public static String round(Double value, int scale) {
		String digit[] = String.valueOf(value).split("\\.");
		int length = digit.length;
		if (length <= 1) {
			StringBuilder build = new StringBuilder(digit[0]).append(".");
			for (int i = 0; i < scale; i++) {
				build.append("0");
			}
			return build.toString();
		}
		else if (digit.length == 2 && (length = digit[1].length()) < scale) {
			StringBuilder build = new StringBuilder(digit[0]).append(".").append(digit[1]);
			scale = scale - length;
			for (int i = 0; i < scale; i++) {
				build.append("0");
			}
			return build.toString();
		}

		return BigDecimal.valueOf(value).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static String setPieceImpl(String string, String delimiter, Integer position, Object value, Integer to) {
		Object[] array = string == null ? new Object[position] : string.toString().split(Pattern.quote(delimiter), -1);
		if (position > array.length) {
			array = Arrays.copyOfRange(array, to == null ? 0 : position, to == null ? position : to);
		}
		if (value == null) {
			value = "";
		}
		if (position >= 1) {

			array[position - 1] = toString(value);
			return generateString(array, delimiter);
		}
		else {

			return "";
		}
	}

	public static String setPieceImpl(String string, String delimiter, Integer position, String value) {
		if (string == null || position < 0) {
			return string;
		}

		final String[] array = string.split(Pattern.quote(delimiter));
		if (value == null) {
			value = "";
		}
		array[position - 1] = value;

		return generateString(array, delimiter);
	}

	public static String[] splitDelimiter(String string) {
		if (string == null) {
			return null;
		}
		return string.split("~", -1);
	}

	public static String timeCodeFormatMumpsToJava(Object tFormat) {
		String dFormatStr = "hh:mm:ss";
		String dFormatCod = String.valueOf(tFormat);
		if (dFormatCod.equals("-1")) {
			// dFormatStr = "";
		}
		else if (dFormatCod.equals("1")) {
			dFormatStr = "HH:mm:ss";
		}
		else if (dFormatCod.equals("2")) {
			dFormatStr = "HH:mm";
		}
		else if (dFormatCod.equals("3")) {
			dFormatStr = "hh:mm:ss";
		}
		else if (dFormatCod.equals("4")) {
			dFormatStr = "hh:mm";
		}
		else if (dFormatCod.equals("5")) {
			dFormatStr = "HH:mm:ss";
		}
		else if (dFormatCod.equals("6")) {
			dFormatStr = "HH:mm";
		}
		else if (dFormatCod.equals("7")) {
			throw new UnsupportedOperationException();
		}
		else if (dFormatCod.equals("8")) {
			throw new UnsupportedOperationException();
		}
		return dFormatStr;
	}

	@TODO
	public static String toString(Object expression) {

		if (expression == null) {
			// throw new
			// IllegalArgumentException("The argument must not be null");
		}

		if (expression instanceof Double) {
			return String.valueOf(round((Double) expression));
		}
		if (expression instanceof Boolean) {
			Boolean bln = (Boolean) expression;
			return bln ? "1" : "0";
		}
		if (expression instanceof mListBuild) {
			mListBuild lb = (mListBuild) expression;
			return lb.getString();
		}
		if (expression == null) {
			// return null;
			return "";
		}
		return String.valueOf(expression);
	}

	public static String translateImpl(String string, String oldCharsequence, String newCharsequence) {

		if (string == null) {
			throw new IllegalArgumentException("String must no be null");
		}

		if (oldCharsequence == null || oldCharsequence.length() == 0) {
			return string;
		}

		/*
		 * Esse mecanismo foi escolhido para evidar de criar muitas strings no pool
		 * e evitar o consumo de memoria excessivo.
		 */
		char[] stringChars = string.toCharArray();
		char[] oldChars = oldCharsequence.toCharArray();
		char[] newChars = (newCharsequence != null ? newCharsequence : "").toCharArray();

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
					}
					else {
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

	public static String unescapeURL(String url) {
		return unescapeURL(url, "UTF-8");
	}

	public static String unescapeURL(String url, String charset) {
		try {
			return URLDecoder.decode(String.valueOf(url), charset);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		}
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
		}
		else if ("U".equalsIgnoreCase(mode)) {
			string = string.toUpperCase();
		}
		return string;
	}

	private static String generateString(Object[] array, String delimiter) {
		return generateString(array, delimiter, false);
	}

	private static String generateString(Object[] array, String delimiter, boolean avoidNull) {
		final StringBuilder result = new StringBuilder();
		final int indexToInsert = array.length - 1;
		for (int i = 0; i < array.length; i++) {
			if (avoidNull && array[i] == null) {
				continue;

			}
			if (array[i] == null) {
				array[i] = "";
			}
			result.append(array[i]);
			if (delimiter != null && i < indexToInsert) {
				result.append(delimiter);
			}
		}
		return result.toString();
	}

	private static boolean hasElementOnListObject(mListBuild list, int position) {
		Object elemet = list(list, position);
		return elemet != null && !"".equals(elemet.toString().trim());
	}

	private mFunctionUtil() {
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

		return string.split(Pattern.quote(delimiter)).length;
	}
}
