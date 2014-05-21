package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ar.com.dcsys.pr.Utils;

public class Param {

	private final String name;
	private final Element element;
	private final TypeMirror typeMirror;
	private final TypeKind typeKind;
	private final ProcessingEnvironment env;
	
	public Param(Element ve, ProcessingEnvironment env) {
		this.name = ve.getSimpleName().toString();
		this.element = ve;
		this.typeMirror = ve.asType();
		this.typeKind = typeMirror.getKind();
		this.env = env;
	}

	public Param(Element ve, TypeMirror tm, ProcessingEnvironment env) {
		this.name = ve.getSimpleName().toString();
		this.element = ve;
		this.typeMirror = tm;
		this.typeKind = typeMirror.getKind();
		this.env = env;
	}
	
	
	
	/**
	 * Retorna true en caso de ser un enum o un tipo primitivo.
	 * @return
	 */
	public boolean isPrimitive() {
		if (typeMirror.toString().startsWith("java.lang.")) {
			return true;
		}
		
		Element e = env.getTypeUtils().asElement(typeMirror);
		ElementKind ek = e.getKind();
		
		if (ElementKind.ENUM.equals(ek)) {
			return true;
		}
		return false;
	}
	
	public boolean isList() {
		return (typeMirror.toString().startsWith("java.util.List"));
	}
	
	/**
	 * Retorna true en caso de ser una lista de elementos primitivos o de enums.
	 * @return
	 */
	public boolean isPrimitiveList() {
		if (!isList()) {
			return false;
		}
		
		String t = typeMirror.toString();
		if (Utils.getInteralType(t).startsWith("java.lang.")) {
			return true;
		}
		
		DeclaredType tm = (DeclaredType)typeMirror;
		TypeMirror dt = tm.getTypeArguments().get(0);
		ElementKind ek = env.getTypeUtils().asElement(dt).getKind();
		if (ek.equals(ElementKind.ENUM)) {
			return true;
		}
		
		return false;
	}
	

	/**
	 * REtorna true si es una lista de tipos declarados, salvo enums y tipos primitivos.
	 * @return
	 */
	public boolean isDeclaredTypeList() {
		return (isList() && (!isPrimitiveList()));
	}
	
	
	public Element getElement() {
		return element;
	}
	
	public TypeMirror getTypeMirror() {
		return typeMirror;
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
	
	
	public static void process(Method method, VariableElement ve, ProcessingEnvironment env) {
		Param param = new Param(ve,env);
		method.getParams().add(param);
		
		Factory factory = method.getManager().getFactory();
		factory.createGetter(param);
	}
	
}
