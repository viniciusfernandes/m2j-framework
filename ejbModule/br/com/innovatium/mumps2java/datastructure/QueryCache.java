package br.com.innovatium.mumps2java.datastructure;

import static util.DataStructureUtil.generateKey;
import static util.DataStructureUtil.generateKeyOfParent;

import java.util.Arrays;

public class QueryCache {
	final EnhancedMap cache = new EnhancedMap(2048);

	public QueryCache() {
	}

	public void add(Object[] subs) {
		this.add(subs, false);
	}

	public void add(Object[] subs, boolean isOrder) {
		if (isOrder) {
			cache.put(generateKeyOfParent(subs), true);
		} else {
			cache.put(generateKey(subs), true);
		}
	}

	public boolean isCached(Object[] subs) {
		int last = subs.length;
		Object[] subscripts = null;
		if (subs.length == 1) {
			return cache.contains(subs[0].toString());
		}
		if (cache.contains(generateKey(subs))) {
			return true;
		}
		for (int i = last; i > 1; i--) {
			subscripts = Arrays.copyOf(subs, i);
			if (cache.contains(generateKeyOfParent(subscripts))) {
				return true;
			}
		}
		return false;
	}
}
