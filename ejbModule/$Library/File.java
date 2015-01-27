package $Library;


public class File {
	public static Object CreateDirectoryChain(Object dir){
		return true;
	}
	public static Object DirectoryExists(Object dir){
		return Exists(dir);
	}	
	public static Object Exists(Object dir){
		return new java.io.File(String.valueOf(dir)).exists();
	}	
	public static Object NormalizeDirectory(Object dir){
		return dir;
	}
}
