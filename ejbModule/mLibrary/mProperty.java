package mLibrary;

import $Library.$List;

import java.lang.reflect.Field;

import $Library.*;
import mLibrary.exceptions.*;

public class mProperty {

	Field field;
	String name;
	Object object;

	public mProperty(Object object, String fieldName) {
		this.object = object;
		this.name = fieldName;
		if (!(object instanceof RegisteredObject)) {
			Class clazz = object.getClass();
			while (this.field==null || !clazz.getName().equals("Object")) {
				try {
					this.field = clazz.getDeclaredField(fieldName);
					if (this.field != null){
						break;
					}
				}
				catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
				catch (SecurityException e) {
					throw new PropertyException(e);
				}
			}
		}
	}

	public Object get() {
		if (object instanceof RegisteredObject) {
			return ((RegisteredObject) object).get(name);
		}
		else {
			try {
				field.setAccessible(true);
				Object value = field.get(object);
				if (value == null) {
					return "";
				}
				return value;
			} 
			catch (IllegalArgumentException e) {
				throw new PropertyException(e);
			} 
			catch (IllegalAccessException e) {
				throw new PropertyException(e);
			}
		}
	}

	public void kill() {
		throw new UnsupportedOperationException();
	}

	public mVar s$(Object... subs) {
		throw new UnsupportedOperationException();
	}

	public void set(Object value) {
		if (object instanceof RegisteredObject) {
			((RegisteredObject) object).set(name,value);
		}
		else {
			try {
				field.setAccessible(true);
				if ((value instanceof String) && ((String)value).isEmpty()) {
					field.set(object,null);
					return;
				}
				if (Boolean.class.isAssignableFrom(field.getType())) {
					field.set(object,m$util.toBoolean(value));
				}
				else if (Double.class.isAssignableFrom(field.getType())) {
					field.set(object,m$util.toDouble(value));
				}
				else if (Float.class.isAssignableFrom(field.getType())) {
					field.set(object,m$util.toDouble(value).floatValue());
				}
				else if (Integer.class.isAssignableFrom(field.getType())) {
					field.set(object,m$util.toInt(value));
				}
				else if (String.class.isAssignableFrom(field.getType())) {
					field.set(object,m$util.toString(value));
				}
				else {
					field.set(object,value);
				}
			} 
			catch (IllegalArgumentException e) {
				throw new PropertyException(e);
			} 
			catch (IllegalAccessException e) {
				throw new PropertyException(e);
			}
		}
	}
}
