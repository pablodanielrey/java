package ar.com.dcsys.gwt.person.client.ws.viejos;


public class EncoderDecoder {
	
	private static native String b64encode(String a) /*-{
	  return window.btoa(a);
	}-*/;
	
	private static native String b64decode(String a) /*-{
	  return window.atob(a);
	}-*/;
	
	public static String toCodedString(String s) {
		String len = String.valueOf(s.length());
		String b64 = b64encode(s);
		return len + "*" + b64;
	}
	
	public static int getLength(String s) {
		int i = s.indexOf("*");
		String len = s.substring(0, i);
		return Integer.valueOf(len);
	}
	
	public static String fromCodedString(String s) {
		String[] s2 = s.split("*");
		String coded = s2[1];
		String decoded = b64decode(coded);
		return decoded;
	}
	
	/*

	public static String toString(WsMessage message) {
		
		StringBuilder sb = new StringBuilder();
		String coded;
		
		String id = message.getId();
		coded = toCodedString(id);
		sb.append(coded);
		
		String type = message.getType();
		coded = EncoderDecoder.toCodedString(type);
		sb.append(coded);

		String desc = message.getDescription();
		coded = toCodedString(desc);
		sb.append(coded);

		WsMessageParams[] params = message.getParams();
		for (WsMessageParams p : params) {
			coded = toCodedString(p.clazz);
			sb.append(coded);
			coded = toCodedString(p.payload);
			sb.append(coded);
		}
		
		return sb.toString();
	}
	
		*/
	
}
