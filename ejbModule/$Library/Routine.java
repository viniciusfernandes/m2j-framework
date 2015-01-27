package $Library;

public class Routine {

	public static Object Exists(String name){		
		boolean result;
		try {
			Class.forName(name);
			result = true;
		} catch (ClassNotFoundException e) {
			result = false;
		}
		return result;
	}
	
	public Routine() {
		// TODO Auto-generated constructor stub
	}
}
