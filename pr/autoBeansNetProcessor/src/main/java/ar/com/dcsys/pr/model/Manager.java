package ar.com.dcsys.pr.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
	
	private final Factory factory;
	private final String packageName;
	private final String type;
	private final List<Method> methods = new ArrayList<>();


	/**
	 * Crea un Manager a partir de un Element de tipo INTERFACE
	 * @param e
	 * @return
	 */
	public static Manager create(Element e) {
		
		if (e.getKind() != ElementKind.INTERFACE) {
			return null;
		}
		
		String pack = ((PackageElement)e.getEnclosingElement()).getQualifiedName().toString();
		String type = ((TypeElement)e).getSimpleName().toString();
		Manager m = new Manager(pack,type);
		
		List<? extends Element> elements = e.getEnclosedElements();
		for (Element ee : elements) {
			m.processElement(ee);
		}
		
		return m;
	}

	private void processElement(Element e) {

		if (e.getKind() == ElementKind.FIELD) {
			return;
		}
		
		if (e.getKind() == ElementKind.METHOD) {
			ExecutableElement ee = (ExecutableElement)e;
			Method.process(this, ee);
		}
		
	}

	public void generateSourceFiles(ProcessingEnvironment processingEnv) {
		
		factory.generateSourceFile(processingEnv);
		
		generateClientSourceFile(processingEnv);
		generateServerSourceFiles(processingEnv);
		
	}
	
	
	public class InstanceInfo {
		String className;
		String classType;
		String messageFactory;
		String messageFactoryClass;
		String managerFactory;
		String managerFactoryClass;
		String transport;
		String transportClass;
		String transportReceiver;
		String transportReceiverClass;
	}
	
	private void generateServerSourceFiles(ProcessingEnvironment processingEnv) {
		InstanceInfo ii = getInstanceInfo();
		for (Method m : getMethods()) {
			m.generateServerSourceFile(this, ii, processingEnv);
		}
	}
	
	private InstanceInfo getInstanceInfo() {
		InstanceInfo ii = new InstanceInfo();
		ii.classType = getClienType() + "Bean";
		ii.className = extractName(ii.classType);
		ii.messageFactory = "messageFactory";
		ii.messageFactoryClass = "ar.com.dcsys.gwt.manager.shared.message.MessageFactory";
		ii.managerFactory = "managerFactory";
		ii.managerFactoryClass = factory.getType();
		ii.transport = "transport";
		ii.transportClass = "ar.com.dcsys.gwt.messages.shared.Transport";
		ii.transportReceiver = "receiver";
		ii.transportReceiverClass = "ar.com.dcsys.gwt.messages.shared.TransportReceiver";
		return ii;
	}
	
	
	private void generateClientSourceFile(ProcessingEnvironment processingEnv) {
		
		StringBuilder sb = new StringBuilder();

		sb.append("package " + getClientPackage()).append(";\n\n");

	
		InstanceInfo ii = getInstanceInfo();
		
		////////definicion de la clase /////////////////////
		
		sb.append("public class ").append(ii.className).append(" implements ").append(getPackageName() + "." + getType()).append(" {").append("\n");
		
		// variables de intancia //////
		sb.append("\n").append(Utils.ident(4)).append("private final ").append(ii.messageFactoryClass).append(" ").append(ii.messageFactory).append(" = com.google.gwt.core.client.GWT.create(").append(ii.messageFactoryClass).append(".class);");
		sb.append("\n").append(Utils.ident(4)).append("private final ").append(ii.managerFactoryClass).append(" ").append(ii.managerFactory).append(" = com.google.gwt.core.client.GWT.create(").append(ii.managerFactoryClass).append(".class);");
		sb.append("\n").append(Utils.ident(4)).append("private ").append(ii.transportClass).append(" ").append(ii.transport).append(";");		// se injecta en el constructor
		sb.append("\n\n");
		
		
		///  constructor //////

		sb.append("\n").append(Utils.ident(4)).append("public ").append(ii.className).append("() { }");
		sb.append("\n\n");
		
		sb.append("\n").append(Utils.ident(4)).append("@com.google.inject.Inject");
		sb.append("\n").append(Utils.ident(4)).append("public ").append(ii.className).append("(").append(ii.transportClass).append(" ").append(ii.transport).append(") {");
		sb.append("\n").append(Utils.ident(8)).append("this.").append(ii.transport).append(" = ").append(ii.transport).append(";");
		sb.append("\n").append(Utils.ident(4)).append("}");
		sb.append("\n\n");
		
		for (Method method : getMethods()) {
			sb.append("\n");
			method.toClientStringBuilder(sb,ii);
		}
		
		sb.append("}\n");
		
		
		// escribo el archivo
		
		try {
			JavaFileObject jfo = processingEnv.getFiler().createSourceFile(ii.classType);
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
		this.factory = new Factory(this);
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

	public Factory getFactory() {
		return factory;
	}
	
	
}
