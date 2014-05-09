package ar.com.dcsys.pr.model;

public class TypeContainerFactory {

	public static TypeContainer create(String sourcePackage, String type) {
		
		TypeContainer tc = null;
		if (type.startsWith("java.util.List")) {
			tc = new ListTypeContainer(sourcePackage, type);
		} else {
			tc = new BasicTypeContainer(sourcePackage, type);
		}
		
		return tc;
	}
	
}
