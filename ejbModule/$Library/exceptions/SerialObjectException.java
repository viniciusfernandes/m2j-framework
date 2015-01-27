package $Library.exceptions;

public class SerialObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5219573303617562006L;

	public SerialObjectException() {
	}

	public SerialObjectException(String message) {
		super(message);
	}

	public SerialObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerialObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SerialObjectException(Throwable cause) {
		super(cause);
	}

}
