package br.com.innovatium.mumps2java.dataaccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ejb.Stateless;

@Stateless
public class ResultSetDAOImpl extends AbstractDAO implements ResultSetDAO {

	private PreparedStatement ps;

	public ResultSetDAOImpl() {
		this(ConnectionType.DATASOURCE_RELATIONAL);
	}

	public ResultSetDAOImpl(ConnectionType connectionType) {
		super(connectionType);
	}

	public PreparedStatement createPreparedStatement(String sql) {
		try {
			closeResouce(ps);
			return ps = con.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDatabaseType(){
		return resolver.getType().toString();
	}
	
	private void closeResouce(PreparedStatement ps) {
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Fail to close prepare statement",
					e);
		}
	}

}
