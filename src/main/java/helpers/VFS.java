package helpers;

import java.io.*;

public class VFS {
	private static String root = System.getProperty("user.dir");
	
	public static boolean isExist(String path) {
		return (new File(root+path)).exists();
	}
	
	public static boolean isDirectory(String path) {
		return (new File(root+path)).isDirectory();
	}
	
	public static byte[] getBytes(String path) {
        byte[] buffer = null;
        String currentLineString;
        StringBuilder builder = new StringBuilder();
        String file = root+path;

        if(isDirectory(file))
            try {
                throw new IOException("is a Directory");
            } catch (IOException e) {
                e.printStackTrace();
            }
        else {
            try {
                FileReader fr = new FileReader(file);

                BufferedReader br = new BufferedReader(fr);

                while((currentLineString = br.readLine()) != null)
                    builder.append(currentLineString);
                br.close();

                buffer = builder.toString().getBytes();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;

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
