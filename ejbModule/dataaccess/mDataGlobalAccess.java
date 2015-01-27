package dataaccess;

import static br.com.innovatium.mumps2java.dataaccess.MetadataDAO.CLASSCDEFTABLE;
import static br.com.innovatium.mumps2java.dataaccess.MetadataDAO.CLASSMAPPINGTABLE;
import static br.com.innovatium.mumps2java.dataaccess.MetadataDAO.GLOBALSAVEMAPTABLE;
import static util.DataStructureUtil.generateKeyToLikeQuery;
import static util.DataStructureUtil.generateKeyWithoutVarName;
import static util.DataStructureUtil.generateSubs;
import static util.DataStructureUtil.generateTableName;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.DataStructureUtil;
import ORM.mJDBCObject;
import br.com.innovatium.mumps2java.dataaccess.MetadataDAO;
import br.com.innovatium.mumps2java.dataaccess.exception.DataAccessException;
import br.com.innovatium.mumps2java.datastructure.Cache;
import br.com.innovatium.mumps2java.datastructure.GlobalCache;
import br.com.innovatium.mumps2java.todo.REVIEW;

public class mDataGlobalAccess extends mDataAccess {
	/**
	 * 
	 */
	private static final long serialVersionUID = -805878250684438112L;

	private final MetadataDAO dao;

	private final mJDBCObject jdbcObj = new mJDBCObject();

	private final Cache cache;

	public mDataGlobalAccess(Cache cache, mVariables mVariables, MetadataDAO dao) {
		super(mVariables, DataStructureUtil.GLOBAL);
		this.cache = cache;
		this.dao = dao;
	}

	public mDataGlobalAccess(mVariables mVariables, MetadataDAO dao) {
		this(GlobalCache.getCache(), mVariables, dao);
	}

	public mDataGlobalAccess(boolean useCache, mVariables mVariables, MetadataDAO dao) {
		// No caso em que tenhamos useCache == true o acesso a dados nao
		// apontara para o cache compartilhado por toda a aplicacao, mas para
		// apenas uma estrutura de cache temporaria. Isso foi necessario para
		// implementarmos a estrategia de comparacao entre os mapeamentos das
		// globais que estavam em memoria com os mapeamentos das classes que
		// estao no banco de dados.
		this(useCache ? GlobalCache.getCache() : new Cache(), mVariables, dao);
	}

	public int data(Object... subs) {
		currentSubs = subs;
		fillCache(false);
		return cache.data(subs);
	}

	private void deleteCacheData(Object[] subs) {
		cache.kill(subs);
	}

	private void deleteKeyValueData(String tableName, String key) {
		try {
			dao.remove(tableName, key);
		} catch (DataAccessException e) {
			throw new IllegalStateException("Fail to delete key: " + key + " of the table: " + tableName, e);
		}
	}

	private void deleteRelationalData(String code, Object[] subs, String tableName, String key) {

		int length = subs.length - ("4".equals(code) ? 0 : 1);

		String globalRefID = "";
		for (int i = 1; i < length; i++) {
			globalRefID = (globalRefID.isEmpty() ? "" : globalRefID + "||") + util.mFunctionUtil.toString(subs[i]);
		}
		jdbcObj.deleteRecord(this, tableName, globalRefID);
	}

	@Override
	public String dump() {
		return cache.dump();
	}

	private void fillCache(boolean isOrder) {
		if (!cache.isQueried(currentSubs)) {
			findData(isOrder);
			cache.addQueried(currentSubs, isOrder);
		}
	}

