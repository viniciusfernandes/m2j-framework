package $Library;

import $Library.exceptions.PersistentObjectException;
import $Library.exceptions.RegisteredObjectException;
import $Library.persistent.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.mFunctionUtil;
import util.DataStructureUtil;
import mLibrary.m$util;
import mLibrary.mContext;
import ORM.mEntity;
import ORM.mJDBCObject;

public abstract class Persistent extends RegisteredObject {

	public Persistent() {
		this.$initializeFields(this.getClass());
	}

	public Persistent(mContext context) {
		this();
		this.init(context);
	}

	protected static String $getEntityMapping(Class persistentClass) {
		String className = persistentClass.getCanonicalName();
		EntityMapping classAnnotation = (EntityMapping) persistentClass.getAnnotation(EntityMapping.class);
		if (classAnnotation != null) {
			if (!classAnnotation.name().isEmpty()) {
				className = classAnnotation.name();
			}
		}
		return className.replace("User.", "");
	}

	protected mEntity $entity;

	@TransientField
	protected String globalResult;

	public Object $ExistsId(Object... args) {
		if (args.length <= 0) {
			return false;
		}
		this.$initializeEntity();
		return this.$entity.existsId(m$util.toString(args[0]));
	}

	public String $Id(Object... args) {
		if (this.$entity == null) {
			return "";
		}
		String Id = this.$entity.getId();
		if (Id == null) {
			return "";
		}
		return Id;
	}

	public Object $OpenId(Object... args) {
		if (args.length <= 0) {
			return "";
		}
		// REMOVE ANTIGA IMPLEMENTAÃ‡ÃƒO
		// ************
		/*
		 * String id = args[0].toString(); mJDBCObject NMO = new mJDBCObject();
		 * Class clazz = this.getClass(); while (clazz!=null) { if
		 * (clazz.getSuperclass().isAssignableFrom(Persistent.class)) { break; }
		 * else { clazz = clazz.getSuperclass(); } } String clazzName =
		 * Persistent.$getEntityMapping(clazz); globalResult =
		 * NMO.loadRecord(m$,clazzName,id); if ((globalResult==null) ||
		 * (globalResult.isEmpty())) { return ""; }
		 */
		// Integer concurrency =
		// args.length>=2?Integer.valueOf(args[1].toString()):-1;
		// $Library.Status sc = new
		// $Library.Status(args.length>=3?mFunctionUtil.integerConverter(args[2]):1);
		// ************

		this.$initializeEntity();
		if (!this.$entity.openId(m$util.toString(args[0]))) {
			return "";
		}
		this.$loadEntity();
		this.$SetModified(false);
		return this;
	}

	public Object $Reload(Object... args) {
		this.$initializeEntity();
		if (!this.$entity.reload()) {
			return 0;
		}
		this.$loadEntity();
		this.$SetModified(false);
		return 1;
	}
	
	public Object $Save(Object... args) {
		Boolean related = true;
		if (args.length > 0) {
			related = m$util.toBoolean(args[0]);
		}

		this.$initializeEntity();
		this.$saveEntity(related);
		this.$entity.save();
		this.$SetModified(false);
		return 1;
	}

	public Object $DeleteId(Object... args) {
		if (args.length <= 0) {
			return "";
		}

		this.$initializeEntity();
		if (!this.$entity.removeId(m$util.toString(args[0]))) {
			return 0;
		}
		return 1;
	}

	protected void $initializeEntity() {
		if (this.$entity == null) {
			String className = this.getClass().getCanonicalName();
			EntityMapping classAnnotation = this.getClass().getAnnotation(EntityMapping.class);
			if (classAnnotation != null) {
				if (!classAnnotation.name().isEmpty()) {
					className = classAnnotation.name();
				}
			}
			this.$entity = new mEntity(className);
		}
	}

