package mLibrary;

import java.lang.reflect.Field;

public class mParameter {

	private Field parameterField;

	public mParameter(Field parameterField) {
		this.parameterField = parameterField;
	}

	public Object get(){
		Object retorno;
		try {
			retorno = parameterField.get(null);
		} catch (IllegalArgumentException e) {
			retorno = "";
		} catch (IllegalAccessException e) {
			retorno = "";;
		}
		return (retorno==null)?"":retorno;
	}

}
