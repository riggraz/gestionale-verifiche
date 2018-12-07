package utils;

public class SQLUtils {
	
	public static String escapeString(String string) {
		return string.replaceAll("'", "''");
	}

}
