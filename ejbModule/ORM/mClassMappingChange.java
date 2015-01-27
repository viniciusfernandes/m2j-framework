package ORM;

import ORM.annotations.Data;
import ORM.annotations.DataDefinition;

public class mClassMappingChange {

	private final mClassMappingProp prop;
	private final String atributeName;
	private final String definition;

	public mClassMappingChange(mClassMappingProp prop) {
		this(prop, null, null);
	}

	public mClassMappingChange(mClassMappingProp prop, String atributeName, Data data) {
		this.prop = prop;
		this.atributeName = atributeName;
		this.definition = generateDefinition(data);
	}

	private String generateDefinition(Data data) {
		DataDefinition definition = data.definition();
		if (definition.equals(DataDefinition.TEXT) && prop.getPropertyLength() != null) {
			return "varchar2(" + prop.getPropertyLength() + ")";
		}

		if (definition.equals(DataDefinition.TEXT) && prop.getPropertyLength() == null) {
			return "varchar2(" + prop.getPropertyLength() + ")";
		}

		if (definition.equals(DataDefinition.REQUIRED)) {
			return "not null";
		}

		if (definition.equals(DataDefinition.NUMERIC)) {
			return "integer";
		}

		throw new IllegalArgumentException("Nao existe o tipo de dados desejado");
	}

	public String getAtributeName() {
		return atributeName;
	}

	public String getDefinition() {
		return definition;
	}

	public mClassMappingProp getProp() {
		return prop;
	}
}
