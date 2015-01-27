package ORM.exceptions;

public class mTransactionManagingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655773854236761696L;

	public mTransactionManagingException(String message) {
		super(message);
	}

	public mTransactionManagingException(String message, Throwable cause) {
		super(message, cause);
	}

}
