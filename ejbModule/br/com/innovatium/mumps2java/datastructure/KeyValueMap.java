package br.com.innovatium.mumps2java.datastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class KeyValueMap {

	private final Map<String, Map<String, Node>> keyValue;
	private int majorCapacity = 0;
	private final int MAX_LENGTH = 16;
	
	private int minorCapacity = 0;
	private int size = 0;
	

	public KeyValueMap() {
		this(1000);
	}

	public KeyValueMap(int capacity) {
		minorCapacity = (int)(capacity * 0.1);
		majorCapacity = (int)(capacity * 0.9);
		
		keyValue = new HashMap<>(minorCapacity);
	}

	public Set<Entry<String, Node>> entrySet() {
		Map<String, Node> result = new HashMap<>();

		Set<Entry<String, Map<String, Node>>> entries = new HashSet<>(
				keyValue.entrySet());

		Set<Entry<String, Node>> subEntries = new HashSet<>();
		String key = null;
		for (Entry<String, Map<String, Node>> entry : entries) {
			subEntries = entry.getValue().entrySet();

			for (Entry<String, Node> subEntry : subEntries) {
				key = entry.getKey() + subEntry.getKey();
				result.put(key, subEntry.getValue());
			}
		}
		return result.entrySet();
	}

	public Node get(String key) {
		String begin = key;
		String end = null;

		if (key.length() > MAX_LENGTH) {
			begin = key.substring(0, MAX_LENGTH);
			end = key.substring(MAX_LENGTH, key.length());

		} else {
			end = key;
		}

		Map<String, Node> nodeMap = keyValue.get(begin);
		if (nodeMap != null) {
			return nodeMap.get(end);
		}

		return null;
	}

	public void put(String key, Node node) {
		String begin = key;
		String end = null;

		if (key.length() > MAX_LENGTH) {
			begin = key.substring(0, MAX_LENGTH);
			end = key.substring(MAX_LENGTH, key.length());

		} else {
			end = key;
		}

		Map<String, Node> nodeMap = keyValue.get(begin);
		if (nodeMap == null) {
			nodeMap = new HashMap<>(majorCapacity);
		}

		nodeMap.put(end, node);
		keyValue.put(begin, nodeMap);
		size++;
	}

	public void remove(String key) {
		String begin = key;
		String end = null;

		if (key.length() > MAX_LENGTH) {
			begin = key.substring(0, MAX_LENGTH);
			end = key.substring(MAX_LENGTH, key.length());

		} else {
			end = key;
		}
		Map<String, Node> nodeMap = keyValue.get(begin);
		if (nodeMap != null) {
			nodeMap.remove(end);
			size--;
		}
	}

	public int size() {
		return size;
	}
}
