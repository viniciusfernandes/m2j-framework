package br.com.innovatium.mumps2java.dataaccess;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import br.com.innovatium.mumps2java.dataaccess.exception.SQLExecutionException;

@Stateless
public class RelationalDataDAOImpl extends AbstractDAO implements
		RelationalDataDAO {

	public RelationalDataDAOImpl() {
		super(ConnectionType.DATASOURCE_RELATIONAL);
	}

	public String deleteRecord(String tableName, String id)
			throws SQLExecutionException {
		PreparedStatement cmd = null;
		try {
			StringBuilder strcmd = new StringBuilder("delete from ").append(
					tableName).append(" where ID = ?");
			cmd = con.prepareStatement(strcmd.toString());
			// ////System.out
			System.out.println(strcmd);
			// ////
			cmd.setString(1, id);
			cmd.execute();
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to delete table " + tableName
					+ " (ID:" + id + ")", e);
		} finally {
			releaseResouce(cmd);
		}
		return "1";
	}

	public boolean existsRecord(String tableName, String id)
			throws SQLExecutionException {
		boolean exists = false;
		PreparedStatement cmd = null;
		ResultSet result = null;
		try {
			StringBuilder strcmd = new StringBuilder("select 1 from ").append(
					tableName).append(" where ID = ?");
			cmd = con.prepareStatement(strcmd.toString());
			cmd.setString(1, id);
			// ////System.out
			System.out.println(strcmd.append(" [" + id + "]"));
			// ////
			result = cmd.executeQuery();
			while (result.next()) {
				exists = true;
				break;
			}
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to select from table "
					+ tableName + " (ID " + id + ")", e);
		} finally {
			releaseResouce(cmd);
		}
		return exists;
	}

	public Boolean existsRecordPK(String tableName, String[] pk, Object[] values)
			throws SQLExecutionException {
		PreparedStatement cmd = null;
		ResultSet result = null;
		Boolean existsPK = false;
		StringBuilder strcmd = new StringBuilder();
		try {
			strcmd.append("select * from ( select ").append(pk[pk.length - 1])
					.append(" from ").append(tableName).append(" where ");
			for (int i = 0; i < pk.length; i++) {
				if ((i < values.length) && (values[i] != null)) {
					if (i > 0) {
						strcmd.append(" and ");
					}
					strcmd.append(pk[i] + " = '" + values[i] + "'");
				}
			}
			strcmd.append(" ) where rownum <= 1");
			cmd = con.prepareStatement(strcmd.toString());
			result = cmd.executeQuery();
			if (result.next()) {
				existsPK = true;
			}
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to select from table "
					+ tableName + " (PK " + values.toString() + ")", e);
		} finally {
			releaseResouce(cmd);
		}
		return existsPK;
	}

	public String generateInsert(String tableName, Object[] values,
			Object[] columnsMap) {

		StringBuilder columnsInsert = new StringBuilder();
		StringBuilder insertValues = new StringBuilder();
		for (int i = 0; i < columnsMap.length; i++) {

			columnsInsert.append(columnsMap[i]);
			if (values[i] != null) {
				if (values[i] instanceof Timestamp) {
					insertValues.append(resolver
							.formatTimestamp((Date) values[i]));
				} else if (values[i] instanceof Date) {
					insertValues.append(resolver.formatDate((Date) values[i]));
				} else {
					appendDelimitedValue(insertValues, values[i].toString());
				}

			} else {
				insertValues.append("null");
			}

			if (i + 1 < columnsMap.length) {
				columnsInsert.append(", ");
				insertValues.append(", ");
			}
		}

		StringBuilder insert = new StringBuilder("insert into ")
				.append(tableName).append("(").append(columnsInsert)
				.append(")").append(" values (").append(insertValues)
				.append(")");

		// ////System.out
		String inserts = insert.toString();
		System.out.println(inserts);
		// ////
		return inserts;
	}

	public String generateUpdate(String idRecord, String tableName,
			Object[] values, Object[] columnsMap) {

		StringBuilder updateValues = new StringBuilder();
		for (int i = 0; i < columnsMap.length; i++) {
			updateValues.append(columnsMap[i]).append("=");
			if (values[i] != null) {
				if (values[i] instanceof Timestamp) {
					updateValues.append(resolver
							.formatTimestamp((Date) values[i]));
				} else if (values[i] instanceof Date) {
					updateValues.append(resolver.formatDate((Date) values[i]));
				} else {
					appendDelimitedValue(updateValues, values[i].toString());
				}
			} else {
				updateValues.append("null");
			}

			if (i + 1 < columnsMap.length) {
				updateValues.append(", ");
			}
		}

		StringBuilder update = new StringBuilder("update ").append(tableName)
				.append(" set ").append(updateValues);

		update.append(" where ID = '").append(idRecord).append("'");

		String updates = update.toString();
		// ////System.out
		System.out.println(updates);
		// ////
		return updates;
	}

	public String insertRecord(String tableName, String columns, Object[] values)
			throws SQLExecutionException {
		PreparedStatement cmd = null;
		String[] columnsMap = columns.split(",");

		try {
			cmd = con
					.prepareStatement("alter session set NLS_NUMERIC_CHARACTERS = '.,'");
			cmd.execute();
			releaseResouce(cmd);
			/*
			 * String columnsVal = ""; for (int i = 0; i < columnsMap.length;
			 * i++) { columnsVal = columnsVal + (columnsVal.isEmpty() ? "" :
			 * ",") + "?"; }
			 * 
			 * String strcmd = "insert into " + tableName + " (" + columns + ")"
			 * + " values (" + columnsVal + ")";
			 * 
			 * cmd = populateStatement(tableName, values, columnsMap, strcmd);
			 */
			cmd = con.prepareStatement(generateInsert(tableName, values,
					columnsMap));
			cmd.execute();
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to insert into table "
					+ tableName + ". The columns are "
					+ Arrays.deepToString(columnsMap) + " and values are "
					+ Arrays.deepToString(values), e);
		} finally {
			releaseResouce(cmd);
		}

		return "1";
	}

	public void execNativeDDL(String ddl) {
		PreparedStatement cmd = null;
		try {
			cmd = con.prepareStatement(ddl);
			cmd.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cmd != null) {
				try {
					cmd.close();
				} catch (SQLException e) {
					throw new IllegalStateException(
							"Falha ao tentar executar a ddl nativa \"" + ddl
									+ "\"", e);
				}
			}
		}

	}

	public ResultSet loadRecords(String tableName, String id,
			String columnString) throws SQLExecutionException {
		PreparedStatement cmd = null;
		ResultSet result = null;
		StringBuilder strcmd = new StringBuilder();
		try {
			if (id.endsWith("||%")) {
				strcmd.append("select ").append(columnString).append(" from ")
						.append(tableName).append(" where ID like ? or ID = ?");
				cmd = con.prepareStatement(strcmd.toString());
				cmd.setString(1, id);
				cmd.setString(2, id.substring(0, id.length() - 3));
			} else if (id.contains("%")) {
				strcmd.append("select ").append(columnString).append(" from ")
						.append(tableName).append(" where ID like ?");
				cmd = con.prepareStatement(strcmd.toString());
				cmd.setString(1, id);
			} else {
				strcmd.append("select ").append(columnString).append(" from ")
						.append(tableName).append(" where ID = ?");
				cmd = con.prepareStatement(strcmd.toString());
				cmd.setString(1, id);
			}
			// ////System.out
			System.out.println(strcmd.append(" [" + id + "]"));
			// ////
			cmd.setFetchSize(500);
			result = cmd.executeQuery();
			if (!result.next()) {
				cmd.close();
				return null;
			}

		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to select from table "
					+ tableName + " (ID " + id + ")", e);
		}
		return result;
	}

	public String[] searchRecordPK(String tableName, int direction,
			String[] pk, Object[] values) throws SQLExecutionException {
		PreparedStatement cmd = null;
		ResultSet result = null;
		List<String> resultPK = new ArrayList<>();
		StringBuilder strcmd = new StringBuilder();
		try {
			cmd = con
					.prepareStatement("alter session set NLS_SORT = BINARY_CI");
			cmd.execute();
			releaseResouce(cmd);

			strcmd.append("select * from ( select ");
			for (int i = 0; i < pk.length; i++) {
				if ((i >= pk.length - 1) || (i >= values.length)) {
					strcmd.append(pk[i] + ((i < (pk.length - 1)) ? ", " : ""));
				}
			}
			strcmd.append(" from ").append(tableName).append(" where ");
			for (int i = 0; i < pk.length; i++) {
				if ((i < values.length) && (values[i] != null)) {
					if (i > 0) {
						strcmd.append(" and ");
					}
					if (i >= pk.length - 1) {
						if (direction < 0) {
							// strcmd.append(pk[i] + " < '" + values[i] + "'");
							strcmd.append(pk[i]).append(" < '")
									.append(values[i]).append("'");
						} else {
							// strcmd.append(pk[i] + " > '" + values[i] + "'");
							strcmd.append(pk[i]).append(" > '")
									.append(values[i]).append("'");
						}
					} else if ((i > 0) && (i >= values.length - 1)) {
						if (direction < 0) {
							// strcmd.append(pk[i] + " <= '" + values[i] + "'");
							strcmd.append(pk[i]).append(" <= '")
									.append(values[i]).append("'");
						} else {
							// strcmd.append(pk[i] + " >= '" + values[i] + "'");
							strcmd.append(pk[i]).append(" >= '")
									.append(values[i]).append("'");
						}
					} else {
						// strcmd.append(pk[i] + " = '" + values[i] + "'");
						strcmd.append(pk[i]).append(" = '").append(values[i])
								.append("'");
					}
				}
			}
			strcmd.append(" order by ");
			for (int i = 0; i < pk.length; i++) {
				if (i > 0) {
					strcmd.append(", ");
				}
				if (direction < 0) {
					strcmd.append(pk[i]).append(" desc ");
				} else {
					strcmd.append(pk[i]).append(" asc ");
				}
			}
			strcmd.append(" ) where rownum <= 1");

			cmd = con.prepareStatement(strcmd.toString());
			result = cmd.executeQuery();
			if (result.next()) {
				for (int i = 0; i < pk.length; i++) {
					if ((i >= pk.length - 1) || (i >= values.length)) {
						if (i >= values.length) {
							resultPK.add(result
									.getString(i - values.length + 1));
						} else {
							resultPK.add(result.getString(1));
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to select from table "
					+ tableName + " (PK " + values.toString() + ")", e);
		} finally {
			releaseResouce(cmd);
		}

		return resultPK.toArray(new String[] {});
	}

	public String toString(Clob clob) throws SQLExecutionException {
		return super.toString(clob);
	}

	public String updateRecord(String tableName, String id, String columns,
			Object[] values) throws SQLExecutionException {

		PreparedStatement cmd = null;
		String update = null;
		try {
			cmd = con
					.prepareStatement("alter session set NLS_NUMERIC_CHARACTERS = '.,'");
			cmd.execute();
			releaseResouce(cmd);

			String[] columnsMap = columns.split(",");
			/*
			 * 
			 * 
			 * String strcmd = "update " + tableName + " set "; for (int i = 0;
			 * i < columnsMap.length; i++) { strcmd = strcmd + columnsMap[i] +
			 * " = ?" + ((i + 1) < columnsMap.length ? "," : ""); } strcmd =
			 * strcmd + " where ID = ?";
			 * 
			 * // cmd = populateStatement(tableName, values, columnsMap,
			 * strcmd); cmd.setString(columnsMap.length + 1, id);
			 */
			update = generateUpdate(id, tableName, values, columnsMap);
			cmd = con.prepareStatement(update);
			cmd.execute();
		} catch (SQLException e) {
			throw new SQLExecutionException("Fail to update table " + tableName
					+ " (ID:" + id + "). The statement is: " + update, e);
		} finally {
			releaseResouce(cmd);
		}

		return "1";
	}
}
