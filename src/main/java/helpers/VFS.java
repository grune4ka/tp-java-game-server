package helpers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VFS {
	private static String root = System.getProperty("user.dir");
	
	public static boolean isExist(String path) {
		return (new File(root+path)).exists();
	}
	
	public static boolean isDirectory(String path) {
		return (new File(root+path)).isDirectory();
	}
	
	public static String getAbsolutePath(String path) {
		return root+path;
	}
	
	public static String getBytes(String path) {
		try {
			File f = new File(root+path);
			FileReader fr = new FileReader(f);
			char[] buffer = null;		
			fr.read(buffer);
			fr.close();
			return String.copyValueOf(buffer);
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public static void writeToFile(String data, String path) {
		FileWriter fw;
		try {
			fw = new FileWriter(new File(path));
			fw.write(data);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
