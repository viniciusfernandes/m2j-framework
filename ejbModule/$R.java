public class $R {
	public static Object EXIST(Object... args) {
		try {
			Class.forName(String.valueOf(args[0]).replace(".OBJ", ""));
		}
		catch (ClassNotFoundException e) {
			System.err.println("Class "+args[0]+ " not found with $R.EXIST!");
			return false;
		}
		return true;
	}

	public static Object ROUTINE(Object... args) {
		return "";
	}
}
