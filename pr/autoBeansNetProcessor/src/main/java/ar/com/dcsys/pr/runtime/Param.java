package ar.com.dcsys.pr.runtime;

import java.io.PrintWriter;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

public class Param {

	public static final Param extractInternalParam(Param p, ProcessingEnvironment env) {
		DeclaredType tm = (DeclaredType)p.getTypeMirror();
		TypeMirror it = tm.getTypeArguments().get(0);
		Element e = env.getTypeUtils().asElement(it);
		Param p2 = new Param(e,it,env);
		return p2;
	}

	
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
	
	
	public Param getParameter() {
		return extractInternalParam(this, env);
	}
	
	
	private static final String[] primitives = {"java.lang.","java.util.Date"};
	
	/**
	 * Retorna true en caso de ser un enum o un tipo no soportado por autobeans
	 * @return
	 */
	public boolean isPrimitive() {
		String t = typeMirror.toString();
		for (String t2 : primitives) {
			if (t.startsWith(t2)) {
				return true;
			}
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

		if (Param.extractInternalParam(this, env).isPrimitive()) {
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
		param.generateSerializers(method, env);
	}
	
	private void generateSerializers(Method method, ProcessingEnvironment env) {
		generateClientSerializer(method, env);
		generateServerSerializer(method, env);
	}
	
	private void generateClientSerializer(Method method, ProcessingEnvironment env) {
		
		// genero usando piriti
		
		Manager manager = method.getManager();
		String clientPackage = manager.getClientPackage();
		
		RuntimeInfo ri = method.getManager().getRuntimeInfo();

		SerializerGenerator.generateClientSerializer(getType(), clientPackage, ri, env);
		
	}
	
	private void generateServerSerializer(Method method, ProcessingEnvironment env) {
		
		Manager manager = method.getManager();
		String serverPackage = manager.getServerPackage();
		
		RuntimeInfo ri = method.getManager().getRuntimeInfo();
		
		SerializerGenerator.generateServerSerializer(getType(), serverPackage, ri, env);

	}
	
}
