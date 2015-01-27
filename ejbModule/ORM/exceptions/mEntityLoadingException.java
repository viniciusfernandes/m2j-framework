package ORM.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class mEntityLoadingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8588863518428320231L;

	public mEntityLoadingException(String message) {
		super(message);
	}

	public mEntityLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
