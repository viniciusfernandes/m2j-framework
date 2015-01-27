package ORM;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;

import util.mFunctionUtil;

/**
 * <b>ORM - Object Relational Mapping</b>
 * <p>
 * Entidade de persistência dinâmica de objetos baseada na existência da
 * definição de mapeamento da classe.
 * <p>
 * <p>
 * Qualquer classe que possua uma pré-definição de estrutura de mapeamento junto
 * ao banco de dados relacional poderá ser acessada através de uma entidade
 * dinâmica (vide ORM.mClassCDef para mais informações sobre o controle e
 * registro de definição dos mapeamentos das classes).
 * <p>
 * <p>
 * Aplicações utilizando esta estrutura de entidade poderão acessar
 * dinamicamente os dados persistentes no banco de dados relacional via JPA
 * (Java Persistence API). O mapeamento das classes é realizado dinamicamente
 * junto ao "class loader" e as tabelas são reconhecidas diretamente no JPQL
 * (Java Persistence Query Language). Todas as propriedades são automaticamente
 * mapeadas para os atributos correspondentes, permitindo ainda o acesso às
 * entidades relacionadas (one-to-one, one-to-many e many-to-one). Os dados
 * podem ser acessados no formato M (tecnologia M - mumps (default)), formato
 * java ou formato externo de apresentação (vide mSystem.NLS para definições de
 * idioma e de formatos de apresentação de datas, horários e números).
 * <p>
 * <p>
 * Para uma maior flexibilidade de uso e agilidade na conversão do sistema
 * AlphaLinc, esta estrutura possui métodos legados para manipulação de dados no
 * formato de global com mapeamento de campos utilizando um delimitador. A
 * utilização destes métodos legados não é recomendada em novas implementações,
 * pois oneram o desempenho e prejudicam a legibilidade, bem como, a
 * manutenibilidade do código.
 * <p>
 * 
 * @author Innovatium Systems (mosselaar)
 */
public class mEntity {

	private static final mEntityManager em = mEntityManager.getInstance();

	/**
	 * Obtém relação de propriedades da chave primária da entidade
	 * 
	 * @return array de nomes das propriedades da chave primária
	 */
	public List<String> getPropertyIdList() {
		return this.getPkNames();
	}

	/**
	 * Obtém relação de todas as propriedades da entidade
	 * 
	 * @return array de nomes das propriedades da entidade
	 */
	public List<String> getPropertyList() {
		List<String> list = new ArrayList<>();
		for (mClassMappingProp props : this.entityProperties.getProperties(this.entityName)) {
			list.add(props.getPropertyName());
		}
		return list;
	}

	/**
	 * Inicializa definição das entidades para as classes informadas (as classes
	 * devem existir na estrutura de mapeamento de classes compiladas)
	 * 
	 * @param classNames
	 *          nome da(s) classe(s) compilada(s)
	 */
	public static void initialize(String... classNames) {
		em.initialize(classNames);
	}

	/**
	 * Abre o último registro da entidade considerando os valores informados da
	 * chave primária
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity last(Object... idValues) {
		// TODO
		return null;
	}

	/**
	 * Abre o último registro da entidade considerando os valores informados da
	 * chave primária
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity lastId(String idKey) {
		// TODO
		return null;
	}

	/**
	 * Realiza um lock exclusivo do registro (instância) selecionado (select for
	 * update)
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lock(Object... idValues) {
		// TODO
		return false;
	}

	/**
	 * Realiza um lock exclusivo do registro (instância) selecionado (select for
	 * update)
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lockId(String idKey) {
		// TODO
		return false;
	}

	/**
	 * Realiza um lock compartilhado do registro (instância) selecionado (shared
	 * mode)
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lockShared(Object... idValues) {
		// TODO
		return false;
	}

	/**
	 * Realiza um lock compartilhado do registro (instância) selecionado (shared
	 * mode)
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lockSharedId(String idKey) {
		// TODO
		return false;
	}

	/**
	 * Avança e abre um registro da entidade
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity next(Object... idValues) {
		// TODO
		return null;
	}

	/**
	 * Avança e abre um registro da entidade
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity nextId(String idKey) {
		// TODO
		return null;
	}

	/**
	 * Retrocede e abre um registro da entidade
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity previous(Object... idValues) {
		// TODO
		return null;
	}

	/**
	 * Retrocede e abre um registro da entidade
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity previousId(String idKey) {
		// TODO
		return null;
	}

	/**
	 * Força reinicialização da definição das entidades para as classes informadas
	 * (as classes devem existir na estrutura de mapeamento de classes compiladas)
	 * A reinicialização da entidade é necessária caso haja alguma alteração na
	 * estrutura de dados (nova definição no class mapping)
	 * 
	 * @param classNames
	 *          nome da(s) classe(s) compilada(s)
	 */
	public static void reinitialize(String... classNames) {
		em.reinitialize(classNames);
	}

