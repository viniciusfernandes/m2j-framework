package ORM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.dynamic.DynamicTypeBuilder;
import org.eclipse.persistence.jpa.dynamic.JPADynamicHelper;
import org.eclipse.persistence.jpa.dynamic.JPADynamicTypeBuilder;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;

import ORM.exceptions.mEntityLoadingException;
import br.com.innovatium.mumps2java.todo.TODO;

import com.google.gson.Gson;

final class mEntityTypeLoader {

	private static mEntityTypeLoader loader;

	static mEntityTypeLoader getInstance(String relationalUnitName, String metadataUnitName) {
		if (loader == null) {
			loader = new mEntityTypeLoader(JPAFactory.createJPAFactory(relationalUnitName),
					JPAFactory.createJPAFactory(metadataUnitName));
		}
		return loader;
	}

	private final Map<String, DynamicTypeBuilder> builderMap = new HashMap<>();

	private final DynamicClassLoader dynamicLoader = new DynamicClassLoader(mEntityTypeLoader.class.getClassLoader());

	/*
	 * Atributo utilizado para cache de quais entidades ja foram carregadas. Isso
	 * eh necessario para a carga das classes com relacionado de FK.
	 */
	private final Map<String, DynamicType> dynamicTypeMap = new HashMap<>(50);
	private final mEntityProperties entityProperties;

	private final JPADynamicHelper helper;

	private final EntityManager metadataEM;

	private List<DynamicType> newTypes = new ArrayList<DynamicType>();

	private mEntityTypeLoader(EntityManagerFactory relationalEMF, EntityManagerFactory metadataEMF) {
		this.metadataEM = metadataEMF.createEntityManager();
		this.helper = new JPADynamicHelper(relationalEMF);

		entityProperties = mEntityProperties.getInstance();
	}

	private String generateListName(mClassMapping mapping, mClassMappingProp prop) {
		return generateListName(mapping.getEntityName(), prop.getPropertyName());
	}

	private DynamicTypeBuilder getTypeBuilder(String className, String sqlTableName) {
		String entityName = null;
		if (className != null) {
			entityName = className.replace('.', '_');
		}
		DynamicTypeBuilder typeBuilder = null;
		if (!builderMap.containsKey(entityName)) {
			Class<?> dynamicClass = dynamicLoader.createDynamicClass("orm.entity." + entityName);

			typeBuilder = new JPADynamicTypeBuilder(dynamicClass, null, sqlTableName);
			builderMap.put(entityName, typeBuilder);
		}
		else {
			typeBuilder = builderMap.get(entityName);
		}
		return typeBuilder;
	}

