package $Library;

import java.util.Map;
import java.util.TreeMap;
import java.lang.reflect.Field;

import mLibrary.*;
import $Library.exceptions.*;

public abstract class RegisteredObject extends mClass {

	private Boolean $modified = false;
	protected Map<String, FieldObject> $fields = new TreeMap<String, FieldObject>();
	
	public RegisteredObject() {
		this.$initializeFields(this.getClass());
	}

	public RegisteredObject(mContext context) {
		this();
		this.init(context);
	}

	public Object $Close() {
		return 1;
	}
	
	public Object $GetParameter(Object... args) {
		if (args.length > 0) {
			String fieldName = (String) args[0];
			try {
				Field field = this.getClass().getDeclaredField("p_"+fieldName);
				field.setAccessible(true);
				return field.get(this);
			}
			catch (Exception e) {
	      throw new RegisteredObjectException(e);
			}
		}
		return "";
	}
	
	public Boolean $IsModified() {
		return this.$modified;
	}
	
	public Object $New(Object... args) {
		try {
			mClass newObj = this.getClass().newInstance();
			newObj.init(this.m$);
			return newObj;
		}
		catch (Exception e) {
      throw new RegisteredObjectException(e);
		}
	}	

	public Object $ObjectModified() {
		return this.$modified;
	}

	public void $SetModified() {
		this.$SetModified(true);
	}

	public void $SetModified(Object value) {
		this.$modified = m$op.Logical(value);
	}

	public Object $ValidateObject(Object... args) {
		//TODO NÃO IMPLEMENTADO 
		return 1;
	}

	public Object get(String propertyName) {
		FieldObject field = this.$fields.get(propertyName);
		if ((field == null) && (propertyName.contains("."))) {
			Object object = this.get(propertyName.substring(0,propertyName.indexOf(".")));
			if (object instanceof RegisteredObject) {
				return ((RegisteredObject) object).get(propertyName.substring(propertyName.indexOf(".")+1));
			}
			else {
	      throw new RegisteredObjectException("Invalid reference: '"+propertyName+"'");
			}
		}
		if (field != null) {
			return this.get(field);
		}
		else {
      throw new RegisteredObjectException("Invalid property name: '"+propertyName+"'");
		}
	}

	public void set(String propertyName, Object value) {
		FieldObject field = this.$fields.get(propertyName);
		if ((field == null) && (propertyName.contains("."))) {
			Object object = this.get(propertyName.substring(0,propertyName.indexOf(".")));
			if (object instanceof RegisteredObject) {
				((RegisteredObject) object).set(propertyName.substring(propertyName.indexOf(".")+1),value);
			}
			else {
	      throw new RegisteredObjectException("Invalid reference: '"+propertyName+"'");
			}
			return;
		}
		if (field != null) {
			this.set(field,value);
		}
		else {
      throw new RegisteredObjectException("Invalid property name: '"+propertyName+"'");
		}
	}

	protected void $initializeFields(@SuppressWarnings("rawtypes")Class classType) {
		this.$initializeFields(classType,"");
	}	

	private void $initializeFields(@SuppressWarnings("rawtypes")Class classType, String namePrefix) {
		try{
		Field[] classFields = classType.getDeclaredFields();
		for (int i=0;i<classFields.length;i++) {
			if ((classFields[i].getName().startsWith("$")) || (classFields[i].getName().startsWith("m$")) || (classFields[i].getName().startsWith("p_"))) {
				continue;
			}
			if (!this.$fields.containsKey(namePrefix+classFields[i].getName())) {
				this.$fields.put(namePrefix+classFields[i].getName(), new FieldObject(namePrefix+classFields[i].getName(),classFields[i]));
				if (SerialObject.class.isAssignableFrom(classFields[i].getType())) {
					try {
						classFields[i].setAccessible(true);
						Object fieldValue = classFields[i].getType().newInstance();
						if (this.m$ != null) {
							((SerialObject) fieldValue).init(this.m$);
						}
						classFields[i].set(this,fieldValue);
						this.$initializeFields(classFields[i].getType(),classFields[i].getName()+".");
					}
					catch (Exception e) {
			      throw new RegisteredObjectException(e);
					}
				}
				if ($List.class.isAssignableFrom(classFields[i].getType())) {
					try {
						classFields[i].setAccessible(true);
						Object fieldValue = classFields[i].getType().newInstance();
						if (this.m$ != null) {
							(($List<?>) fieldValue).init(this.m$);
						}
						(($List<?>) fieldValue).$initializeObject(this);
						classFields[i].set(this,fieldValue);
					}
					catch (Exception e) {
			      throw new RegisteredObjectException(e);
					}
				}
			}
		}
		if (classType.getSuperclass() != null) {
			this.$initializeFields(classType.getSuperclass());
		}
		return;
		}catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	protected void $initializeField(FieldObject field) {
		return;
	}

	protected Object get(FieldObject field) {
		this.$initializeField(field);
		Object value = this.getValue(field);
		if (value == null) {
			return "";
		}
		return value;
	}	

	protected Object getValue(FieldObject field) {
		try {
			field.setAccessible(true);
			if (field.name.contains(".")) {
				Object object = this.get(field.name.substring(0,field.name.indexOf(".")));
				if (object instanceof RegisteredObject) {
					return ((RegisteredObject) object).get(field.name.substring(field.name.indexOf(".")+1));
				}
				else {
		      throw new RegisteredObjectException("Invalid reference: '"+field.name+"'");
				}
			}
			Object value = field.get(this);
			return value;
		}
		catch (Exception e) {
      throw new RegisteredObjectException(e);
		}
	}

	protected void set(FieldObject field, Object value) {
		this.$initializeField(field);
		if ((value instanceof String) && ((String)value).isEmpty()) {
			value = null;
		}
		this.setValue(field,value);
	}

	protected void setValue(FieldObject field, Object value) {
		try {
			this.$SetModified();
			field.setAccessible(true);
			if (field.name.contains(".")) {
				Object object = this.get(field.name.substring(0,field.name.indexOf(".")));
				if (object instanceof RegisteredObject) {
					((RegisteredObject) object).set(field.name.substring(field.name.indexOf(".")+1),value);
				}
				else {
		      throw new RegisteredObjectException("Invalid reference: '"+field.name+"'");
				}
				return;
			}
			if (value == null) {
				field.set(this,null);
				return;
			}
			if (Boolean.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toBoolean(value));
			}
			else if (Double.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toDouble(value));
			}
			else if (Float.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toDouble(value).floatValue());
			}
			else if (Integer.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toInt(value));
			}
			else if (Long.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toLong(value));
			}
			else if (String.class.isAssignableFrom(field.getType())) {
				field.set(this,m$util.toString(value));
			}
			else {
				field.set(this,value);
			}
		}
		catch (Exception e) {
      throw new RegisteredObjectException(e);
		}
	}
}
