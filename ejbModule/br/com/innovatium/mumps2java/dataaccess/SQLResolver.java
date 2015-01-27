package br.com.innovatium.mumps2java.dataaccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.mFunctionUtil;

final class SQLResolver {
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static final DateFormat dateTimeFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");
	private static final DateFormat timestampFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");
	public static SQLResolver getResolver(SQLResolverType type) {
		return new SQLResolver(type);
	}
	public static SQLResolver getResolver(String description) {
		return getResolver(SQLResolverType.getType(description));
	}

	private Map<SQLType, String> sql = new HashMap<SQLType, String>();

	private final SQLResolverType type;

	private SQLResolver(SQLResolverType type) {
		if (SQLResolverType.POSTGRE.equals(type)) {
			initPostgreSQL();
		} else if (SQLResolverType.ORACLE.equals(type)) {
			initOracleSQL();
		} else if (SQLResolverType.MYSQL.equals(type)) {
			initMySQL();
		} else {
			throw new IllegalArgumentException(
					"This database instruction was not implemented.");
		}
		dateFormat.setTimeZone(mFunctionUtil.getTimeZone());
		dateTimeFormat.setTimeZone(mFunctionUtil.getTimeZone());
		timestampFormat.setTimeZone(mFunctionUtil.getTimeZone());
		this.type = type;
	}

	public String formatDate(Date date) {
		if (date == null) {
			return null;
		}

		if (SQLResolverType.ORACLE.equals(type)) {
			return sql.get(SQLType.TO_DATETIME).replaceAll("X",
					dateTimeFormat.format(date));
		}
		throw new IllegalArgumentException();
	}

	public String formatTimestamp(Date date) {
		if (date == null) {
			return null;
		}

		if (SQLResolverType.ORACLE.equals(type)) {
			return sql.get(SQLType.TO_TIMESTAMP).replaceAll("X",
					timestampFormat.format(date));
		}
		throw new IllegalArgumentException();
	}

	public SQLResolverType getType() {
		return type;
	}

	public String resolve(SQLType type) {
		return sql.get(type);
	}

	private void initMySQL() {
		sql = new HashMap<SQLType, String>();
		sql.put(SQLType.STRING, "varchar(4000)");
		sql.put(SQLType.LONG_STRING, "varchar");
		sql.put(SQLType.DROP_TABLE, "drop table");
		sql.put(SQLType.CREATE_TABLE, "create table");
		sql.put(SQLType.SELECT_TABLE_NAME, "SHOW TABLES from metauser");
	}

	private void initOracleSQL() {
		sql = new HashMap<SQLType, String>();
		sql.put(SQLType.STRING, "varchar2(4000)");
		sql.put(SQLType.LONG_STRING, "clob");
		sql.put(SQLType.DROP_TABLE, "drop table");
		sql.put(SQLType.CREATE_TABLE, "create table");
		sql.put(SQLType.SELECT_TABLE_NAME, "select table_name from ALL_TABLES WHERE OWNER = 'METAUSER'");
		sql.put(SQLType.TO_DATE, "TO_DATE('X', 'DD/MM/YYYY')");
		sql.put(SQLType.TO_DATETIME, "TO_DATE('X', 'DD/MM/YYYY HH24:MI:SS')");
		sql.put(SQLType.TO_TIMESTAMP, "TO_DATE('X', 'DD/MM/YYYY HH24:MI:SS')");

	}

	private void initPostgreSQL() {
		sql = new HashMap<SQLType, String>();
		sql.put(SQLType.STRING, "varchar(5000)");
		sql.put(SQLType.LONG_STRING, "varchar");
		sql.put(SQLType.DROP_TABLE, "drop table");
		sql.put(SQLType.CREATE_TABLE, "create table");
		sql.put(SQLType.SELECT_TABLE_NAME,
				"SELECT tablename FROM pg_tables where schemaname = 'public' ");
	}
}