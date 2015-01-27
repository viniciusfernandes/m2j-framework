package br.com.innovatium.mumps2java.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.thrift.TException;
import org.hypertable.thrift.ThriftClient;
import org.hypertable.thriftgen.Cell;
import org.hypertable.thriftgen.ClientException;
import org.hypertable.thriftgen.HqlResult;
import org.hypertable.thriftgen.Key;
import org.hypertable.thriftgen.KeyFlag;
import org.hypertable.thriftgen.RowInterval;
import org.hypertable.thriftgen.ScanSpec;

import br.com.innovatium.mumps2java.dataaccess.exception.DataAccessException;
import br.com.innovatium.mumps2java.dataaccess.exception.NoSQLExecutionException;
import br.com.innovatium.mumps2java.dataaccess.exception.NonUniqueResultException;
import br.com.innovatium.mumps2java.datastructure.GlobalCache;

@Local
@Stateless
public class NoSQLMetadataDAOImpl implements MetadataDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1864709162571154205L;
	private ThriftClient client = null;
	private final GlobalCache metadataCache = GlobalCache.getCache();

	private Map<String, Long> mutatorMap = new HashMap<String, Long>(30);
	private long namespaceCode = 0;

	private ScanSpec scannerSpec;

	public NoSQLMetadataDAOImpl() {
		init();
	}

	public void createTable(String tableName) throws ClientException, TException {
		client.hql_query(namespaceCode, "create table " + tableName + " ('value_')");

		metadataCache.addTableName(tableName);
	}

	@PreDestroy
	public void destroy() throws ClientException, TException {
		for (Long mutator : mutatorMap.values()) {
			client.mutator_close(mutator);
		}
		if (client != null) {
			client.close();
		}
	}

	@Override
	public Object find(String tableName, String key) {
		if (!hasTable(tableName)) {
			return null;
		}
		return findByKey(tableName, key);
	}

	@Override
	public String findGlobalMapCode(String tableName) throws DataAccessException {
		return findByKey(GLOBALSAVEMAPTABLE, tableName);
	}

	public boolean hasTable(String tableName) {
		return metadataCache.isTableNameCached(tableName.toLowerCase());
	}

	@PostConstruct
	public void init() {
		try {
			client = ThriftClient.create("iv-ora01", 38080);
			namespaceCode = client.namespace_open("metauser");
		}
		catch (Exception e) {
			throw new IllegalStateException("Fail to connect to hypertable database");
		}

		scannerSpec = new ScanSpec();

		List<String> columns = new ArrayList<>();
		columns.add("value_");
		scannerSpec.setColumns(columns);

		// Return only most recent version of each cell
		scannerSpec.setVersions(1);

		HqlResult result = null;
		try {
			result = client.hql_query(namespaceCode, "show tables");
		}
		catch (ClientException | TException e) {
			throw new IllegalStateException("Fail to load table names to fill metadata cache", e);
		}

		for (String tableName : result.results) {
			metadataCache.addTableName(tableName.toLowerCase());
		}
	}

	@Override
	public void insert(String tableName, Object key, Object value) throws NoSQLExecutionException {

		if (!hasTable(tableName)) {
			try {
				createTable(tableName);
			}
			catch (ClientException | TException e) {
				throw new NoSQLExecutionException("Fail to create table before insert data. Table name is " + tableName
						+ ", key is " + key + " and value is " + value, e);
			}
		}

		Key keyCell = new Key(key.toString(), "value_", null, KeyFlag.INSERT);
		Cell cell = new Cell(keyCell);
		cell.setValue(value.toString().getBytes());

		Long mutator = mutatorMap.get(tableName);
		try {
			if (mutator == null) {
				mutator = client.mutator_open(namespaceCode, tableName, 0, 0);
			}

			client.mutator_set_cell(mutator, cell);
			client.mutator_flush(mutator);
		}
		catch (Exception e) {
			throw new IllegalStateException("Fail to insert key: " + key + " and value: " + value + " into the table: "
					+ tableName, e);
		}
	}

	@Override
	public Map<String, String> like(String tableName, String key) throws NoSQLExecutionException {

		if (!hasTable(tableName)) {
			return null;
		}

		HqlResult result = null;
		try {
			result = client.hql_query(namespaceCode, "select CELLS * from " + tableName + " where ROW =^ '" + key + "'");
		}
		catch (Exception e) {
			return null;
		}

		if (result.getCells() == null) {
			return new HashMap<>(0);
		}

		Map<String, String> keyValue = new HashMap<>(50);
		String value = null;
		for (Cell cell : result.getCells()) {
			if (cell.getValue() == null) {
				value = "";
			}
			else {
				value = new String(cell.getValue());
			}
			keyValue.put(cell.getKey().getRow(), value);
		}

		return keyValue;
	}

	@Override
	public void remove(String tableName, String key) {
		try {
			client.hql_query(namespaceCode, "delete * from " + tableName + " where row = '" + key + "'");
		}
		catch (Exception e) {
			throw new IllegalStateException("Fail to delte key: " + key + " from the table: " + tableName);
		}
	}

	private String findByKey(String tableName, String key) {
		// Estamos redefinindo o valor da chave pois o hypertable executa traz
		// todos os registros da tabela quando uma chave eh uma string vazia.
		if ("".equals(key)) {
			key = " ";
		}
		List<RowInterval> intervals = new ArrayList<>();

		// Row range [lemon..orange)
		RowInterval ri = new RowInterval();
		ri.setStart_row(key);
		ri.setEnd_row(key);
		ri.setStart_inclusive(true);
		ri.setEnd_inclusive(true);

		intervals.add(ri);
		scannerSpec.setRow_intervals(intervals);

		try {
			long scannerCode = client.scanner_open(namespaceCode, tableName, scannerSpec);
			List<Cell> cells = client.scanner_get_cells(scannerCode);

			if (cells.isEmpty()) {
				return null;
			}
			else if (cells.size() > 1) {
				throw new NonUniqueResultException("There was multiples results to the key " + key + " from the table "
						+ tableName + " It was found " + cells.size() + " keys.");
			}

			return new String(cells.get(0).getValue());

		}
		catch (ClientException | TException e1) {
			throw new IllegalStateException("Fail to search value to the key " + key + " of the table " + tableName, e1);
		}

	}
}
