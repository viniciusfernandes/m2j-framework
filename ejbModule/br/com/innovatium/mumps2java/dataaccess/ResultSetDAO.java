package br.com.innovatium.mumps2java.dataaccess;

import java.sql.PreparedStatement;

import javax.ejb.Local;

@Local
public interface ResultSetDAO {
	PreparedStatement createPreparedStatement(String sql);

	String getDatabaseType();
}
