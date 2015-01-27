package br.com.innovatium.mumps2java.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class ConnectionFactory {
	private static InitialContext context;
	private static Properties properties;

	static {
		properties = new Properties();
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		try {
			context = new InitialContext(properties);
		} catch (NamingException e) {
			throw new IllegalStateException(
					"Fail to initialize de contexto to do lookups of the datasource.",
					e);
		}
	}

	public static Connection getConnection(ConnectionType type)
			throws SQLException {

		if (context == null) {
			try {
				context = new InitialContext();
			} catch (NamingException e) {
				throw new IllegalStateException(
						"The context to looking for data sources was not initialized");
			}
		}

		if (ConnectionType.JDBC.equals(type)) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return DriverManager.getConnection(
					"jdbc:oracle:thin:@mokona:1521:ora11db1", "metauser",
					"metauser");

		} else if (ConnectionType.DATASOURCE_METADATA.equals(type)) {

			try {
				return ((DataSource) ConnectionFactory.context
						.lookup("java:jboss/datasources/metadata"))
						.getConnection();
			} catch (NamingException e) {
				throw new IllegalStateException(
						"Fail on look up datasource to open a new conection.",
						e);
			}
		} else {

			try {
				return ((DataSource) ConnectionFactory.context
						.lookup("java:jboss/datasources/relational"))
						.getConnection();
			} catch (NamingException e) {
				throw new IllegalStateException(
						"Fail on look up datasource to open a new conection.",
						e);
			}
		}

	}

	private ConnectionFactory() {

	}
}