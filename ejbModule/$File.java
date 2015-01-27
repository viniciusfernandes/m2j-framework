import java.net.URI;

public class $File {
	public static Object Exists(Object dir) {
		return new java.io.File(String.valueOf(dir)).exists();
	}

	public static Object NormalizeDirectory(Object dir) {
		return URI.create(String.valueOf(dir)).normalize().toString();
	}
}
