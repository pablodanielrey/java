package ar.com.dcsys.pr.model;

import javax.lang.model.element.ElementKind;


public class TypeContainerFactory {

	public static TypeContainer create(String sourcePackage, Param param) {
		
		String type = param.getType();
		TypeContainer tc = null;

		if (param.getElement().getKind() == ElementKind.ENUM) {
			tc = new BasicTypeContainer(sourcePackage, param);
		} else if (type.startsWith("java.util.List")) {
			tc = new ListTypeContainer(sourcePackage, param);
		} else if (type.startsWith("java.lang.")) {
			tc = new BasicTypeContainer(sourcePackage, param);
		} else {
			tc = new NullTypeContainer(sourcePackage, param);
		}
		
		return tc;
	}
	
}
