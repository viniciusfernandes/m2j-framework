package util;

public final class DataStructureUtil {
	public final static String DELIMITER = "~";
	public static final int GLOBAL = 2;
	public static final int LOCAL = 3;
	public static final int PUBLIC = 1;

	@Deprecated
	public static void appendStringValueWithSQLConcat(final StringBuilder statement, final int maxSize,
			final String value, final String delimiter) {
		if (value == null) {
			statement.append("null");
			return;
		}

		int length = value.length();
		if (length <= maxSize) {
			statement.append("'").append(value).append("'");
			return;
		}

		statement.append(generateStringValueWithSQLConcat(maxSize, value, delimiter));
	}

	public static Object[] concat(Object[] dest, Object orig) {
		return concat(dest, new Object[] { orig });
	}

	public static Object[] concat(Object[] first, Object[] second) {
		return concat(first, second, first.length, 0);
	}

	public static Object[] concatSinceLast(Object[] first, Object[] second) {
		return concat(first, second, first.length - 1, 0);
	}

	public static String generateKey(Object[] subs) {
		return generateKey(0, subs.length, false, subs);
	}

	public static String generateKeyOfParent(Object[] subs) {
		return generateKey(0, subs.length - 1, false, subs);
	}

	public static String generateKeyToLikeQuery(Object[] subs) {
		// Like query must remove the first subscript which means the table name
		// and remove the last subscript to fetch all children of that key.
		return generateKey(1, subs.length - 1, true, subs);
	}

	public static String generateKeyToLikeQuery(Object[] subs, int dec) {
		// Like query must remove the first subscript which means the table name
		// and remove the last subscript to fetch all children of that key.
		return generateKey(1, subs.length - dec, true, subs);
	}

	public static String generateKeyWithoutVarName(Object[] subs) {
		return generateKey(1, subs.length, false, subs);
	}

	/*
	 * Esse metodo foi criado para contornar um problema de insercao de string >
	 * 4000 no oracle e sera removido adiante.
	 */
	@Deprecated
	public static String generateStringValueWithSQLConcat(final int maxSize, final String value, final String delimiter) {
		if (value == null) {
			return value;
		}

		int length = value.length();
		if (length <= maxSize) {
			return value;
		}
		StringBuilder string = new StringBuilder();

		int total = length / maxSize;
		int init = -1;
		int end = -1;
		string.append("to_clob('')||");
		for (int i = 0; i < total; i++) {
			init = i * maxSize;
			end = (i + 1) * maxSize;
			string.append("'").append(value.substring(init, end)).append("'").append(delimiter);
		}

		// Estamos concatenando a ultima porcao da String
		string.append("'").append(value.substring(total * maxSize)).append("'");
		return string.toString();
	}

	public static Object[] generateSubs(String key) {
		if (key == null) {
			return null;
		}
		return key.split(DELIMITER);
	}

	public static Object[] generateSubs(String tableName, String key) {
		return generateSubs(new StringBuilder("^").append(tableName).append(DELIMITER).append(key).toString());
	}

	public static String generateTableName(Object... subs) {
		return subs[0].toString().replace("^", "");
	}

	public static int getVariableType(Object[] subs) {
		return getVariableType(subs[0].toString());
	}

	/*
	 * 1. scan the string from left to right until the first character that does
	 * not "make sense" as a number is found 3. check a single minus sign at the
	 * begin 5. check leading plus sign 7. check leading zeros 8. check trailing
	 * fractional zeros
	 */
	// val deve ser um string com ao menos um caracter, conforme MUMPS
	// subscripts
	public static boolean isCanonicalNumeric(String val) {
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

	public static boolean isGlobal(Object[] subs) {
		return GLOBAL == getVariableType(subs);
	}

	public static boolean isGlobal(String name) {
		return GLOBAL == getVariableType(name);
	}

	public static boolean isLocal(Object[] subs) {
		return LOCAL == getVariableType(subs);
	}

	public static boolean isLocal(String name) {
		return LOCAL == getVariableType(name);
	}

	public static boolean isPublic(Object[] subs) {
		return PUBLIC == getVariableType(subs);
	}

	public static boolean isPublic(String name) {
		return PUBLIC == getVariableType(name);
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

	private static Object[] concat(Object[] first, Object[] second, int lastIndexOfFirst, int startIndexOfSecond) {
		Object[] copy = new Object[lastIndexOfFirst + second.length];
		for (int i = 0; i < lastIndexOfFirst; i++) {
			copy[i] = first[i];
		}

		for (int i = startIndexOfSecond; i < second.length; i++) {
			copy[i + lastIndexOfFirst] = second[i];
		}
		return copy;
	}

	private static String generateKey(int start, int end, boolean like, Object... subs) {
		if (subs == null || subs.length <= start) {
			return " ";
		}

		final StringBuilder builder = new StringBuilder();

		for (int i = start; i < end; i++) {
			if (i > start) {
				builder.append(DELIMITER);
			}
			builder.append(mFunctionUtil.toString(subs[i]));
		}

		if (like) {
			builder.append(DELIMITER);
		}

		return builder.toString();
	}

	private static int getVariableType(String name) {
		final boolean isEmpty = name.length() == 0;
		if (!isEmpty && '%' == name.charAt(0)) {
			return PUBLIC;
		}
		else if (!isEmpty && '^' == name.charAt(0)) {
			return GLOBAL;
		}
		else {
			return LOCAL;
		}
	}

	private DataStructureUtil() {
	}

}