	/*
	 * Esse metodo foi criado para desconsiderar a situacao em que nao temos o
	 * mapeamento das FKs no banco de dados. Isso foi necessario para darmos
	 * inicio os testes de implementacoes.
	 */
	private void loadType(mClassMapping mapping, String parentClassName, boolean isAutoRefFK) {

		if (parentClassName == null) {
			this.newTypes = new ArrayList<DynamicType>();
		}

		final String sqlTableName = mapping.getSqlTableName();

		DynamicTypeBuilder typeBuilder = getTypeBuilder(mapping.getClassName(), sqlTableName);

		/*
		 * Se for uma auto referencia nao precisamos carregar os campos pois eles ja
		 * foram carregados na iteracao anterior quando mapeamos a clase pai.
		 */
		if (!isAutoRefFK) {

			// Construindo as pks da entidade. Estamos assumindo que todas terao
			// a pk como sendo o ID dado por um string.
			typeBuilder.setPrimaryKeyFields(mapping.getPKSQLColumnNames());

			for (mClassMappingProp prop : mapping.getPropertyList()) {

				entityProperties.add(mapping.getEntityName(), prop);

				try {
					// Esse mapeamento sera comum a todos os tipo de campos. Decidimos
					// por
					// incluir esse tratamento pois em determinados blocos de codigo o
					// programador fara acesso ao valor de uma FK diretamente via get()
					// e
					// assim nao sera necessario carregar todo o relacionamento da FK.
					// Padronizamos os nomes da seguinte maneira: <nomeDoCampo> contera
					// o
					// valor da chave, ja <nomeDoCampoObj> tera o relacionamento
					// completo.
					// Abaixo as FKs recebem um outro tratamento especial para refletir
					// os
					// relacionamentos do ORM.
					typeBuilder.addDirectMapping(prop.getPropertyName(), prop.getType(), prop.getSqlColumnName());

				}
				catch (Exception e) {
					throw new mEntityLoadingException(

					"Fail to map the class: \"" + sqlTableName + "\", the class of the FK: \"" + prop.getClassNameFK()
							+ "\", the property: \"" + prop.getPropertyName() + "\" and the sql column: \"" + prop.getSqlColumnName()
							+ "\"", e);
				}

				// Faremos o mapeamento das FKs no bloco de codigo seguinte.
				if (prop.isPropertyFK()) {

					mClassMapping mappingFK = findClassMapping(prop.getClassNameFK());

					/*
					 * Codigo temporario para tratamento do nao carregamento das FKs. Se
					 * uma classe ainda nao foi mapeada daremos continuidade aos testes do
					 * ORM. Adiante lancaremos uma exception.
					 */
					if (mappingFK == null) {
						// throw new
						// mEntityLoadingException("Fail to load the entity \""+mapping.getClassName()+"\""+" because its FK \""+prop.getClassNameFK()+"\" does not have mapping definitions");
						continue;
					}

					String parentName = mapping.getClassName();

					if (!builderMap.containsKey(mappingFK.getEntityName())) {
						loadType(mappingFK, parentName, false);
					}

					// Esse bloco sera executado assim que for finalizada o mapeamento
					// dos
					// campos da FK desejada atraves da recursividade, sendo assim,
					// finalizado o mapeamento dos campos das FKs, restara efetuar a
					// amarracao entre as classes.
					if (!mapping.isAutoRef(mappingFK.getClassName())) {
						try {

							// Somente para foreign keys com referência via primary key (FK com referência via Id não possuem mapeamento no EclipseLink)
							if (!prop.isPropertyIdFK()) {

								OneToOneMapping oneToOne = typeBuilder.addOneToOneMapping(generateObjName(prop.getPropertyName()),
										builderMap.get(mappingFK.getEntityName()).getType(),
										prop.getPropertyListFK().toArray(new String[] {}));

								oneToOne.setIsReadOnly(true);
								oneToOne.setIsLazy(true);
								oneToOne.setCascadePersist(true);
								oneToOne.setCascadeMerge(true);
								oneToOne.setCascadeRemove(true);

								DynamicTypeBuilder typeBuilderFK = getTypeBuilder(mappingFK.getEntityName(),
										mappingFK.getSqlTableName());

								// Convencionamos que o nome da lista gerada no relacionamento
								// de
								// "1 para muitos" sera:
								// <nome da classe que referencia>
								// + <nome da propriedade referenciada>
								// + <a palavra list>
								// Esse padrao tambem sera adotado no caso das
								// autoreferencias.
								String propertyListName = generateListName(mapping, prop);
								System.out.println("propertyListName: "+propertyListName);

								typeBuilderFK.addOneToManyMapping(propertyListName, typeBuilder.getType(), prop.getPropertyListFK()
										.toArray(new String[] {}));

							}

						}
						catch (Exception e) {
							throw new mEntityLoadingException("Fail to map the class: \"" + sqlTableName
									+ "\", the class of the FK: \"" + prop.getClassNameFK() + "\", the property: \""
									+ prop.getPropertyName() + "\" and the sql column: \"" + prop.getSqlColumnName() + "\"", e);
						}

					}
					// ELSE DA VERIFICACAO DO AUTORELACIONAMENTO
					else {
						try {

							// Somente para foreign keys com referência via primary key (FK com referência via Id não possuem mapeamento no EclipseLink)
							if (!prop.isPropertyIdFK()) {

								// Estamos mapeando a situacao de autoreferencia entre tabelas
								// por isso utilizamos o mesmo typebuilder.
								OneToOneMapping oneToOne = typeBuilder.addOneToOneMapping(generateObjName(prop.getPropertyName()),
										typeBuilder.getType(), prop.getPropertyListFK().toArray(new String[] {}));

								oneToOne.setIsReadOnly(true);
								oneToOne.setIsLazy(true);
								oneToOne.setCascadePersist(true);
								oneToOne.setCascadeMerge(true);
								oneToOne.setCascadeRemove(true);

								String propertyListName = generateListName(mapping, prop);
								OneToManyMapping oneToMany = typeBuilder.addOneToManyMapping(propertyListName, typeBuilder.getType(),
										prop.getPropertyListFK().toArray(new String[] {}));

								oneToMany.setIsReadOnly(true);

							}

						}
						catch (Exception e) {
							throw new mEntityLoadingException("Fail to map the FK class: \"" + parentClassName
									+ "\", the property: \"" + Arrays.deepToString(prop.getPropertyListFK().toArray(new String[] {}))
									+ "\" and the sql column: \"" + prop.getSqlColumnName() + "\"", e);
						}
					}
				}
			}

			DynamicType type = typeBuilder.getType();
			this.newTypes.add(type);

			/*
			 * try { helper.addTypes(false, false, type); } catch (Exception e) {
			 * throw new mEntityLoadingException(
			 * "Fail to publish some entity type into the jpadynamic mapping helper. Mapping detail is: "
			 * + type.toString(), e); }
			 */

			if (parentClassName == null) {
				try {
					helper.addTypes(false, false, this.newTypes.toArray(new DynamicType[] {}));
				}
				catch (Exception e) {
					throw new mEntityLoadingException(
							"Fail to publish some entity type into the jpadynamic mapping helper. Mapping detail is: "
									+ type.toString(), e);
				}
			}

			dynamicTypeMap.put(mapping.getEntityName(), type);

		}

	}

