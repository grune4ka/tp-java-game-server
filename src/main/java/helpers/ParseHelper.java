package helpers;

public class ParseHelper {
	public static int strToInt(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		}
		catch (NumberFormatException e) {}
		return i;
	}
}
