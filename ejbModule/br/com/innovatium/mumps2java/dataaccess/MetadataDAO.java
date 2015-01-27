package br.com.innovatium.mumps2java.dataaccess;

import java.util.Map;

import br.com.innovatium.mumps2java.dataaccess.exception.DataAccessException;

public interface MetadataDAO {
	final static String CLASSCDEFTABLE = "WWWCLASSCDEF";
	final static String CLASSMAPPINGTABLE = "WWWCLASSMAPPING";
	final static String GLOBALSAVEMAPTABLE = "WWWGlobalSaveMap";

	Object find(String tableName, String key) throws DataAccessException;

	String findGlobalMapCode(String tableName) throws DataAccessException;

	void insert(String tableName, Object key, Object value)
			throws DataAccessException;

	Map<String, String> like(String tableName, String key)
			throws DataAccessException;

	void remove(String tableName, String key) throws DataAccessException;
}
