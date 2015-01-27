package br.com.innovatium.mumps2java.datastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class EnhancedMap {
	private final Map<String, Object> keyValue;
	private final int MAP_KEY_LENGTH = 16;
	private final int capacity;

	public EnhancedMap(int capacity) {
		this.capacity = capacity;
		this.keyValue = new HashMap<String, Object>(capacity);
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findMap(String key) {
		Map<String, Object> KV = this.keyValue;
		String keyConcat = null;
		while (key.length() > MAP_KEY_LENGTH) {
			keyConcat = key.substring(0, MAP_KEY_LENGTH) + "_";
			Map<String, Object> nextKV = (Map<String, Object>) KV.get(keyConcat);
			if (nextKV == null) {
				nextKV = new HashMap<String, Object>(capacity);
				KV.put(keyConcat, nextKV);
			}
			key = key.substring(0, MAP_KEY_LENGTH);
			KV = nextKV;
		}
		return KV;
	}

	public Object get(String key) {
		if (key == null) {
			return null;
		}
		return findMap(key).get(key);
	}

	/*
	 * Metodo criado para execucao de testes unitarios
	 */
	Set<String> getFirstLevelKeys() {
		return keyValue.keySet();
	}

	public void put(String key, Object value) {
		if (key == null) {
			return;
		}
		findMap(key).put(key, value);
	}

	public void remove(String key) {
		if (key == null) {
			return;
		}
		findMap(key).remove(key);
	}
}
