package br.com.innovatium.mumps2java.dataaccess;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.DataStructureUtil;
import util.SQLUtil;
import util.exception.DataConvertionException;
import br.com.innovatium.mumps2java.dataaccess.exception.SQLExecutionException;

public class AbstractDAO {
	final Connection con;

	SQLResolver resolver = null;

	public AbstractDAO() {
		this(ConnectionType.DATASOURCE_RELATIONAL);
	}

	public AbstractDAO(ConnectionType connectionType) {
		try {
			con = ConnectionFactory.getConnection(connectionType);
			resolver = SQLResolver.getResolver(con.getMetaData().getDatabaseProductName());
		}
		catch (SQLException e) {
			throw new IllegalStateException("Fail to open connection to database access throught " + connectionType
					+ " strategy", e);
		}
	}

	void appendDelimitedValue(StringBuilder statement, String value) {

		// Devemos previnir que exista uma aspas simples no meio de uma String
		// contendo descrioes, etc.
		DataStructureUtil.appendStringValueWithSQLConcat(statement, SQLUtil.STRING_MAXLENGTH, value.replace("'", "''"),
				"||");
	}

	void releaseResouce(PreparedStatement ps) throws SQLExecutionException {
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		catch (SQLException e) {
			throw new SQLExecutionException("Fail to close prepare statement", e);
		}
	}

	String toString(Clob clob) throws SQLExecutionException {
		try {
			return SQLUtil.CLOBtoString(clob);
		}
		catch (DataConvertionException e) {
			throw new SQLExecutionException(e);
		}
	}
}
