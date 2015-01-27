package br.com.innovatium.mumps2java.dataaccess.exception;

public class NonUniqueResultException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6239293862261342293L;

	public NonUniqueResultException(String message) {
		super(message);
	}

	public NonUniqueResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
