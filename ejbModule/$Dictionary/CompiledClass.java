package $Dictionary;

public class CompiledClass {
	public Object $ExistsId(Object id) {
		int result;
		try {
			Class.forName(String.valueOf(id));
			result = 1;
		} catch (ClassNotFoundException e) {
			result = 0;
		}
		return result;
	}
}
