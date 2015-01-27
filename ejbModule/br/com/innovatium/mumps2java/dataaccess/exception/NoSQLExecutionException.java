package br.com.innovatium.mumps2java.dataaccess.exception;

public class NoSQLExecutionException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7945688003115658258L;

	public NoSQLExecutionException(String message) {
		super(message);
	}

	public NoSQLExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
