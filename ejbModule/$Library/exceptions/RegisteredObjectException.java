package $Library.exceptions;

public class RegisteredObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6881688307133972911L;

	public RegisteredObjectException() {
	}

	public RegisteredObjectException(String message) {
		super(message);
	}

	public RegisteredObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegisteredObjectException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RegisteredObjectException(Throwable cause) {
		super(cause);
	}

}
