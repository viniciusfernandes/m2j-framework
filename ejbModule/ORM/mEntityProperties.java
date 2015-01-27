package ORM;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

class mEntityProperties {
	private static final mEntityProperties properties = new mEntityProperties();

	static mEntityProperties getInstance() {
		return properties;
	}

	private final Map<String, Set<mClassMappingProp>> fkCache = new LinkedHashMap<>(100);

	/*
	 * Estamos usando linkedHashMap pois a ordem de inclusao sera muito importante
	 * no momento em que formos construir a associacao entre as dependecias das
	 * FKs, pois la copiamos os valores das FKs da classe referenciada para a
	 * classe que a referencia. Veja o metodo mEntity.save()
	 */
	private final Map<String, Set<mClassMappingProp>> pkCache = new LinkedHashMap<>(100);

	private final Map<String, Map<String, mClassMappingProp>> propertiesCache = new LinkedHashMap<>(100);

	private mEntityProperties() {
	}

	/*
	 * 
	 * Esse metodo nao precisa ser performatico pois sera executado apenas uma vez
	 * para cada entidade, por isso concatenamos varias chamadas via get() e
	 * add().
	 */
	public void add(String entityName, mClassMappingProp prop) {
		/*
		 * Estamos usando linkedHashMap pois a ordem de inclusao sera muito
		 * importante no momento em que formos construir a associacao entre as
		 * dependecias das FKs, pois la copiamos os valores das FKs da classe
		 * referenciada para a classe que a referencia. Veja o metodo mEntity.save()
		 */
		if (!propertiesCache.containsKey(entityName)) {
			// Aqui temos um treemap para refletir a ordenacao das properties quando
			// realizarmos o toString no mEntity.
			propertiesCache.put(entityName, new TreeMap<String, mClassMappingProp>());
			pkCache.put(entityName, new LinkedHashSet<mClassMappingProp>());
			fkCache.put(entityName, new LinkedHashSet<mClassMappingProp>());
		}

		String propName = prop.getPropertyName();

		propertiesCache.get(entityName).put(propName, prop);
		if (prop.isPropertyPK()) {
			pkCache.get(entityName).add(prop);
		}

		if (prop.isPropertyFK()) {
			fkCache.get(entityName).add(prop);
		}

	}

	public List<mClassMappingProp> getFKList(String entityName) {
		return new ArrayList<>(fkCache.get(entityName));
	}

	public List<mClassMappingProp> getPKList(String entityName) {
		return new ArrayList<>(pkCache.get(entityName));

	}

	public List<mClassMappingProp> getProperties(String entityName) {
		return new ArrayList<>(propertiesCache.get(entityName).values());
	}

	public mClassMappingProp getProperty(String entityName, String propertyName) {
		Map<String, mClassMappingProp> cache = propertiesCache.get(entityName);
		if (cache == null) {
			return null;
		}
		return cache.get(propertyName);
	}

	public Class<?> getPropertyType(String entityName, String propertyName) {
		Map<String, mClassMappingProp> cache = propertiesCache.get(entityName);
		if (cache == null) {
			return null;
		}
		mClassMappingProp map = cache.get(propertyName); 
		if (map == null) {
			return null;
		}
		return map.getType();
	}
}
