package br.com.innovatium.mumps2java.dataaccess;

public enum SQLResolverType {
	MYSQL, ORACLE, POSTGRE;
	public static SQLResolverType getType(String description) {

		if (description == null) {
			throw new IllegalArgumentException(
					"The sql type description must not be null");
		}
		description = description.toUpperCase();
		for (SQLResolverType type : values()) {
			if(description.contains(type.toString())) {
				return type;
			}
		}
		
		throw new IllegalArgumentException(
				"There is no one sql type to this description "+description);
	}
}