	private final String _ID = "_Id";

	private Map<String, String> changedFK;

	private DynamicEntity dynamicEntity;

	private final String entityName;

	private final mEntityProperties entityProperties;

	private final List<String> pkNames = new ArrayList<>();

	/**
	 * Cria uma instância (ou registro) de uma entidade conforme definição da
	 * classe compilada (a classe deve existir na estrutura de mapeamento de
	 * classes compiladas)
	 * 
	 * @param className
	 *          nome da classe compilada
	 */
	public mEntity(String className) {
		this(className, null);
	}

	mEntity(String className, DynamicEntity dynamicEntity) {
		String entityName = null;
		if (className != null) {
			entityName = className.replace('.', '_');
		}

		boolean entityPopulated = dynamicEntity != null;
		if (entityPopulated) {
			this.dynamicEntity = dynamicEntity;
		} else {
			DynamicType type = em.loadType(className);
			this.dynamicEntity = type.newDynamicEntity();
		}

		this.entityName = entityName;
		// Aqui estamos certos de que as propriedade ja foram carregadas pelo
		// mEntityLoader.
		this.entityProperties = mEntityProperties.getInstance();

		String pkName = null;
		for (mClassMappingProp pk : entityProperties.getPKList(entityName)) {
			pkName = pk.getPropertyName();
			pkNames.add(pkName);
			if (entityPopulated) {
				set(pkName, dynamicEntity.get(pkName));
			}
		}

	}

	/**
	 * Verifica existência de registro da entidade
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return booleano indicando existência do registro
	 */
	public boolean exists(Object... idValues) {
		return em.exists(entityName, pkNames, idValues);
	}

	/**
	 * Verifica existência de registro da entidade
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return booleano indicando existência do registro
	 */
	public boolean existsId(String idKey) {
		if (this.getPkNames().size() > 0) {
			return em.exists(this.entityName, this.getPkNames(), splitIdKey(idKey));
		} else {
			return em.exists(this.entityName, Arrays.asList("_Id"), splitIdKey(idKey));
		}
	}

	/**
	 * Verifica existência de registro da entidade
	 * 
	 * @param idSeq
	 *          id sequencial do registro (inteiro com a sequence)
	 * @return booleano indicando existência do registro
	 */
	public boolean existsIdSeq(Integer idSeq) {
		return em.exists(this.entityName, Arrays.asList("_IdSeq"), new Object[] { idSeq });
	}

	/**
	 * Abre o primeiro registro da entidade considerando os valores informados da
	 * chave primária
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity first(Object... idValues) {
		// TODO
		return null;
	}

	/**
	 * Abre o primeiro registro da entidade considerando os valores informados da
	 * chave primária
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity firstId(String idKey) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade da entidade (no formato java)
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @return valor da propriedade da entidade
	 */
	public Object get(String propertyName) {
		try {
			return dynamicEntity.get(propertyName);
		} catch (Exception e) {
			throw new IllegalArgumentException("Property " + propertyName + " not found in " + this.getEntityName() + ".", e);
		}
	}

