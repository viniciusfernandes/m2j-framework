package br.com.innovatium.mumps2java.datastructure;

import java.util.HashSet;
import java.util.Set;

public class Cache {
	private final QueryCache queryCache = new QueryCache();
	private final Set<String> tableNameCache = new HashSet<String>(50);
	private final Tree tree = new Tree();

	public void addQueried(Object[] subs) {
		this.addQueried(subs, false);
	}

	public void addQueried(Object[] subs, boolean isOrder) {
		this.queryCache.add(subs, isOrder);
	}

	public void addTableName(String tableName) {
		tableNameCache.add(tableName);
	}

	public int data(Object[] subs) {
		return tree.data(subs);
	}

	public String dump() {
		return tree.dump();
	}

	public Object get(Object[] subs) {
		return tree.get(subs);
	}

	public boolean isIndexTable(String tableName) {
		return tableName.endsWith("s") && tableNameCache.contains(tableName.substring(0, tableName.length() - 1));
	}

	public boolean isQueried(Object[] subs) {
		return this.queryCache.isCached(subs);
	}

	public boolean isTableNameCached(String tableName) {
		return tableNameCache.contains(tableName);
	}

	public Node kill(Object[] subs) {
		String tableName = subs[0].toString();
		/*
		 * Implementamos o lock da atualizacao do cache de metadados atraves
		 * nome da global que estamos acessando, ou seja, apenas 1 thread podera
		 * atualizar uma determinada global. Para isso, implementamos um bloco
		 * sincronizado utilizando o nome da tabela que a thread tentara
		 * alterar, e como o nome da tabela devera ser unico no pool de Strings,
		 * recuperamos esse valor do pool para efetuar o lock do block.
		 */
		synchronized (tableName.intern()) {
			tree.kill(subs);
		}

		return null;
	}

	public void merge(final Object[] destSubs, final Object[] origSubs) {
		tree.merge(destSubs, origSubs);
	}

	public Object order(Object[] subs) {
		return order(subs, 1);
	}

	public Object order(Object[] subs, int direction) {
		return tree.order(subs, direction);
	}

	public void set(Object[] subs, Object value) {
		String tableName = subs[0].toString();

		/*
		 * Implementamos o lock da atualizacao do cache de metadados atraves
		 * nome da global que estamos acessando, ou seja, apenas 1 thread podera
		 * atualizar uma determinada global. Para isso, implementamos um bloco
		 * sincronizado utilizando o nome da tabela que a thread tentara
		 * alterar, e como o nome da tabela devera ser unico no pool de Strings,
		 * recuperamos esse valor do pool para efetuar o lock do block.
		 */
		synchronized (tableName.intern()) {
			tree.set(subs, value);
		}
	}

	public void setNew(Object[] subs, Object value) {
		if (!tree.contains(subs)) {
			tree.set(subs, value);
		}
	}
}
