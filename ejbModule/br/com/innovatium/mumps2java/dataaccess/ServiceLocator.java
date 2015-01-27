package br.com.innovatium.mumps2java.dataaccess;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceLocator {
	private static InitialContext context;
	private static Properties properties;

	static {
		properties = new Properties();
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		try {
			context = new InitialContext(properties);
		} catch (NamingException e) {
			throw new IllegalStateException(
					"Falha na inicializacao do service locator. Nao foi possivel inicial o contexto para efetuar os lookups dos recursos.",
					e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T locate(Class<T> classe) throws ServiceLocatorException {

		StringBuilder serviceName = new StringBuilder();

		serviceName.append("java:global/netmanager/")
				.append(classe.getSimpleName()).append("!")
				.append(classe.getInterfaces()[0].getName());
		try {
			return (T) context.lookup(serviceName.toString());
		} catch (NamingException e) {
			throw new ServiceLocatorException(
					"Falha na localizacao do servico: " + serviceName, e);
		}
	}
}
