package $Library;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

import $Library.exceptions.*;

public class FieldObject {

	public Boolean $initialized = true;
	public Field field;
	public String name;
	
	public FieldObject(String name, Field field) {
		this.field = field;
		this.name = name;
	}

	public Object get(Object object) {
		try {
			return this.field.get(object);
		}
		catch (Exception e) {
			throw new RegisteredObjectException(e);
		}
	}
	
	public Annotation getAnnotation(Class<? extends Annotation> annotationClass) {
		return this.field.getAnnotation(annotationClass);
	}
	
	public Type getGenericType() {
		return this.field.getGenericType();
	}
	
	public Class<?> getType() {
		return this.field.getType();
	}

	public Boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return this.field.isAnnotationPresent(annotationClass);
	}

	public void set(Object object, Object value) {
		try {
			this.field.set(object,value);
		}
		catch (Exception e) {
			throw new RegisteredObjectException(e);
		}
	}

	public void setAccessible(Boolean flag) {
		this.field.setAccessible(flag);
	}
}