	private void findData(boolean isOrder) {

		String tableName = generateTableName(currentSubs);
		String code = getGlobalMapCode(tableName);

		if (code.equals("92")) {
			return;
		}

		Map<String, String> map = null;
		if (code.equals("90") || code.equals("91")) {

			String like = null;
			like = generateKeyToLikeQuery(currentSubs, (isOrder ? 1 : 0));

			try {
				map = dao.like(tableName, like);
			} catch (DataAccessException e) {
				throw new IllegalStateException("Fail to execute like clause in the table " + tableName
						+ " and subscripts like " + like);

			}

			if (map != null) {
				Set<Entry<String, String>> result = map.entrySet();
				for (Entry<String, String> entry : result) {
					// Here we have to include variable or table name into the
					// key
					// because this is part of the subscripts.
					cache.set(generateSubs(tableName, entry.getKey()), entry.getValue());
				}
			}

		} else {

			String like = null;
			like = generateKeyToLikeQuery(currentSubs, (isOrder ? 1 : 0)).replace("~", "||");

			map = jdbcObj.findRecords(this, tableName, like + "%");

			if (map != null) {
				Set<Entry<String, String>> result = map.entrySet();
				for (Entry<String, String> entry : result) {
					// Here we have to include variable or table name into the
					// key
					// because this is part of the subscripts.
					if (code.equals("4")) {
						cache.set(generateSubs(tableName, entry.getKey().replace("||", "~")), entry.getValue());
					} else {
						cache.set(generateSubs(tableName, entry.getKey().replace("||", "~") + "~1"), entry.getValue());
					}
				}
			}

		}

		/*
		 * // Não necessita mais carregar o registro do subscrito corrente (a
		 * carga já é realizada junto com o like) if ((currentSubs.length>1)
		 * && (!currentSubs[currentSubs.length-1].toString().isEmpty())) {
		 * Object value = get(currentSubs); }
		 */
	}

	public Object get(Object... subs) {
		Object value = cache.get(subs);
		if (value != null) {
			return value;
		}

		if (cache.isQueried(subs)) {
			return null;
		}

		String tableName = generateTableName(subs);
		String code = getGlobalMapCode(tableName);

		switch (code) {
		case "0":
		case "4":
			value = loadRelationalData(code, subs, tableName);
			break;
		case "90":
			value = loadKeyValueData(tableName, subs);
			break;
		case "91":
			value = loadKeyValueData(tableName, subs);
			break;
		case "92":
			return null;
		default:
			value = loadRelationalData(code, subs, tableName);
			break;
		}

		if (value != null) {
			cache.set(subs, value);
		}

		if (!cache.isQueried(subs)) {
			cache.addQueried(subs);
		}

		return value;
	}

	/*
	 * This method was create to support lastVar function and should not remove.
	 */
	public Object[] getCurrentSubs() {
		return currentSubs;
	}

