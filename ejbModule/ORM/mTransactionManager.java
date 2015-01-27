package ORM;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ORM.exceptions.mTransactionManagingException;

public class mTransactionManager {
	private final Context initialContext;
	// Aqui estao listados os status da transacao
	// STATUS_ACTIVE 0
	// STATUS_COMMITTED 3
	// STATUS_COMMITTING 8
	// STATUS_MARKED_ROLLBACK 1
	// STATUS_NO_TRANSACTION 6
	// STATUS_PREPARED 2
	// STATUS_PREPARING 7
	// STATUS_ROLLEDBACK 4
	// STATUS_ROLLING_BACK 9
	// STATUS_UNKNOWN 5
	private final UserTransaction tx;

	public mTransactionManager() {
		try {
			initialContext = new InitialContext();
		} catch (NamingException e) {
			throw new mTransactionManagingException("Fail to initialize context to lookup transaction service", e);
		}

		try {
			tx = (UserTransaction) initialContext.lookup("java:comp/UserTransaction");
		} catch (NamingException e) {
			throw new mTransactionManagingException("Fail to lookup transaction service", e);
		}
	}

	public void commit() {
		if (!isAbleToCommit()) {
			return;
		}
		try {
			tx.commit();
		} catch (Exception e) {
			throw new mTransactionManagingException("Fail to commit the transaction", e);
		}
	}

	private boolean isAbleToCommit() {
		try {
			return tx != null && tx.getStatus() == 0;
		} catch (SystemException e) {
			return false;
		}
	}

	private boolean isAbleToRollback() {
		try {
			return tx != null && (tx.getStatus() == 1 || tx.getStatus() != 6);
		} catch (SystemException e) {
			return false;
		}
	}

	private boolean isAbleToStart() {
		try {
			return tx != null && (tx.getStatus() == 6 || tx.getStatus() == 1);
		} catch (SystemException e) {
			return false;
		}
	}

	private boolean isOpened() {
		try {
			return tx != null && tx.getStatus() == 0;
		} catch (SystemException e) {
			return false;
		}
	}

	public void rollback() {
		if (!isAbleToRollback()) {
			return;
		}
		try {
			tx.rollback();
		} catch (Exception e) {
			throw new mTransactionManagingException("Fail to rollback the transaction", e);
		}
	}

	public void start() {

		if (isOpened()) {
			return;
		}
		try {
			if (isAbleToStart()) {
				tx.begin();
			}
		} catch (NotSupportedException | SystemException e) {
			throw new mTransactionManagingException("Fail to start the transaction", e);
		}
	}
}