	/**
	 * Obtém valor da propriedade de data no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param dateformat
	 *          código de formatação da data (vide opções em $zdate)
	 * @return valor formatado da propriedade
	 */
	public String getDateDisplay(String propertyName, int dateformat) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade de data/hora no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param precision
	 *          precisão de fração de segundos
	 * @return valor formatado da propriedade
	 */
	public String getDateTimeDisplay(String propertyName, int precision) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade de data/hora no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param precision
	 *          precisão de fração de segundos
	 * @param dateformat
	 *          código de formatação da data (vide opções em $zdate)
	 * @param timeformat
	 *          código de formatação da hora (vide opções em $ztime)
	 * @return valor formatado da propriedade
	 */
	public String getDateTimeDisplay(String propertyName, int precision, int dateformat, int timeformat) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade da entidade (no formato externo de apresentação)
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @return valor da propriedade da entidade
	 */
	public String getDisplay(String propertyName) {
		// TODO
		return null;
	}

	/**
	 * Obtém diretamente a coleção de entidades relacionadas conforme classe de
	 * destino com chave estrangeira
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @return coleção de entidades relacionadas
	 */
	public List<mEntity> getEntityDirectList(String targetClassName) {
		String targetEntityName = null;
		if (targetClassName != null) {
			targetEntityName = targetClassName.replace(".", "_");
		}

		String exactName0 = this.entityName.replace("_", "");
		String exactName1 = null;
		String exactName2 = null;
		if (this.entityName.indexOf('_') >= 0) {
			exactName1 = this.entityName.substring(this.entityName.indexOf('_') + 1).replace("_", "");
			if (this.entityName.indexOf('_', this.entityName.indexOf('_') + 1) >= 0) {
				exactName2 = this.entityName.substring(this.entityName.indexOf('_', this.entityName.indexOf('_') + 1)).replace(
						"_", "");
			}
		}

		String propertyName = null;
		for (mClassMappingProp prop : entityProperties.getFKList(targetEntityName)) {
			if (prop.getClassNameFK().equals(targetClassName)) {
				propertyName = prop.getPropertyName();
				if (propertyName.equals(exactName0) || propertyName.equals(exactName1) || propertyName.equals(exactName2)) {
					return this.getEntityDirectList(targetClassName, propertyName);
				}
			}
		}

		return null;
	}

	/**
	 * Obtém diretamente a coleção de entidades relacionadas conforme classe e
	 * propriedade de destino com chave estrangeira (caso classe de destino possua
	 * múltiplos relacionamentos)
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @param propertyName
	 *          nome da propriedade de destino com chave estrangeira
	 * @return coleção de entidades relacionadas
	 */
	public List<mEntity> getEntityDirectList(String targetClassName, String propertyName) {
		mEntity.initialize(targetClassName);
		String targetEntityName = null;
		if (targetClassName != null) {
			targetEntityName = targetClassName.replace(".", "_");
		}
		mClassMappingProp propertyMap = entityProperties.getProperty(targetEntityName, propertyName);
		if ((propertyMap != null) && (propertyMap.isPropertyIdFK())) {

			// Retornar lista de targetClassName onde propertyName = this._Id (id
			// value da entidade corrente)
			return em.loadList(targetEntityName, propertyName, getIDValue());
		} else {
			return em.loadList(this, targetClassName, propertyName);
		}
	}

