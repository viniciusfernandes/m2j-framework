package ORM;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;

import br.com.innovatium.mumps2java.todo.REVIEW;

public class mEntityManager {

	public static mEntityManager getInstance() {
		return em;
	}

	private final static mEntityManager em;

	static {
		em = new mEntityManager();
	}

	private final mEntityTypeLoader entityLoader;
	private final EntityManager relationalEM;

	private mEntityManager() {
		relationalEM = JPAFactory.createJPAManager("relational");
		entityLoader = mEntityTypeLoader.getInstance("relational", "metadata");
	}

	public void execNative(String statement) {
		relationalEM.createNativeQuery(statement).executeUpdate();
	}

	public boolean exists(String entityName, List<String> pkNames, Object[] pkValues) {
		String entityNameAlias = "_" + entityName;
		StringBuilder select = new StringBuilder("select count (").append(entityNameAlias).append(") from ");
		select.append(entityName).append(" ").append(entityNameAlias).append(" where ");

		settingPkNames(entityName, select, pkNames);

		Query query = relationalEM.createQuery(select.toString());
		settingPkValues(query, pkValues);

		Long count = (Long) query.getSingleResult();
		return count > 0;
	}

	String generateListName(String className, String propertyName) {
		return entityLoader.generateListName(className.replace('.', '_'), propertyName);
	}

	String generateObjName(String propertyName) {
		return entityLoader.generateObjName(propertyName);
	}

	public Object getValue(String entityName, String propertyName, List<String> pkNames, Object[] pkValues) {
		StringBuilder select = new StringBuilder();
		select.append("select ").append(propertyName).append(" from ").append(entityName).append(" where ");

		settingPkNames(entityName, select, pkNames);

		Query query = relationalEM.createQuery(select.toString());
		settingPkValues(query, pkValues);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	void initialize(String... entityNames) {
		for (String name : entityNames) {
			entityLoader.loadType(name);
		}
	}

	@SuppressWarnings("unchecked")
	List<mEntity> loadList(mEntity entity, String targetName, String propertyName) {
		propertyName = generateListName(targetName, propertyName);
		StringBuilder select = new StringBuilder();

		select.append("select l from ").append(entity.getEntityName()).append(" e ").append(" join fetch e.")
				.append(propertyName).append(" l where e = :entity");

		Query query = relationalEM.createQuery(select.toString()).setParameter("entity", entity.getDynamicEntity());

		List<DynamicEntity> results = query.getResultList();
		List<mEntity> entityList = new ArrayList<>(20);

		for (DynamicEntity dynamicEntity : results) {
			entityList.add(new mEntity(targetName, dynamicEntity));
		}
		return entityList;
	}

	@SuppressWarnings("unchecked")
	public List<mEntity> loadList(String entityName, String propertyName, Object propertyValue) {

		StringBuilder select = new StringBuilder();
		select.append("select e from ").append(entityName).append(" e ").append(" where ").append(" e.")
				.append(propertyName).append(" =:").append("fk");

		try {

			final List<mEntity> entityList = new ArrayList<>(50);
			final List<DynamicEntity> results = relationalEM.createQuery(select.toString()).setParameter("fk", propertyValue)
					.getResultList();

			for (DynamicEntity dynamicEntity : results) {
				// Aqui na ha problemas ao passar o entity name pois o nome de
				// nenhuma
				// entidade estara definido com o caracter ".", sendo assim, o
				// replace
				// do construtor nao tera efeito algum nesse parametro.
				entityList.add(new mEntity(entityName, dynamicEntity));
			}
			return entityList;
		} catch (NonUniqueResultException | NoResultException e) {
			return null;
		}
	}

	public List<mEntity> loadReverseList(mEntity entity, String targetClassName, String propertyName) {
		return loadList(entity, targetClassName, propertyName);
	}

	public mEntity loadReverseObj(mEntity entity, String targetClassName, String propertyName) {
		StringBuilder select = new StringBuilder();
		select.append("select t.").append(propertyName).append(" from ").append(targetClassName).append(" t ");
		select.append(" where t.").append(propertyName).append(" =:entity");
		DynamicEntity result = (DynamicEntity) relationalEM.createQuery(select.toString())
				.setParameter("entity", entity.getDynamicEntity()).getSingleResult();
		return new mEntity(targetClassName, result);
	}

	public DynamicType loadType(String className) {
		return entityLoader.loadType(className);
	}

	public mEntity merge(mEntity entity) {
		DynamicEntity dynamicEntity = entity.getDynamicEntity();
		dynamicEntity = relationalEM.merge(dynamicEntity);
		relationalEM.flush();
		relationalEM.refresh(dynamicEntity);
		entity.setDynamicEntity(dynamicEntity);
		return entity;
	}

	public boolean open(mEntity entity, List<String> pkNames, Object[] pkValues) {
		String entityName = entity.getEntityName();
		if (pkNames == null || pkValues == null || pkNames.size() != pkValues.length) {
			throw new IllegalArgumentException("Primery key names and values of " + entity.getEntityName()
					+ " must not be empty and must have the same elements number. The pk names have " + pkNames.size()
					+ " element(s) and the pk values have " + pkValues.length + " element(s).");
		}

		StringBuilder select = new StringBuilder();
		select.append("select ").append("_").append(entityName).append(" from ").append(entityName).append(" _")
				.append(entityName).append(" where ");

		settingPkNames(entityName, select, pkNames);

		Query query = relationalEM.createQuery(select.toString());
		settingPkValues(query, pkValues);

		try {
			entity.setDynamicEntity((DynamicEntity) query.getSingleResult());
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public DynamicEntity persiste(DynamicEntity dynamicEntity) {
		relationalEM.persist(dynamicEntity);
		return dynamicEntity;
	}

	public void refresh(DynamicEntity entity) {
		relationalEM.refresh(entity);
	}

	void reinitialize(String... entityNames) {
		for (String name : entityNames) {
			entityLoader.reloadType(name);
		}
	}

	public DynamicType reloadType(String className) {
		return entityLoader.reloadType(className);
	}

	public void remove(mEntity entity) {
		StringBuilder delete = new StringBuilder();
		delete.append("delete from ").append(entity.getEntityName()).append(" e where e =:entity");
		relationalEM.createQuery(delete.toString()).setParameter("entity", entity.getDynamicEntity()).executeUpdate();
	}

	@REVIEW
	private void settingPkNames(String entityName, StringBuilder builder, List<String> pkNames) {
		int length = pkNames.size();
		int index = length - 1;

		for (int i = 0; i < length; i++) {
			builder.append("_").append(entityName).append(".").append(pkNames.get(i)).append("=:").append("pk").append(i);
			if (i < index) {
				builder.append(" and ");
			}
		}
	}

	private void settingPkValues(Query query, Object[] pkValues) {
		for (int i = 0; i < pkValues.length; i++) {
			query.setParameter("pk" + i, pkValues[i]);
		}
	}
}