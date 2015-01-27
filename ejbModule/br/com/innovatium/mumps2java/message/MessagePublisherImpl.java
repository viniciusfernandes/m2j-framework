package br.com.innovatium.mumps2java.message;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

@Stateless
public class MessagePublisherImpl implements MessagePublisher {

	@Resource(mappedName = "java:/queue/atualizacaocache")
	private Queue atualizacaoMetadadosQueue;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/queue/job")
	private Queue jobQueue;

	@Override
	public void newJob(String methodName, Object... parameters) {

		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session sessao = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			ObjectMessage mensagem = sessao.createObjectMessage(new Object[] { methodName, parameters });
			MessageProducer messageProducer = sessao.createProducer(jobQueue);
			messageProducer.send(mensagem);

		}
		catch (JMSException e) {
			throw new IllegalStateException("Nao foi possivel enviar notificacao para a fila de compra de acoes", e);
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}

			}
			catch (JMSException e) {
				throw new IllegalStateException("Falha ao fechar a conexao JMS", e);
			}
		}

	}

	@Override
	public void updateMetadataCache(Object[] subs, Object value) {

		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session sessao = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Object[] keyValue = { subs, value };

			ObjectMessage mensagem = sessao.createObjectMessage(keyValue);
			MessageProducer messageProducer = sessao.createProducer(atualizacaoMetadadosQueue);
			messageProducer.send(mensagem);

		}
		catch (JMSException e) {
			throw new IllegalStateException("Nao foi possivel enviar notificacao para a fila de compra de acoes", e);
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}

			}
			catch (JMSException e) {
				throw new IllegalStateException("Falha ao fechar a conexao JMS", e);
			}
		}
	}

}