	/**
	 * Obtém objeto de coleção de entidades relacionadas conforme classe de
	 * destino com chave estrangeira
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @return objeto de coleção de entidades relacionadas
	 */
	public mEntityList getEntityList(String targetClassName) {
		String targetEntityName = null;
		if (targetClassName != null) {
			targetEntityName = targetClassName.replace(".", "_");
		}

		String exactName0 = this.entityName.replace("_", "");
		String exactName1 = null;
		String exactName2 = null;
		if (this.entityName.indexOf('_') >= 0) {
			exactName1 = this.entityName.substring(this.entityName.indexOf('_') + 1).replace("_", "");
			if (this.entityName.indexOf('_', this.entityName.indexOf('_') + 1) >= 0) {
				exactName2 = this.entityName.substring(this.entityName.indexOf('_', this.entityName.indexOf('_') + 1)).replace(
						"_", "");
			}
		}

		String propertyName = null;
		for (mClassMappingProp prop : entityProperties.getFKList(targetEntityName)) {
			if (prop.getClassNameFK().equals(targetClassName)) {
				propertyName = prop.getPropertyName();
				if (propertyName.equals(exactName0) || propertyName.equals(exactName1) || propertyName.equals(exactName2)) {
					return new mEntityList(this, this.getEntityDirectList(targetClassName, propertyName), propertyName);
				}
			}
		}

		return null;
	}

	/**
	 * Obtém objeto de coleção de entidades relacionadas conforme classe e
	 * propriedade de destino com chave estrangeira (caso classe de destino possua
	 * múltiplos relacionamentos)
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @param propertyName
	 *          nome da propriedade de destino com chave estrangeira
	 * @return objeto de coleção de entidades relacionadas
	 */
	public mEntityList getEntityList(String targetClassName, String propertyName) {
		return new mEntityList(this, this.getEntityDirectList(targetClassName, propertyName), propertyName);
	}

	/**
	 * Obtém nome da entidade
	 * 
	 * @return nome da entidade
	 */
	public String getEntityName() {
		return this.entityName;
	}

	/**
	 * Obtém referência de entidade conforme chave estrangeira com classe de
	 * destino
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @return instância da entidade referenciada (registro)
	 */
	public mEntity getEntityObj(String targetClassName) {
		String targetEntityName = null;
		if (targetClassName != null) {
			targetEntityName = targetClassName.replace(".", "_");
		}

		String exactName0 = targetEntityName.replace("_", "");
		String exactName1 = null;
		String exactName2 = null;
		if (targetEntityName.indexOf('_') >= 0) {
			exactName1 = targetEntityName.substring(targetEntityName.indexOf('_') + 1).replace("_", "");
			if (targetEntityName.indexOf('_', targetEntityName.indexOf('_') + 1) >= 0) {
				exactName2 = targetEntityName.substring(targetEntityName.indexOf('_', targetEntityName.indexOf('_') + 1))
						.replace("_", "");
			}
		}

		String propertyName = null;
		for (mClassMappingProp prop : entityProperties.getFKList(this.entityName)) {
			propertyName = prop.getPropertyName();
			// TODO: validar classeNameFK com targetClassName
			if (propertyName.equals(exactName0) || propertyName.equals(exactName1) || propertyName.equals(exactName2)) {
				return this.getEntityObj(targetClassName, propertyName);
			}
		}

		return null;
	}

	/**
	 * Obtém referência de entidade conforme chave estrangeira da propriedade
	 * 
	 * @param targetClassName
	 *          nome da classe de destino
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @return instância da entidade referenciada (registro)
	 */
	public mEntity getEntityObj(String targetClassName, String propertyName) {
		mClassMappingProp propertyMap = entityProperties.getProperty(this.entityName, propertyName);
		if ((propertyMap != null) && (propertyMap.isPropertyIdFK())) {
			mEntity e = new mEntity(propertyMap.getClassNameFK());
			e.openId(mFunctionUtil.toString(getM(propertyName)));
			return e;
		} else {
			propertyName = em.generateObjName(propertyName);
			DynamicEntity loaded = (DynamicEntity) get(propertyName);
			mEntity e = new mEntity(targetClassName);
			e.dynamicEntity = loaded;
			return e;
		}
	}

