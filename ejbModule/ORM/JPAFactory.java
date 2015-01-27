package ORM;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.dynamic.DynamicClassLoader;

final class JPAFactory {
	public static EntityManagerFactory createJPAFactory(String persistenteUnit) {
		DynamicClassLoader dcl = new DynamicClassLoader(
				JPAFactory.class.getClassLoader());
		Map<Object, Object> properties = new HashMap<Object, Object>();
		properties.put(PersistenceUnitProperties.CLASSLOADER, dcl);
		properties.put(PersistenceUnitProperties.WEAVING, "static");
		return Persistence.createEntityManagerFactory(persistenteUnit,
				properties);
	}

	public static EntityManager createJPAManager(String persistenteUnit) {
		return createJPAFactory(persistenteUnit).createEntityManager();
	}

	private JPAFactory() {
	}
}
