package ar.com.dcsys.pr;

public class Utils {

	public static String extractName(String type) {
		String t = type;
		if (type.contains("<")) {
			t = type.substring(0,type.indexOf("<"));
		}
		return t.substring(t.lastIndexOf(".") + 1);
	}
	
	public static String ident(int j) {
		if (j <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= j; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public static String getInteralType(String type) {
		return type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
	}
	
}