	/**
	 * Obtém string com a chave primária (valores delimitados por ||)
	 * 
	 * @return idKey do registro
	 */
	public String getId() {
		String Id = null;
		if (this.pkNames.size() > 0) {
			for (String name : this.pkNames) {
				Object value = this.getM(name);
				if (value == null) {
					value = "";
				} else {
					value = mFunctionUtil.toString(value);
				}
				Id = ((Id == null) ? "" : Id + "||") + value;
			}
		} else {
			Object objId = this.getM("_Id");
			if (objId == null) {
				return null;
			}
			Id = mFunctionUtil.toString(objId);
		}
		return Id;
	}

	/**
	 * Obtém inteiro com o id sequencial
	 * 
	 * @return idSeq do registro
	 */
	public Integer getIdSeq() {
		return mFunctionUtil.numberConverter(this.getM("_IdSeq")).intValue();
	}

	/**
	 * Obtém valor da propriedade da entidade (no formato M)
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @return valor da propriedade da entidade
	 */
	public Object getM(String propertyName) {
		Class<?> type = entityProperties.getPropertyType(entityName, propertyName);

		Object value = get(propertyName);
		if (value == null) {
			return "";
		}
		if (type == null) {
			return get(propertyName);
		}
		if (Date.class.isAssignableFrom(type)) {
			return mFunctionUtil.dateTimeJavaToMumpsDouble(get(propertyName));
		}
		if (Timestamp.class.isAssignableFrom(type)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateFormat.setTimeZone(mFunctionUtil.getTimeZone());
			String valueString = dateFormat.format((Timestamp) get(propertyName));
			if (valueString.substring(19).equals(".000")) {
				return valueString.substring(0, 19);
			}
			return valueString;
		}
		return get(propertyName);
	}

	/**
	 * Obtém valor da propriedade numérica no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param decimals
	 *          número de casas decimais
	 * @return valor formatado da propriedade
	 */
	public String getNumericDisplay(String propertyName, int decimals) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade numérica no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param decimals
	 *          número de casas decimais
	 * @param format
	 *          formato de decimais e separador de milhar (primeiro caracter
	 *          determina o separador de decimais e o segundo caracter determina o
	 *          separador de milhar - exemplos: ".," => 9,999,999.99 / ",." =>
	 *          9.999.999,99)
	 * @return valor formatado da propriedade
	 */
	public String getNumericDisplay(String propertyName, int decimals, String format) {
		// TODO
		return null;
	}

	/**
	 * Obtém valores da chave primária
	 * 
	 * @return array de valores da chave primária
	 */
	public Object[] getPK() {
		Object[] pkValues = new Object[pkNames.size()];
		for (int i = 0; i < pkValues.length; i++) {
			pkValues[i] = get(pkNames.get(i));
		}
		return pkValues;
	}

	public List<String> getPkNames() {
		return pkNames;
	}

	/**
	 * Obtém índice posicional de uma propriedade da entidade
	 * 
	 * @param propertyName
	 *          nome da propriedade
	 * @return índice posicional da propridade
	 */
	public int getPropertyIndex(String propertyName) {
		// TODO
		return 0;
	}

	/**
	 * Obtém nome da propriedade de um índice posicional
	 * 
	 * @param index
	 *          índice posicional da propriedade
	 * @return nome da propriedade
	 */
	public String getPropertyName(int index) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade de hora no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param precision
	 *          precisão de fração de segundos
	 * @return valor formatado da propriedade
	 */
	public String getTimeDisplay(String propertyName, int precision) {
		// TODO
		return null;
	}

	/**
	 * Obtém valor da propriedade de hora no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propridade da entidade
	 * @param precision
	 *          precisão de fração de segundos
	 * @param timeformat
	 *          código de formatação da hora (vide opções em $ztime)
	 * @return valor formatado da propriedade
	 */
	public String getTimeDisplay(String propertyName, int precision, int timeformat) {
		// TODO
		return null;
	}

	public boolean isNew() {
		if (this.getIdSeq() == null) {
			return true;
		}
		return false;
	}

	public void loadEntityList(String propertyName) {
		em.loadList(this, this.getEntityName(), propertyName);
	}

