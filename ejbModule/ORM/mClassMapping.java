package ORM;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ORM.annotations.Data;
import br.com.innovatium.mumps2java.dataaccess.MetadataDAO;
import br.com.innovatium.mumps2java.dataaccess.SQLMetadataDAOImpl;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocator;
import br.com.innovatium.mumps2java.dataaccess.ServiceLocatorException;
import br.com.innovatium.mumps2java.dataaccess.exception.DataAccessException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Estrutura de definição de mapeamento de classes compiladas
 * 
 * @author Innovatium Systems (mosselaar)
 */
public class mClassMapping {

	private String className;

	private String classType;

	private List<mClassMappingIndex> indexList = new ArrayList<mClassMappingIndex>();

	private List<mClassMappingProp> propertyList = new ArrayList<mClassMappingProp>();

	private Map<String, mClassMappingProp> propertyNameMap;

	private Map<String, mClassMappingProp> propertyNumberMap;

	private String saveMethod;

	private Boolean sharedFile;

	private String sqlTableName;

	// Construtor criado para construir o objeto a partir de uma string json
	// efetuado na compilacao das alteracoes das classes do netmanager.
	public mClassMapping() {

	}

	public mClassMapping(String className) {
		this.className = className;
	}

	public void addProperty(mClassMappingProp prop) {
		propertyList.add(prop);
	}

	private void fillMap(Map<String, mClassMappingProp> map, List<mClassMappingProp> props) {
		for (mClassMappingProp prop : props) {
			map.put(prop.getPropertyName(), prop);
		}
	}

	public Map<String, mClassMappingProp> findAdds(mClassMapping mappping) {
		Map<String, mClassMappingProp> map = getPropertyNameMap();
		Map<String, mClassMappingProp> changes = new HashMap<>();
		// Essa condicao indica que o mapeamento ainda nao foi incluido no banco de
		// dados e portanto, qualquer property deve ser incluida.
		if (map.isEmpty()) {
			for (mClassMappingProp prop : mappping.getPropertyList()) {
				changes.put(prop.getPropertyName(), prop);
			}
		} else {
			for (mClassMappingProp prop : mappping.getPropertyList()) {
				if (!map.containsKey(prop.getPropertyName())) {

					Field[] fields = prop.getClass().getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);

						try {
							Object value = field.get(prop);
							if (value != null) {
								changes.put(field.getName(), prop);
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							field.setAccessible(false);
						}
					}
				}
			}
		}
		return changes;
	}

	public Map<String, mClassMappingProp> findDrops(mClassMapping mapping) {
		Map<String, mClassMappingProp> map = new HashMap<>();
		Map<String, mClassMappingProp> changes = new HashMap<>();
		fillMap(map, mapping.getPropertyList());

		for (mClassMappingProp prop : propertyList) {
			if (!map.containsKey(prop.getPropertyName())) {
				changes.put(prop.getPropertyName(), prop);
			}
		}
		return changes;
	}

	public Map<String, mClassMappingProp> findUpdates(mClassMapping mapping) {
		Map<String, mClassMappingProp> map = getPropertyNameMap();
		Map<String, mClassMappingProp> changes = new HashMap<>();

		mClassMappingProp tempProp = null;
		for (mClassMappingProp prop : mapping.getPropertyList()) {
			if (map.containsKey(prop.getPropertyName())) {
				tempProp = map.get(prop.getPropertyName());

				Field[] fields = prop.getClass().getDeclaredFields();
				Field tempField = null;
				Object value = null;
				Object tempValue = null;
				for (Field field : fields) {
					try {
						tempField = tempProp.getClass().getDeclaredField(field.getName());

						field.setAccessible(true);
						tempField.setAccessible(true);

						value = field.get(prop);
						tempValue = tempField.get(tempProp);
						if (value != null && !value.equals(tempValue) && field.isAnnotationPresent(Data.class)) {
							changes.put(field.getName(), prop);
						}

					} catch (Exception e) {
						throw new IllegalStateException("Falha na geracao das alteracoes do mapeamento da classe "
								+ mapping.getClassName(), e);
					} finally {
						field.setAccessible(false);
						tempField.setAccessible(false);
					}

				}

			}
		}
		return changes;
	}

	public String getClassName() {
		return className;
	}

	public String getClassType() {
		return classType;
	}

	public String getEntityName() {
		return className.replace('.', '_');
	}

	public List<mClassMappingProp> getFieldList() {
		return null;
	}

	public List<mClassMappingIndex> getIndexList() {
		return indexList;
	}

	public mClassMappingProp[] getPKArray() {
		return getPKList().toArray(new mClassMappingProp[] {});
	}

	public List<mClassMappingProp> getPKList() {

		List<mClassMappingProp> pkList = new ArrayList<>();

		if (propertyList == null) {
			return pkList;
		}

		for (mClassMappingProp prop : propertyList) {
			if (prop.isPropertyPK()) {
				pkList.add(prop);
			}
		}
		return pkList;
	}

	public String[] getPKSQLColumnNames() {
		List<mClassMappingProp> pks = getPKList();
		String[] pkNames = new String[pks.size()];
		for (int i = 0; i < pkNames.length; i++) {
			pkNames[i] = pks.get(i).getSqlColumnName();
		}
		return pkNames;
	}

	public List<mClassMappingProp> getPropertyList() {
		return propertyList;
	}

	public mClassMappingProp getPropertyMapping(int propertyNumber) {
		return null;
	}

	public mClassMappingProp getPropertyMapping(String propertyName) {
		return null;
	}

	public Map<String, mClassMappingProp> getPropertyNameMap() {
		if (propertyNameMap == null) {
			propertyNameMap = new HashMap<>();
			fillMap(propertyNameMap, propertyList);
		}
		return propertyNameMap;
	}

	public String getSaveMethod() {
		return saveMethod;
	}

	public Boolean getSharedFile() {
		return sharedFile;
	}

	public String getSqlTableName() {
		return sqlTableName;
	}

	public boolean isAutoRef(String propClassName) {
		return className.equals(propClassName);
	}

	public void reload() {
	}

	public void save() {
		try {
			MetadataDAO dao = ServiceLocator.locate(SQLMetadataDAOImpl.class);
			Gson gson = new Gson();
			JsonElement json = gson.toJsonTree(this);
			JsonObject gsob = json.getAsJsonObject();
			gsob.remove("isListFilled");
			gsob.remove("propertyNameMap");
			gsob.remove("propertyNumberMap");
			dao.insert("WWWCLASSMAPPING", this.className, gson.toJson(json));

		} catch (ServiceLocatorException e) {
			throw new IllegalArgumentException("Fail look up the service to persiste the class mapping definitions", e);
		} catch (DataAccessException e) {
			throw new IllegalStateException("Fail to insert json string from the classmapping definition of the class "
					+ this.className + " and json string is " + new Gson().toJson(this), e);
		}

	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setIndexList(List<mClassMappingIndex> indexList) {
		this.indexList = indexList;
	}

	public void setPropertyList(List<mClassMappingProp> propertyList) {
		this.propertyList = propertyList;
	}

	public void setSaveMethod(String saveMethod) {
		this.saveMethod = saveMethod;
	}

	public void setSharedFile(Boolean sharedFile) {
		this.sharedFile = sharedFile;
	}

	public void setSqlTableName(String sqlTableName) {
		this.sqlTableName = sqlTableName;
	}
}