	mClassMapping findClassMapping(String className) {
		StringBuilder select = new StringBuilder();
		select.append("select value_, valueclob_ from WWWCLASSMAPPING where key_ = '").append(className).append("'");

		try {
			String jsonString = null;

			Object[] result = (Object[]) metadataEM.createNativeQuery(select.toString()).getSingleResult();

			jsonString = (String) result[0];

			if (jsonString == null) {
				jsonString = (String) result[1];
			}

			return new Gson().fromJson(jsonString, mClassMapping.class);
		}
		catch (NoResultException e) {
			return null;
		}
	}

	String generateListName(String entityName, String propertyName) {
		return entityName.replace("_", "") + propertyName + "List";
	}

	String generateObjName(String propertyName) {
		return propertyName + "Obj";
	}

	DynamicType loadType(String className) {
		String entityName = null;
		if (className != null) {
			entityName = className.replace('.', '_');
		}
		/*
		 * Verificando se a entidade ja foi carregada. Utilizando o classname aqui
		 * para ficar de acordo com a forma que empregados para verificar o cache de
		 * uma FK.
		 */
		DynamicType type = dynamicTypeMap.get(entityName);
		if (type != null) {
			return type;
		}
		
		final mClassMapping mapping = findClassMapping(className);

		if (mapping == null) {
			throw new mEntityLoadingException("This entity is not defined. The entity name " + entityName
					+ " could not be loaded. Please, verify the classe definition into the database.");
		}

		loadType(mapping, null, false);
		
		builderMap.clear();
		return dynamicTypeMap.get(entityName);
	}

	@TODO(author = "vinicius", date = "05/11/2014", description = "Acredito que devemos fazer o reload de todas as FKS pois verificamos intabiliades nas recargas durante os testes de implementacao do ORM V2")
	DynamicType reloadType(String className) {
		if (className != null) {
			String entityName = className.replace('.', '_');
			//List<mClassMappingProp> fkList = entityProperties.getFKList(entityName);
			
			builderMap.remove(entityName);
			dynamicTypeMap.remove(entityName);
		}
		return loadType(className);
	}
}