	/**
	 * Realiza um lock exclusivo do registro (instância) corrente (select for
	 * update)
	 * 
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lock() {
		// TODO
		return false;
	}

	/**
	 * Realiza um lock compartilhado do registro (instância) corrente (shared
	 * mode)
	 * 
	 * @return booleano indicando se o registro foi locado
	 */
	public boolean lockShared() {
		// TODO
		return false;
	}

	/**
	 * Avança e abre um registro da entidade a partir da instância corrente
	 * 
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity next() {
		// TODO
		return null;
	}

	/**
	 * Abre um registro da entidade
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return booleano indicando se o registro foi aberto
	 */
	public boolean open(Object... idValues) {
		return em.open(this, this.getPkNames(), idValues);
	}

	/**
	 * Abre um registro da entidade
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return booleano indicando se o registro foi aberto
	 */
	public boolean openId(String idKey) {
		/*
		 * if (idKey == null || idKey.isEmpty()) { throw new
		 * IllegalArgumentException("To open/load the mEntity \"" + this.entityName
		 * + "\" the Id must not be null or empty"); }
		 */

		if (this.getPkNames().size() > 0) {
			return em.open(this, this.getPkNames(), splitIdKey(idKey));
		} else {
			return em.open(this, Arrays.asList("_Id"), splitIdKey(idKey));
		}
	}

	/**
	 * Abre um registro da entidade
	 * 
	 * @param idSeq
	 *          id sequencial do registro (inteiro com a sequence)
	 * @return booleano indicando se o registro foi aberto
	 */
	public boolean openIdSeq(Integer idSeq) {
		return em.open(this, Arrays.asList("_IdSeq"), new Object[] { idSeq });
	}

	/**
	 * Retrocede e abre um registro da entidade a partir da instância corrente
	 * 
	 * @return registro da entidade encontrada (nulo caso não for encontrado o
	 *         registro seguinte)
	 */
	public mEntity previous() {
		// TODO
		return null;
	}

	/**
	 * Recarrega o registro (instância) corrente
	 * 
	 * @return booleano indicando se o registro foi recarregado (somente registros
	 *         que não existe na base não são carregados, demais erros geram uma
	 *         exceção)
	 */
	public boolean reload() {
		if (this.getIdSeq() == null) {
			return false;
		}
		this.openIdSeq(this.getIdSeq());
		return true;
	}

	/**
	 * Remove o registro (instância) corrente (as entidades filhas são excluídas
	 * automaticamente) (todas entidades que possuem a chave desta entidade em sua
	 * chave primária são consideradas como filhas e possuem a cláusula para
	 * exclusão em cascata)
	 * 
	 * @return booleano indicando se o registro foi removido (excluído
	 *         fisicamente)
	 */
	public boolean remove() {
		em.remove(this);
		return true;
	}

	/**
	 * Remove o registro (instância) selecionado (as entidades filhas são
	 * excluídas automaticamente) (todas entidades que possuem a chave desta
	 * entidade em sua chave primária são consideradas como filhas e possuem a
	 * cláusula para exclusão em cascata)
	 * 
	 * @param idValues
	 *          valores da chave primária do registro
	 * @return booleano indicando se o registro foi removido (excluído
	 *         fisicamente)
	 */
	public boolean remove(Object... idValues) {
		if (idValues == null || idValues.length <= 0) {
			return false;
		}

		if (this.pkNames.size() != idValues.length) {
			throw new IllegalArgumentException(
					"O numero de IDs deve ser identico ao numero de pks declarados na entidade. Total pks: " + pkNames.size()
							+ ". Nome das pks: " + Arrays.deepToString(pkNames.toArray()));
		}
		mEntity entity = new mEntity(this.entityName);

		for (int i = 0; i < pkNames.size(); i++) {
			entity.set(pkNames.get(i), idValues[i]);
		}
		em.remove(entity);
		return true;
	}

