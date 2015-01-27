package br.com.innovatium.mumps2java.message;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import mLibrary.mContext;

@MessageDriven(name = "JobListener", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/job"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class JobListener implements MessageListener {

	public void onMessage(Message rcvMessage) {
		ObjectMessage msg = (ObjectMessage) rcvMessage;
		mContext m$ = null;
		try {

			m$ = mContext.createComAcessoABancoSQL();
			Object[] args = (Object[]) msg.getObject();
			String methodName = (String) args[0];
			Object[] parameters = null;
			if (args.length > 1) {
				parameters = (Object[]) args[1];
			}

			m$.dispatch(false, true, null, methodName, parameters);
		}
		catch (JMSException e) {
			if (m$ != null) {
				m$.TRollback();
			}
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}