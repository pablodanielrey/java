package ar.com.dcsys.pr;

public class Utils {

	public static String extractName(String type) {
		String t = type;
		if (type.contains("<")) {
			t = type.substring(0,type.indexOf("<"));
		}
		return t.substring(t.lastIndexOf(".") + 1);
	}
	
}
