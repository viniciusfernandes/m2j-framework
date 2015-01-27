package br.com.innovatium.mumps2java.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import br.com.innovatium.mumps2java.datastructure.GlobalCache;

/*
 @MessageDriven(name = "MetadataCacheChangeListener", activationConfig = {
 @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
 @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/atualizacaocache"),
 @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
 */
public class MetadataCacheChangeListener implements MessageListener {

	private final GlobalCache metadataCache = GlobalCache.getCache();

	public void onMessage(Message rcvMessage) {
		ObjectMessage msg = (ObjectMessage) rcvMessage;
		try {

			Object[] keyValue = (Object[]) msg.getObject();
			metadataCache.set((Object[]) keyValue[0], keyValue[1]);

		}
		catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}