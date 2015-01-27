package br.com.innovatium.mumps2java.dataaccess;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import util.SQLUtil;
import util.exception.DataConvertionException;
import br.com.innovatium.mumps2java.dataaccess.exception.DataAccessException;
import br.com.innovatium.mumps2java.dataaccess.exception.SQLExecutionException;
import br.com.innovatium.mumps2java.datastructure.GlobalCache;
import br.com.innovatium.mumps2java.todo.REVIEW;
import br.com.innovatium.mumps2java.todo.TODO;

@Local
@Stateless
public class SQLMetadataDAOImpl extends AbstractDAO implements MetadataDAO {
	private final int LIMITE = 3500;
	private final GlobalCache metadataCache = GlobalCache.getCache();

	public SQLMetadataDAOImpl() {
		this(ConnectionType.DATASOURCE_METADATA);
	}

	public SQLMetadataDAOImpl(ConnectionType connectionType) {
		super(connectionType);

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(resolver
					.resolve(SQLType.SELECT_TABLE_NAME));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				metadataCache.addTableName(rs.getString(1).toLowerCase());
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Table cache name was not loaded",
					e);
		} finally {
			try {
				releaseResouce(ps);
			} catch (SQLExecutionException e) {
				throw new IllegalStateException(
						"Fail to release resources on relational metadata dao creation",
						e);
			}
		}

	}

	public void createTable(String tableName) throws SQLExecutionException {
		if (hasTable(tableName)) {
			return;
		}

		PreparedStatement ps = null;
		try {
			final StringBuilder selectOne = new StringBuilder("CREATE TABLE ");
			selectOne.append(tableName);
			selectOne.append(" ( KEY_ ")
					.append(resolver.resolve(SQLType.STRING))
					.append(" NOT NULL ,");
			selectOne.append(" VALUE_ ").append(
					resolver.resolve(SQLType.STRING));
			selectOne.append(" , VALUECLOB_ ")
					.append(resolver.resolve(SQLType.LONG_STRING)).append(")");

			ps = con.prepareStatement(selectOne.toString());
			ps.execute();
			metadataCache.addTableName(tableName.toLowerCase());
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Fail to create table " + tableName, e);
		} finally {
			releaseResouce(ps);
		}
	}

	// Remove table name treatment.
	@TODO
	public Object find(String tableName, String key)
			throws SQLExecutionException {
		if (!hasTable(tableName)) {
			// insert("LOGNM", "NOHASTABLE~"+tableName, key);
			return null;
		}
		try {
			return findByKey(tableName, key);
		} catch (DataConvertionException e) {
			throw new SQLExecutionException("Fail to find key: " + key
					+ " of the table: " + tableName, e);
		}
	}

	@Override
	public String findGlobalMapCode(String tableName)
			throws DataAccessException {
		Object code;
		try {
			code = findByKey(GLOBALSAVEMAPTABLE, tableName);
		} catch (DataConvertionException e) {
			throw new DataAccessException(
					"Fail to find global save code of the table: " + tableName,
					e);
		}
		if (code != null) {
			return code.toString();
		}
		return null;
	}

	public boolean hasTable(String tableName) {
		return metadataCache.isTableNameCached(tableName.toLowerCase());
	}

	// Remove table name treatment.
	@TODO
	public void insert(String tableName, Object key, Object value)
			throws SQLExecutionException {
		if (!hasTable(tableName)) {
			createTable(tableName);
		}

		PreparedStatement ps = null;
		ResultSet result = null;

		try {

			String selectOne = "select key_, value_, valueCLOB_  from "
					+ tableName + " where key_ = ?";
			ps = con.prepareStatement(selectOne);
			ps.setObject(1, key);
			result = ps.executeQuery();

			if (!result.next()) {
				releaseResouce(ps);
				String insertQuery = "insert into " + tableName
						+ " values (?, ?, ?)";

				ps = con.prepareStatement(insertQuery);
				// ////System.out
				System.out.println(insertQuery);
				// ////
				ps.setString(1, String.valueOf(key));

				if (value instanceof String
						&& value.toString().length() > LIMITE) {
					ps.setObject(2, null);
					ps.setObject(3, value);
				} else {
					ps.setObject(2, value);
					ps.setObject(3, null);
				}

			} else {
				releaseResouce(ps);
				String updateQuery = "update " + tableName
						+ " set value_ = ?, valueCLOB_ = ? where key_ = ?";
				ps = con.prepareStatement(updateQuery);
				// ////System.out
				System.out.println(updateQuery);
				// ////

				if (value instanceof String
						&& value.toString().length() > LIMITE) {
					ps.setObject(1, null);
					ps.setObject(2, value);
				} else {
					ps.setObject(1, value);
					ps.setObject(2, null);
				}
				ps.setObject(3, String.valueOf(key));
			}

			ps.execute();

		} catch (SQLException e) {

			throw new IllegalStateException("Fail to insert data into table "
					+ tableName + " and key " + key, e);
		} finally {
			releaseResouce(ps);
		}
	}

	// Check another return type different from the map.
	@TODO
	@REVIEW(author = "vinicius", date = "27/08/2014", description = "Remover o insert das tabelas nao encontradas LOGNM")
	public Map<String, String> like(String tableName, String key)
			throws SQLExecutionException {
		if (!hasTable(tableName)) {
			// insert("LOGNM", "NOHASTABLE~"+tableName, key);
			return null;
		}

		if (key == null) {
			return null;
		}
		Map<String, String> map = null;
		PreparedStatement select = null;
		ResultSet result = null;
		try {

			String like = null;
			if (key.endsWith("~")) {
				like = "select key_, value_, valueCLOB_  from " + tableName
						+ " where key_ like ? or key_ = ? order by key_ asc";
				select = con.prepareStatement(like);
				select.setString(1, ((key == " ") ? "" : key) + "%");
				select.setString(2, key.substring(0, key.length() - 1));
			} else {
				like = "select key_, value_, valueCLOB_  from " + tableName
						+ " where key_ like ? order by key_ asc";
				select = con.prepareStatement(like);
				select.setString(1, ((key == " ") ? "" : key) + "%");
			}

			// ////System.out
			System.out.println(like + " [" + ((key == " ") ? "" : key) + "%]");
			// ////
			select.setFetchSize(500);
			result = select.executeQuery();
			map = new HashMap<String, String>();
			while (result.next()) {

				map.put(result.getString(1),
						getValueCLOB(result.getString(2), result.getClob(3)));
			}

		} catch (SQLException e) {
			throw new IllegalStateException(
					"Fail to find data thought like clause from table "
							+ tableName + " and key " + key, e);
		} catch (DataConvertionException e) {
			throw new SQLExecutionException("Fail to convert clob to string", e);
		} finally {
			try {
				releaseResouce(select);
			} catch (SQLExecutionException e) {
				throw new SQLExecutionException(
						"Fail to close prepare select statement of the like clause",
						e);
			}
		}
		return map;
	}

	public void remove(String tableName, String key)
			throws SQLExecutionException {
		if (!hasTable(tableName)) {
			return;
		}

		PreparedStatement delete = null;
		try {

			String string = "delete from " + tableName + " where key_ like ?";
			delete = con.prepareStatement(string);
			// ////System.out
			System.out.println(string);
			// ////
			delete.setString(1, key + "%");
			delete.execute();

		} catch (SQLException e) {
			throw new IllegalStateException("Fail to remove data from table "
					+ tableName + " and key " + key, e);
		} finally {
			releaseResouce(delete);
		}
	}

	private Object findByKey(String tableName, String key)
			throws DataConvertionException, SQLExecutionException {
		Object objResult = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {

			final StringBuilder selectOne = new StringBuilder(
					"select value_, valueCLOB_ from ").append(tableName)
					.append(" where key_ = ?");

			ps = con.prepareStatement(selectOne.toString());
			// ////System.out
			System.out.println(selectOne.toString() + " [" + key + "]");
			// ////
			ps.setString(1, key);
			result = ps.executeQuery();

			objResult = result.next() ? getValueCLOB(result.getString(1),
					result.getClob(2)) : null;
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Fail to select data from the table: " + tableName
							+ " and key: " + key, e);
		} finally {
			releaseResouce(ps);
		}
		return objResult;

	}

	private String getValueCLOB(String value, Clob valueCLOB)
			throws DataConvertionException {

		// A primeira condicao verifica se os dados sao do tipo varchar, caso
		// contrario vamos assumir que eles sao do tipo CLOB.
		if (value != null && valueCLOB == null) {
			return value;
		} else if (value == null && valueCLOB != null) {
			return SQLUtil.CLOBtoString(valueCLOB);
		} else {
			return "";
		}
	}
}
