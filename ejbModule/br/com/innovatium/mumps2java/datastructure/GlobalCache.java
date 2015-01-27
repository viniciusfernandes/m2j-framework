package br.com.innovatium.mumps2java.datastructure;

public final class GlobalCache extends Cache {
	private static final GlobalCache cache;

	static {
		cache = new GlobalCache();
	}

	public static GlobalCache getCache() {
		return cache;
	}

	private GlobalCache() {
	}
}