	/**
	 * Remove o registro (instância) selecionado (as entidades filhas são
	 * excluídas automaticamente) (todas entidades que possuem a chave desta
	 * entidade em sua chave primária são consideradas como filhas e possuem a
	 * cláusula para exclusão em cascata)
	 * 
	 * @param idKey
	 *          id do registro (string com a chave primária)
	 * @return booleano indicando se o registro foi removido (excluído
	 *         fisicamente)
	 */
	public boolean removeId(String idKey) {
		return remove((Object[]) splitIdKey(idKey));
	}

	/**
	 * Salva o registro (instância) corrente (incluindo todas as referências de
	 * objetos, exceto listas de relacionamentos 1:N)
	 */
	public void save() {
		if (changedFK != null) {
			for (Map.Entry<String, String> entry : changedFK.entrySet()) {
				String entityName = entry.getValue();
				String fkNameObj = entry.getKey();
				String fkName = fkNameObj.substring(0, fkNameObj.length() - 3);
				DynamicEntity entityFK = dynamicEntity.get(fkNameObj);
				List<mClassMappingProp> pkNames = entityProperties.getPKList(entityName);

				mClassMappingProp fkProp = entityProperties.getProperty(entityName, fkName);
				List<String> fkNames = null;
				if (fkProp != null) {
					fkNames = fkProp.getPropertyListFK();
				}
				if (fkNames != null) {
					for (int i = 0; i < fkNames.size() - 1; i++) {
						if (this.get(fkNames.get(i)) == null) {
							this.set(fkNames.get(i), ((entityFK == null) ? null : entityFK.get(pkNames.get(i).getPropertyName())));
						}
					}
				}
				this.set(fkName, ((entityFK == null) ? null : entityFK.get(pkNames.get(pkNames.size() - 1).getPropertyName())));
			}
		}
		em.merge(this);
		changedFK = null;
	}

	/**
	 * Atribui valor da propriedade da entidade (no formato java)
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param value
	 *          valor da propriedade da entidade
	 */
	public void set(String propertyName, Object value) {
		this.dynamicEntity.set(propertyName, value);
	}

	/**
	 * Atribui valor da propriedade de data no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param dateformat
	 *          código de formatação da data (vide opções em $zdate)
	 * @param value
	 *          valor formatado da propriedade
	 */
	public void setDateDisplay(String propertyName, String value, int dateformat) {
		// TODO
	}

	/**
	 * Atribui valor da propriedade de data no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param dateformat
	 *          código de formatação da data (vide opções em $zdate)
	 * @param timeformat
	 *          código de formatação da hora (vide opções em $ztime)
	 * @param value
	 *          valor formatado da propriedade
	 */
	public void setDateTimeDisplay(String propertyName, String value, int dateformat, int timeformat) {
		// TODO
	}

	/**
	 * Atribui valor da propriedade da entidade (no formato externo de
	 * apresentação)
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param value
	 *          valor da propriedade da entidade
	 */
	public void setDisplay(String propertyName, String value) {
		// TODO
	}

	/**
	 * Atribui referência de entidade conforme chave estrangeira da propriedade
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param entityRef
	 *          instância da entidade referenciada (registro)
	 */
	public void setEntityObj(String propertyName, mEntity entityRef) {
		mClassMappingProp propertyMap = entityProperties.getProperty(this.entityName, propertyName);
		if ((propertyMap != null) && (propertyMap.isPropertyIdFK())) {
			if (entityRef != null) {
				set(propertyName, entityRef.getId());
			} else {
				set(propertyName, null);
			}
		} else {
			String propertyNameObj = em.generateObjName(propertyName);
			if (entityRef != null) {
				set(propertyNameObj, entityRef.dynamicEntity);

				if (changedFK == null) {
					changedFK = new HashMap<String, String>();
				}
				changedFK.put(propertyNameObj, entityRef.getEntityName());
			} else {
				// set(propertyNameObj, null);
				set(propertyName, null);
			}
		}
	}

