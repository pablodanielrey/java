package ar.com.dcsys.pr.runtime;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import ar.com.dcsys.pr.Utils;

public class Manager {

	public static String extractName(String type) {
		return type.substring(type.lastIndexOf(".") + 1);
	}
	
	private final String packageName;
	private final String type;
	private final List<Method> methods = new ArrayList<>();
	private final RuntimeInfo runtimeInfo = new RuntimeInfo();
	
	public static final String messageClass = "ar.com.dcsys.gwt.manager.shared.message.MessageImpl";
	public static final String transport = "transport";
	public static final String transportClass = "ar.com.dcsys.gwt.messages.shared.Transport";
	public static final String transportReceiver = "receiver";
	public static final String transportReceiverClass = "ar.com.dcsys.gwt.messages.shared.TransportReceiver";
	

	public RuntimeInfo getRuntimeInfo() {
		return runtimeInfo;
	}
	
	
	/**
	 * Crea un Manager a partir de un Element de tipo INTERFACE
	 * @param e
	 * @return
	 */
	public static Manager create(Element e, ProcessingEnvironment env) {
		
		if (e.getKind() != ElementKind.INTERFACE) {
			return null;
		}
		
		String pack = ((PackageElement)e.getEnclosingElement()).getQualifiedName().toString();
		String type = ((TypeElement)e).getSimpleName().toString();
		Manager m = new Manager(pack,type);
		
		List<? extends Element> elements = e.getEnclosedElements();
		for (Element ee : elements) {
			m.processElement(ee, env);
		}
		
		return m;
	}

	private void processElement(Element e, ProcessingEnvironment env) {

		if (e.getKind() == ElementKind.FIELD) {
			return;
		}
		
		if (e.getKind() == ElementKind.METHOD) {
			ExecutableElement ee = (ExecutableElement)e;
			Method.process(this, ee, env);
		}
		
	}

	public void generateSourceFiles(ProcessingEnvironment processingEnv) {

		generateClientSourceFile(getRuntimeInfo(), processingEnv);
		generateServerSourceFiles(getRuntimeInfo(), processingEnv);
		
	}

	
	private void generateServerSourceFiles(RuntimeInfo ii, ProcessingEnvironment processingEnv) {
		for (Method m : getMethods()) {
			m.generateServerSourceFile(this, ii, processingEnv);
		}
	}
	
	private void generateClientSourceFile(RuntimeInfo info, ProcessingEnvironment processingEnv) {
		
		// creo la info de runtime ///
		
		String interfaceType = getPackageName() + "." + getType();
		String classType = getClienType() + "Bean";
		String className = extractName(classType);

		//////

		// genero el serializer de los mensajes //
		
		SerializerGenerator.generateClientSerializer(messageClass, getClientPackage(), getRuntimeInfo(), processingEnv);
		
		///////////////////
		
		
		
		StringBuilder sb = new StringBuilder();

		sb.append("package " + getClientPackage()).append(";\n\n");
		
		////////definicion de la clase /////////////////////
		
		sb.append("public class ").append(className).append(" implements ").append(interfaceType).append(" {").append("\n");
		
		// variables de intancia //////
		sb.append("\n").append(Utils.ident(4)).append("private ").append(transportClass).append(" ").append(transport).append(";");		// se injecta en el constructor
		
		// aca se insertan los readeers y writers de todos los tipos manejados por el manager.
		Set<String> serializers = info.clientRuntimeVars.keySet();
		for (String s : serializers) {
			String varName = info.clientRuntimeVars.get(s);
			sb.append("\n").append(Utils.ident(4)).append("private ").append(s).append(" ").append(varName).append(" = com.google.gwt.core.client.GWT.create(").append(s).append(".class);");
		}
		
		sb.append("\n\n");
		
		
		////// implementacion de las internfaces /////////
		
		sb.append("\n").append("@Override");
		sb.append("\n").append(Utils.ident(4)).append("public void setTransport(ar.com.dcsys.gwt.messages.shared.Transport t) {");
		sb.append("\n").append(Utils.ident(8)).append("this.").append(transport).append(" = t;");
		sb.append("\n").append(Utils.ident(4)).append("}");
		sb.append("\n\n");
		
		
		///  constructor //////

		sb.append("\n").append(Utils.ident(4)).append("public ").append(className).append("() { }");
		sb.append("\n\n");

		/*
		 * 	esto era para probar la injection del transport. NO FUNCA
		sb.append("\n").append(Utils.ident(4)).append("@com.google.inject.Inject");
		sb.append("\n").append(Utils.ident(4)).append("public ").append(ii.className).append("(").append(ii.transportClass).append(" ").append(ii.transport).append(") {");
		sb.append("\n").append(Utils.ident(8)).append("this.").append(ii.transport).append(" = ").append(ii.transport).append(";");
		sb.append("\n").append(Utils.ident(4)).append("}");
		sb.append("\n\n");
		*/
		
		for (Method method : getMethods()) {
			sb.append("\n");
			method.toClientStringBuilder(sb,info);
		}
		
		sb.append("}\n");
		
		
		// escribo el archivo
		
		try {
			JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classType);
			PrintWriter out = new PrintWriter(jfo.openWriter());
			out.println(sb.toString());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			
		}		
		
	}

	
	public Manager(String packageName, String type) {
		this.packageName = packageName;
		this.type = type;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public List<Method> getMethods() {
		return methods;
	}
	
	public String getSharedPackage() {
		return packageName;
	}
	
	public String getClientPackage() {
		return packageName.replace(".shared", ".client");
	}

	public String getServerPackage() {
		return packageName.replace(".shared", ".server");
	}
	
	public String getType() {
		return type;
	}
	
	public String getClienType() {
		return getClientPackage() + "." + extractName(getType());
	}
	
	public String getServerType() {
		return getServerPackage() + "." + extractName(getType());
	}

	
}
