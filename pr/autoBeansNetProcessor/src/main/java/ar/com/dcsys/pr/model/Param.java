package ar.com.dcsys.pr.model;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class Param {

	private final String name;
	private final TypeMirror typeMirror;
	private final TypeKind typeKind;
	
	public Param(String name, TypeMirror mirror) {
		this.name = name;
		this.typeMirror = mirror;
		this.typeKind = mirror.getKind();
	}
	
	public String getType() {
		return typeMirror.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPackage() {
		String t = getType();
		if (t.contains("<")) {
			String st = t.substring(0,t.indexOf("<")); 
			return st.substring(0, st.lastIndexOf("."));
		} else {
			return t.substring(0, t.lastIndexOf("."));
		}
	}
	
	
	public static void process(Method method, VariableElement ve) {
		String name = ve.getSimpleName().toString();
		Param param = new Param(name,ve.asType());
		method.getParams().add(param);
		
		Factory factory = method.getManager().getFactory();
		factory.createGetter(param.getType());
	}
	
}
