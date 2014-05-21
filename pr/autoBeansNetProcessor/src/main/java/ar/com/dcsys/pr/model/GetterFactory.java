package ar.com.dcsys.pr.model;

import javax.lang.model.type.TypeMirror;

public class GetterFactory {

	private static String getInternalType(String t) {
		return t.substring(t.indexOf("<") + 1, t.lastIndexOf(">"));
	}
	
	
	private static boolean hasSubtype(String t) {
		return t.contains("<") && t.contains(">");
	}
	
	public static void create(Factory f, Param param) {

		String packageName = f.getPackage();

		String t = param.getType();

		while (true) {
			
			if (f.findByType(t) == null) {
				
				TypeContainer tc = TypeContainerFactory.create(packageName, param);
				String name = "get_" + tc.getType().replace(".", "_");
				
				Getter g = new Getter(name,tc);
				f.getGetters().add(g);
				
			}
			
		
			if (!hasSubtype(t)) {
				break;
			}
			
			t = getInternalType(t);
		}
		
	}
	
}
