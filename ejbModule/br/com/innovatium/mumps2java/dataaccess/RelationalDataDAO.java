package br.com.innovatium.mumps2java.dataaccess;

import java.sql.Clob;
import java.sql.ResultSet;

import javax.ejb.Local;

import br.com.innovatium.mumps2java.dataaccess.exception.SQLExecutionException;

@Local
public interface RelationalDataDAO {

	String deleteRecord(String tableName, String id)
			throws SQLExecutionException;

	void execNativeDDL(String ddl);

	boolean existsRecord(String tableName, String id)
			throws SQLExecutionException;

	public Boolean existsRecordPK(String tableName, String[] pk, Object[] values)
			throws SQLExecutionException;

	String insertRecord(String tableName, String columns, Object[] values)
			throws SQLExecutionException;
     
	ResultSet loadRecords(String tableName, String id, String columnString)
			throws SQLExecutionException;

	public String[] searchRecordPK(String tableName, int direction,
			String[] pk, Object[] values) throws SQLExecutionException;

	String toString(Clob clob) throws SQLExecutionException;

	String updateRecord(String tableName, String id, String columns,
			Object[] values) throws SQLExecutionException;
}
