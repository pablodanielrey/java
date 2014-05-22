package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

public class GetterFactory {

	private static String getInternalType(String t) {
		return t.substring(t.indexOf("<") + 1, t.lastIndexOf(">"));
	}
	
	
	private static boolean hasSubtype(String t) {
		return t.contains("<") && t.contains(">");
	}
	
	public static void create(Factory f, Param p, ProcessingEnvironment env) {

		String packageName = f.getPackage();
		Param param = p;
		
		while (true) {

			String t = param.getType();
			
			if (f.findByType(t) == null) {
				
				TypeContainer tc = TypeContainerFactory.create(packageName, param, env);
				String name = "get_" + tc.getType().replace(".", "_");
				
				Getter g = new Getter(name,tc);
				f.getGetters().add(g);
				
			}
			
			
			if (!param.isList()) {
				break;
			}

			param = param.getParameter();
		}
		
	}
	
}
