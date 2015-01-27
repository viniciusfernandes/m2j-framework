package br.com.innovatium.mumps2java.dataaccess.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class DataAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6082802915265833360L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

}
