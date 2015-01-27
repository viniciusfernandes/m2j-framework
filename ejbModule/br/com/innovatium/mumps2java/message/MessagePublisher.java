package br.com.innovatium.mumps2java.message;

import javax.ejb.Local;

@Local
public interface MessagePublisher {
	void newJob(String methodName, Object... parameters);

	void updateMetadataCache(Object[] subs, Object value);
}
