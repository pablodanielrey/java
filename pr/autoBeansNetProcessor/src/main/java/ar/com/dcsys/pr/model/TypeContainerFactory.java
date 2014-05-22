package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;


public class TypeContainerFactory {

	public static TypeContainer create(String sourcePackage, Param param, ProcessingEnvironment env) {
		
		TypeContainer tc = null;

		if (param.isPrimitive()) {
			env.getMessager().printMessage(Kind.NOTE,"creando container basico");
			tc = new BasicTypeContainer(sourcePackage, param);
		} else if (param.isList()) {
			env.getMessager().printMessage(Kind.NOTE,"creando container list");
			tc = new ListTypeContainer(sourcePackage, param);
		} else {
			env.getMessager().printMessage(Kind.NOTE,"creando container null");
			tc = new NullTypeContainer(sourcePackage, param);
		}
		
		return tc;
	}
	
}
