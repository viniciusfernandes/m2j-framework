package ORM;

import static ORM.annotations.DataDefinition.COLUMN;
import static ORM.annotations.DataDefinition.NUMERIC;
import static ORM.annotations.DataDefinition.REQUIRED;
import static ORM.annotations.DataDefinition.TEXT;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import ORM.annotations.Data;

/**
 * Estrutura de definição de mapeamento de propriedades de classes compiladas
 * 
 * @author Innovatium Systems (mosselaar)
 */
public class mClassMappingProp {

	public static Class<?> getType(String propertyType) {
		switch (propertyType) {
		// Campo tipo hidden no cache
		case "0":
			return String.class;
			// Campo tipo date cache
		case "1":
			return Date.class;
			// Campo tipo boolean no cache
		case "2":
			return String.class;
			// Campo tipo Memo no cache
		case "3":
			return String.class;
			// Campo tipo integer no cache
		case "4":
			return String.class;
			// Campo tipo password no cache
		case "5":
			return String.class;
			// Campo tipo texto no cache
		case "6":
			return String.class;
			// Campo tipo time no cache
		case "7":
			return Double.class;
			// Campo tipo currency no cache
		case "8":
			return Double.class;
			// Campo tipo contador no cache
		case "9":
			return String.class;
			// Campo tipo file name no cache
		case "10":
			return String.class;
			// Campo tipo imagem no cache
		case "11":
			return String.class;
			// Campo tipo float no cache
		case "12":
			return Double.class;
			// Campo tipo formato ip no cache
		case "13":
			return String.class;
			// Campo tipo timestamp no cache
		case "14":
			return Timestamp.class;
			// Campo tipo collection no cache
		case "15":
			return String.class;
			// Campo tipo embedded no cache
		case "16":
			return String.class;
			// Campo tipo date formatado no cache
		case "17":
			return String.class;
			// Campo tipo taxa de cambio no cache
		case "18":
			return String.class;
			// Campo tipo sequence no cache
		case "19":
			return String.class;
		default:
			throw new IllegalArgumentException("Invalid property type: \"" + propertyType + "\"");
		}
	}
	
	@Data(definition = TEXT)
	private String classNameFK;

	private Boolean propertyCalculated;

	@Data(definition = NUMERIC)
	private Integer propertyDecimals;

	@Data(definition = TEXT)
	private Integer propertyLength;

	private List<String> propertyListFK;

	@Data(definition = TEXT)
	private String propertyName;

	private Integer propertyNumber;

	private Boolean propertyPK;

	@Data(definition = REQUIRED)
	private Boolean propertyRequired;

	private String propertyType;

	private String sqlColumnExpression;

	@Data(definition = COLUMN)
	private String sqlColumnName;

	private Boolean propertyIdFK;

	@Override
	public boolean equals(Object o) {
		return this.propertyName.equals(((mClassMappingProp) o).propertyName);
	}

	public String[] getAllFKNames() {
		return propertyListFK != null ? propertyListFK.toArray(new String[] {}) : new String[] {};
	}

	public String getClassNameFK() {
		return classNameFK;
	}

	public Boolean getPropertyCalculated() {
		return propertyCalculated;
	}

	public Integer getPropertyDecimals() {
		return propertyDecimals;
	}

	public Boolean getPropertyIdFK() {
		return propertyIdFK;
	}

	public Integer getPropertyLength() {
		return propertyLength;
	}

	public List<String> getPropertyListFK() {
		return propertyListFK;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Integer getPropertyNumber() {
		return propertyNumber;
	}

	public Boolean getPropertyRequired() {
		return propertyRequired;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public String getSqlColumnExpression() {
		return sqlColumnExpression;
	}

	public String getSqlColumnName() {
		return sqlColumnName;
	}

	public Class<?> getType() {
		try {
			return getType(propertyType);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid property type: '" + propertyName + "' [" + propertyType + "]");
		}
	}

	public String getTypeDescription() {
		return isPropertyFK() ? getClassNameFK() : getType().getSimpleName();
	}

	@Override
	public int hashCode() {
		return this.propertyName.hashCode();
	}

	public boolean isDate() {
		return Date.class.equals(getType()) && !Timestamp.class.equals(getType());
	}

	public boolean isNumeric() {
		return Double.class.equals(getType());
	}

	public boolean isPropertyCalculated() {
		return propertyCalculated == null ? false : propertyCalculated;
	}

	public boolean isPropertyFK() {
		return classNameFK != null && !classNameFK.isEmpty() && propertyListFK != null && !propertyListFK.isEmpty();
	}

	public boolean isPropertyIdFK() {
		return ((propertyIdFK == null) ? false : propertyIdFK);
	}

	public boolean isPropertyPK() {
		return propertyPK != null && Boolean.TRUE.equals(propertyPK);
	}

	public boolean isPropertyRequired() {
		return propertyRequired == null ? false : propertyRequired;
	}

	public boolean isText() {
		return String.class.equals(getType()) && (getPropertyLength() == null || getPropertyLength() > 4000);
	}

	public boolean isTimestamp() {
		return Timestamp.class.equals(getType());
	}

	public boolean isVarchar() {
		return String.class.equals(getType()) && getPropertyLength() != null;
	}

	public void setClassNameFK(String classNameFK) {
		this.classNameFK = classNameFK;
	}

	public void setPropertyCalculated(Boolean propertyCalculated) {
		this.propertyCalculated = propertyCalculated;
	}

	public void setPropertyDecimals(Integer propertyDecimals) {
		this.propertyDecimals = propertyDecimals;
	}

	public void setPropertyIdFK(Boolean propertyIdFK) {
		this.propertyIdFK = propertyIdFK;
	}

	public void setPropertyLength(Integer propertyLength) {
		this.propertyLength = propertyLength;
	}

	public void setPropertyListFK(List<String> propertyListFK) {
		this.propertyListFK = propertyListFK;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setPropertyNumber(Integer propertyNumber) {
		this.propertyNumber = propertyNumber;
	}

	public void setPropertyPK(Boolean propertyPK) {
		this.propertyPK = propertyPK;
	}

	public void setPropertyRequired(Boolean propertyRequired) {
		this.propertyRequired = propertyRequired;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public void setSqlColumnExpression(String sqlColumnExpression) {
		this.sqlColumnExpression = sqlColumnExpression;
	}

	public void setSqlColumnName(String sqlColumnName) {
		this.sqlColumnName = sqlColumnName;
	}

	public String toString() {
		if (isPropertyFK()) {
			return "FK: " + this.classNameFK + ": " + this.getPropertyName();
		} else if (isPropertyPK()) {
			return "PK: " + this.getType().getSimpleName() + ": " + this.getPropertyName();
		} else {
			return this.getType().getSimpleName() + ": " + this.getPropertyName();
		}
	}
}