	protected void $loadEntity() {
		for (Map.Entry<String, FieldObject> entry : $fields.entrySet()) {
			FieldObject field = entry.getValue();
			if (field.isAnnotationPresent(TransientField.class)) {
				continue;
			}
			if (field.isAnnotationPresent(CalculatedField.class)) {
				continue;
			}
			if (SerialObject.class.isAssignableFrom(field.getType())) {
				try {
					if (this.getValue(field) == null) {
						this.setValue(field, field.getType().newInstance());
					}
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			if (Persistent.class.isAssignableFrom(field.getType())) {
				try {
					field.$initialized = false;
					this.setValue(field, null);
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			if ($List.class.isAssignableFrom(field.getType())) {
				try {
					$List<?> fieldValue = ($List<?>) field.getType().newInstance();
					fieldValue.$initializeObject(this);
					field.$initialized = false;
					this.setValue(field, fieldValue);
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			this.set(field, this.$entity.getM(entry.getKey().replace('.', '_')));
		}
	}

	protected void $saveEntity(Boolean related) {
		for (Map.Entry<String, FieldObject> entry : $fields.entrySet()) {
			FieldObject field = entry.getValue();
			if (field.isAnnotationPresent(TransientField.class)) {
				continue;
			}
			if (field.isAnnotationPresent(CalculatedField.class)) {
				continue;
			}
			if (field.isAnnotationPresent(SqlComputedField.class)) {
				continue;
			}
			if (SerialObject.class.isAssignableFrom(field.getType())) {
				try {
					if (this.getValue(field) == null) {
						this.setValue(field, field.getType().newInstance());
					}
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			if (Persistent.class.isAssignableFrom(field.getType())) {
				try {
					if (field.$initialized) {
						$savePersistentField(field, related);
					}
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			if (RelationshipObject.class.isAssignableFrom(field.getType())) {
				try {
					if (field.$initialized) {
						$saveRelationshipField(field, related);
					}
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			if ($List.class.isAssignableFrom(field.getType())) {
				try {
					if (field.$initialized) {
						$saveListField(field, related);
					}
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
				continue;
			}
			this.$entity.setM(entry.getKey().replace('.', '_'), this.getValue(field));
		}
	}

	protected void $initializeField(FieldObject field) {
		if (field.$initialized) {
			return;
		}
		field.$initialized = true;
		if (Persistent.class.isAssignableFrom(field.getType())) {
			this.$loadPersistentField(field);
			return;
		}
		if (RelationshipObject.class.isAssignableFrom(field.getType())) {
			this.$loadRelationshipField(field);
			return;
		}
		if ($List.class.isAssignableFrom(field.getType())) {
			this.$loadListField(field);
			return;
		}
	}

	protected void $loadListField(FieldObject field) {
		$List<?> fieldValue = ($List<?>) this.getValue(field);
		if (fieldValue == null) {
			fieldValue = new $List<>(this);
		}
		else {
			fieldValue.$initializeObject(this);
		}
		String fieldListValue = (String) this.$entity.get(field.name.replace('.', '_'));
		if ((fieldListValue == null) || (fieldListValue.isEmpty())) {
			return;
		}
		String[] fieldArrayValue = fieldListValue.split("\\" + DataStructureUtil.DELIMITER, -1);
		if (!(field.getGenericType() instanceof ParameterizedType)) {
			throw new PersistentObjectException("Invalid list type: '" + field.name + "'");
		}
		ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
		Class fieldClass = (Class) fieldType.getActualTypeArguments()[0];
		if (Persistent.class.isAssignableFrom(fieldClass)) {
			for (String value : fieldArrayValue) {
				try {
					Persistent fieldValueInstance = (Persistent) fieldClass.newInstance();
					if (this.m$ != null) {
						fieldValueInstance.init(this.m$);
					}
					fieldValueInstance.$OpenId(value);
					fieldValue.Insert(fieldValueInstance);
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
			}
		}
		else {
			for (String value : fieldArrayValue) {
				fieldValue.Insert(value);
			}
		}
	}

	protected void $saveListField(FieldObject field, Boolean related) {
		$List<?> fieldValue = ($List<?>) this.getValue(field);
		if ((fieldValue == null) || (fieldValue.$list.size() == 0)) {
			this.$entity.set(field.name.replace('.', '_'), null);
			return;
		}
		if (!(field.getGenericType() instanceof ParameterizedType)) {
			throw new PersistentObjectException("Invalid list type: '" + field.name + "'");
		}
		ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
		Class fieldClass = (Class) fieldType.getActualTypeArguments()[0];
		StringBuilder fieldListValue = new StringBuilder("");
		if (Persistent.class.isAssignableFrom(fieldClass)) {
			for (Object value : fieldValue.$list) {
				if (value instanceof Persistent) {
					if (related) {
						if (((Persistent) value).$IsModified()) {
							((Persistent) value).$SetModified(false);
							if (!mFunctionUtil.booleanConverter(((Persistent) value).$Save(related))) {
								((Persistent) value).$SetModified(true);
							}
						}
					}
					String valueId = ((Persistent) value).$Id();
					if ((valueId != null) && (!valueId.isEmpty())) {
						fieldListValue.append(((fieldListValue.length() == 0) ? "" : DataStructureUtil.DELIMITER)).append(valueId);
					}
				}
			}
		}
		else {
			for (Object value : fieldValue.$list) {
				if (value != null) {
					fieldListValue.append(((fieldListValue.length() == 0) ? "" : DataStructureUtil.DELIMITER)).append(
							mFunctionUtil.toString(value));
				}
			}
		}
		this.$entity.set(field.name.replace('.', '_'), fieldListValue.toString());
	}

	protected void $loadPersistentField(FieldObject field) {
		this.$initializeEntity();
		if (this.$entity.get(field.name.replace('.', '_')) == null) {
			this.setValue(field, null);
		}
		else {
			mEntity entity = this.$entity.getEntityObj(Persistent.$getEntityMapping(field.getType()),
					field.name.replace('.', '_'));
			if (entity == null) {
				this.setValue(field, null);
			}
			else {
				try {
					Persistent fieldValue = (Persistent) field.getType().newInstance();
					if (this.m$ != null) {
						fieldValue.init(this.m$);
					}
					fieldValue.$entity = entity;
					fieldValue.$loadEntity();
					this.setValue(field, fieldValue);
				}
				catch (Exception e) {
					throw new PersistentObjectException(e);
				}
			}
		}
	}

	protected void $savePersistentField(FieldObject field, Boolean related) {
		if (this.getValue(field) != null) {
			Persistent fieldObject = (Persistent) this.getValue(field);
			if (related) {
				if (fieldObject.$IsModified()) {
					fieldObject.$SetModified(false);
					if (!mFunctionUtil.booleanConverter(fieldObject.$Save(related))) {
						fieldObject.$SetModified(true);
					}
				}
			}
			if (fieldObject.$entity != null) {
				this.$entity.setM(field.name.replace('.', '_'), fieldObject.$entity.getId());
				this.$entity.setEntityObj(field.name.replace('.', '_'), fieldObject.$entity);
			}
			else {
				this.$entity.setM(field.name.replace('.', '_'), null);
				this.$entity.setEntityObj(field.name.replace('.', '_'), null);
			}
		}
		else {
			this.$entity.setM(field.name.replace('.', '_'), null);
			this.$entity.setEntityObj(field.name.replace('.', '_'), null);
		}
	}

	protected void $loadRelationshipField(FieldObject field) {
		$Relationship<?> fieldValue = ($Relationship<?>) this.getValue(field);
		if (fieldValue == null) {
			fieldValue = new $Relationship<>(this);
		}
		else {
			fieldValue.$initializeObject(this);
		}
		if (!(field.getGenericType() instanceof ParameterizedType)) {
			throw new PersistentObjectException("Invalid relationship type: '" + field.name + "'");
		}
		RelationshipField fieldAnnotation = (RelationshipField) field.getAnnotation(RelationshipField.class);
		if (fieldAnnotation == null) {
			throw new PersistentObjectException("Invalid relationship field: '" + field.name + "'");
		}
		if ((fieldAnnotation.inverse() == null) || (fieldAnnotation.inverse().isEmpty())) {
			throw new PersistentObjectException("Invalid relationship inverse name: '" + field.name + "'");
		}
		ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
		Class<?> fieldClass = (Class<?>) fieldType.getActualTypeArguments()[0];
		List<mEntity> entityList = this.$entity.getEntityDirectList(Persistent.$getEntityMapping(fieldClass),
				fieldAnnotation.inverse());
		fieldValue.$setEntityList(entityList);

		final List<mEntity> results = new ArrayList<>(entityList);
		for (mEntity entity : results) {
			try {
				Persistent fieldValueInstance = (Persistent) fieldClass.newInstance();
				if (this.m$ != null) {
					fieldValueInstance.init(this.m$);
				}
				fieldValueInstance.$entity = entity;
				fieldValueInstance.$loadEntity();
				fieldValue.Insert(fieldValueInstance);
			}
			catch (Exception e) {
				throw new PersistentObjectException(e);
			}
		}
	}

	protected void $saveRelationshipField(FieldObject field, Boolean related) {
		if (!related) {
			return;
		}
		$Relationship<?> fieldValue = ($Relationship<?>) this.getValue(field);
		if (fieldValue == null) {
			return;
		}
		for (Object fieldValueObject : fieldValue.$list) {
			Persistent fieldValueInstance = (Persistent) fieldValueObject;
			if (fieldValueInstance.$IsModified()) {
				fieldValueInstance.$SetModified(false);
				if (!mFunctionUtil.booleanConverter(fieldValueInstance.$Save(related))) {
					fieldValueInstance.$SetModified(true);
				}
			}
		}
	}

	public Object $AcquireLock(Object... args) {
		// TODO
		return 1;
	}

	public Object $ReleaseLock(Object... args) {
		// TODO
		return 1;
	}

	public Object $LockId(Object... args) {
		// TODO
		return 1;
	}

	public Object $UnlockId(Object... args) {
		// TODO
		return 1;
	}

	public Object $DowngradeConcurrency(Object... args) {
		// Sem implementação em Java
		return 1;
	}

	public Object $UpgradeConcurrency(Object... args) {
		// Sem implementação em Java
		return 1;
	}
}
