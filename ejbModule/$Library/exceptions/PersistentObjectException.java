package $Library.exceptions;

public class PersistentObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -879643011493470919L;

	public PersistentObjectException() {
	}

	public PersistentObjectException(String message) {
		super(message);
	}

	public PersistentObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistentObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PersistentObjectException(Throwable cause) {
		super(cause);
	}

}
