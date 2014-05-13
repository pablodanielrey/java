package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;

public class Getter {
	
	private final String name;
	private final TypeContainer typeContainer;
	
	public Getter(String name, TypeContainer typeContainer) {
		this.name = name;
		this.typeContainer = typeContainer;
	}
	
	public void toStringBuilder(StringBuilder sb) {
		sb.append("\npublic com.google.web.bindery.autobean.shared.AutoBean<").append(getTypeContainer().getType()).append("> ").append(getName()).append("();");
/*		if (!getTypeContainer().getContainedType().startsWith("java.util.List")) {
			sb.append("\npublic com.google.web.bindery.autobean.shared.AutoBean<").append(getTypeContainer().getType()).append("> ").append(getName()).append("(").append(getTypeContainer().getContainedType()).append(" l);");
		}
		*/
	}

	public String getName() {
		return name;
	}

	public TypeContainer getTypeContainer() {
		return typeContainer;
	}
	
	
	public void generateSourceFile(ProcessingEnvironment processingEnv) {
		typeContainer.generateSourceFile(processingEnv);
	}
	
}