	private String getGlobalMapCode(String tableName) {

		if (tableName.startsWith("CacheTemp") || tableName.startsWith("mtemp") || tableName.startsWith("VARTemp")) {
			return "92";
		}

		if (CLASSCDEFTABLE.equals(tableName) || CLASSMAPPINGTABLE.equals(tableName)
				|| GLOBALSAVEMAPTABLE.equals(tableName)) {
			return "91";
		}

		Object[] subs = { "^" + GLOBALSAVEMAPTABLE, tableName };
		Object codeObj = get(subs);
		String code = ((codeObj == null) ? null : codeObj.toString());

		if (code == null) {
			if (tableName.startsWith("Y")) {
				code = "92";
			} else if (cache.isIndexTable(tableName)) {
				code = "91";
			} else {
				code = "91";
			}
		}

		return code;
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public void kill(Object[] subs) {
		currentSubs = null;
		if (subs[0].toString().equals("^WWWUSER")) {
			System.out.println("Kill: " + Arrays.toString(subs));
		}

		String tableName = DataStructureUtil.generateTableName(subs);
		String key = DataStructureUtil.generateKeyWithoutVarName(subs);

		String code = getGlobalMapCode(tableName);

		switch (code) {
		case "0":
		case "4":
			deleteRelationalData(code, subs, tableName, key);
			if (cache.isQueried(subs)) {
				deleteCacheData(subs);
			}
			break;

		case "90":
			deleteRelationalData(code, subs, tableName, key);
			deleteKeyValueData(tableName, key);
			deleteCacheData(subs);
			break;

		case "91":
			deleteKeyValueData(tableName, key);
			deleteCacheData(subs);
			break;

		// Dados transientes ou temporarios
		case "92":
			deleteCacheData(subs);
			break;

		// Default deve ser tratado como o codigo zero.
		default:
			deleteRelationalData(code, subs, tableName, key);
			if (cache.isQueried(subs)) {
				deleteCacheData(subs);
			}
			break;
		}

	}

	private Object loadKeyValueData(String tableName, Object[] subs) {

		try {
			return dao.find(tableName, generateKeyWithoutVarName(subs));
		} catch (DataAccessException e) {
			throw new IllegalStateException("Fail to execute find value from the table " + tableName
					+ " and subscripts " + generateKeyWithoutVarName(subs), e);
		}

	}

	private Object loadRelationalData(String code, Object[] subs, String tableName) {
		String globalRefID = "";
		int length = subs.length - ("4".equals(code) ? 0 : 1);

		for (int i = 1; i < length; i++) {
			globalRefID = (globalRefID.isEmpty() ? "" : globalRefID + "||") + util.mFunctionUtil.toString(subs[i]);
		}
		return jdbcObj.loadRecord(this, tableName, globalRefID);
	}

	@Override
	public void merge(Object[] dest, Object[] orig) {
		cache.merge(dest, orig);
	}

	public Object order(Object... subs) {
		return order(subs, 1);
	}

	public Object order(Object[] subs, int direction) {
		this.currentSubs = subs;

		fillCache(true);
		return cache.order(subs, direction);
	}

	private void saveCacheData(Object value) {
		cache.set(currentSubs, value);
		// changeTrigger.insert(currentSubs, value);
	}

	private void saveKeyValueData(String tableName, Object value) {
		try {
			dao.insert(tableName, DataStructureUtil.generateKeyWithoutVarName(currentSubs), value);
		} catch (DataAccessException e) {
			throw new IllegalStateException("Fail to insert data into table: " + tableName + " key: "
					+ DataStructureUtil.generateKeyWithoutVarName(currentSubs) + " and value: " + value, e);
		}
	}

	private void saveRelationalData(String code, String tableName, Object value) {
		String globalRefID = "";
		int length = currentSubs.length - ("4".equals(code) ? 0 : 1);

		for (int i = 1; i < length; i++) {
			globalRefID = (globalRefID.isEmpty() ? "" : globalRefID + "||")
					+ util.mFunctionUtil.toString(currentSubs[i]);
		}
		jdbcObj.saveRecord(this, tableName, globalRefID, util.mFunctionUtil.toString(value));
	}

	@REVIEW(author = "vinicius", date = "03/09/2014", description = "Ja que as tabelas estao em cache podemos incluir os codigos em cache tambem")
	public void set(Object value) {

		if (currentSubs != null && value != null) {
			String tableName = generateTableName(currentSubs);
			String code = getGlobalMapCode(tableName);

			switch (code) {
			case "0":
			case "4":
				saveRelationalData(code, tableName, value);
				if (cache.isQueried(currentSubs)) {
					saveCacheData(value);
				}
				break;

			case "90":
				saveRelationalData(code, tableName, value);
				saveKeyValueData(tableName, value);
				saveCacheData(value);
				break;

			case "91":
				saveKeyValueData(tableName, value);
				saveCacheData(value);
				break;

			// Dados transientes ou temporarios
			case "92":
				saveCacheData(value);
				break;

			// Default deve ser tratado como o codigo zero.
			default:
				saveRelationalData(code, tableName, value);
				if (cache.isQueried(currentSubs)) {
					saveCacheData(value);
				}
				break;
			}
		}
	}

	public void stacking(Object... variables) {
		throw new UnsupportedOperationException("Stacking variable is not supported to access data on disk");
	}

	@Override
	public void stackingBlock(int indexBlock, int dispatchIndex, Object... variables) {
		throw new UnsupportedOperationException(
				"Stacking variable into a block is not supported to access data on disk");
	}

	public void stackingExcept(Object... variables) {
		throw new UnsupportedOperationException("Stacking variable is not supported to access data on disk");
	}

	@Override
	public void stackingExceptBlock(int blockIndex, int dispatchIndex, Object... variables) {
		throw new UnsupportedOperationException(
				"Stacking variable into a block is not supported to access data on disk");
	}

	public void unstacking() {
		throw new UnsupportedOperationException("Stacking variable is not supported to access data on disk");
	}

	@Override
	public void unstackingBlock(int indexBlock, int indexDispatch) {
		throw new UnsupportedOperationException(
				"Stacking variable into a block is not supported to access data on disk");
	}
}