	/**
	 * Atribui valor da propriedade da entidade (no formato M)
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param value
	 *          valor da propriedade da entidade
	 */
	public void setM(String propertyName, Object value) {
		Class<?> type = entityProperties.getPropertyType(entityName, propertyName);
		if ((type == null) || (value == null)) {
			set(propertyName, value);
			return;
		}
		if ((value instanceof String) && (((String) value).isEmpty())) {
			set(propertyName, null);
			return;
		}
		if (Date.class.isAssignableFrom(type)) {
			set(propertyName, new Date(mFunctionUtil.dateMumpsToJava(value).longValue()));
			return;
		}
		if (Timestamp.class.isAssignableFrom(type)) {
			value = mFunctionUtil.toString(value);
			try {
				if (((String) value).length() > 19) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					dateFormat.setTimeZone(mFunctionUtil.getTimeZone());
					set(propertyName, new Timestamp(dateFormat.parse((String) value).getTime()));
				} else {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					dateFormat.setTimeZone(mFunctionUtil.getTimeZone());
					set(propertyName, new Timestamp(dateFormat.parse((String) value).getTime()));
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal timestamp value: '" + value + "'");
			}
			return;
		}
		if (Double.class.isAssignableFrom(type)) {
			set(propertyName, mFunctionUtil.numberConverter(value));
			return;
		}
		if (String.class.isAssignableFrom(type)) {
			set(propertyName, mFunctionUtil.toString(value));
			return;
		}
		set(propertyName, value);
	}

	/**
	 * Atribui valor da propriedade numérica no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param format
	 *          formato de decimais e separador de milhar (primeiro caracter
	 *          determina o separador de decimais e o segundo caracter determina o
	 *          separador de milhar - exemplos: ".," => 9,999,999.99 / ",." =>
	 *          9.999.999,99)
	 * @param value
	 *          valor formatado da propriedade
	 */
	public void setNumericDisplay(String propertyName, String value, String format) {
		// TODO
	}

	/**
	 * Atribui valor da propriedade de data no formato externo de apresentação
	 * 
	 * @param propertyName
	 *          nome da propriedade da entidade
	 * @param timeformat
	 *          código de formatação da hora (vide opções em $ztime)
	 * @param value
	 *          valor formatado da propriedade
	 */
	public void setTimeDisplay(String propertyName, String value, int timeformat) {
		// TODO
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (mClassMappingProp prop : entityProperties.getProperties(entityName)) {
			builder.append(prop.getPropertyName()).append(": ").append(prop.getTypeDescription());
			if (prop.isPropertyPK()) {
				builder.append(" : ").append("PK");
			}
			if (!prop.isPropertyFK()) {
				builder.append(" = ").append(get(prop.getPropertyName()));
			}

			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * Valida o registro (instância) corrente (sem validar as entidades
	 * relacionadas), contemplando a validação dos campos obrigatórios e o formato
	 * dos valores.
	 * 
	 * @return booleano indicando se o registro foi validado (em caso de erros
	 *         gera uma exceção com todos os erros de validação encontrados)
	 */
	public boolean validate() {
		// TODO
		return false;
	}

	/**
	 * Valida o registro (instância) corrente
	 * 
	 * @param related
	 *          booleano indicando a necessidade de validar as entidades
	 *          relacionadas
	 * @return booleano indicando se o registro foi validado (em caso de erros
	 *         gera uma exceção com todos os erros de validação encontrados)
	 *         (todas as referências e relacionamentos desta instância são
	 *         consideradas como entidades relacionadas)
	 */
	public boolean validate(boolean related) {
		// TODO
		return false;
	}

	private String[] splitIdKey(String idKey) {
		return idKey.split("\\|\\|", -1);
	}

	DynamicEntity getDynamicEntity() {
		return dynamicEntity;
	}

	Object getIDValue() {
		return this.get(_ID);
	}

	void setDynamicEntity(DynamicEntity dynamicEntity) {
		this.dynamicEntity = dynamicEntity;
	}

}
