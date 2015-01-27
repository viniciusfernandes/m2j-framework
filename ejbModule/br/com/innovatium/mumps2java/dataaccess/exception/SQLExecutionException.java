package br.com.innovatium.mumps2java.dataaccess.exception;

import javax.ejb.ApplicationException;

@ApplicationException(inherited = true)
public class SQLExecutionException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6411215259586116124L;

	public SQLExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLExecutionException(Throwable cause) {
		super(cause);
	}

}
