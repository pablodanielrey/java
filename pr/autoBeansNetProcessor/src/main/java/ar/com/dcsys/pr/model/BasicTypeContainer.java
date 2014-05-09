package ar.com.dcsys.pr.model;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

public class BasicTypeContainer implements TypeContainer {

	final String pack;
	final String type;
	
	public BasicTypeContainer(String pack, String type) {
		this.type = type;
		this.pack = pack;
	}
	

	@Override
	public String getPackage() {
		return pack;
	}
	
	@Override
	public String getContainedType() {
		return type;
	}
	
	@Override
	public String getType() {
		String typeName = type.substring(type.lastIndexOf(".") + 1);
		return getPackage() + "." + typeName + "Container"; 
	}
	

	@Override
	public void generateSourceFile(ProcessingEnvironment processingEnv) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(getPackage()).append(";");
		
		sb.append("\n\n");
		sb.append("\nimport ar.com.dcsys.gwt.manager.shared.TypeContainer;");
		sb.append("\n\n");

		String type = getType();
		String name = type.substring(type.lastIndexOf(".") + 1);
		
		
		sb.append("public interface ").append(name).append(" extends TypeContainer<").append(getContainedType()).append("> {");
		sb.append("\n").append("}");
		
		
		try {
			JavaFileObject jfo = processingEnv.getFiler().createSourceFile(type);
			PrintWriter out = new PrintWriter(jfo.openWriter());
			out.println(sb.toString());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Genera el codigo necesario para codificar el parámetro a String y agregarlo a la lista de Strings paramsName.
	 * @param f
	 * @param paramsName
	 * @param sb
	 
	public void generateCoder(Factory f, String paramsName, StringBuilder sb) {
		
		String varName = UUID.randomUUID().toString().replace("-","");
		String encodedVarName = UUID.randomUUID().toString().replace("-","");
		
		sb.append("\n").append("AutoBean<").append(getType()).append(">").append(varName).append(" = ").append(f.getName()).append(".").append(getterName());
		sb.append("\n").append(varName).append(".as().setValue(").append(param.getName()).append(");");
		sb.append("\n").append("String").append(encodedVarName).append(" = ").append("AutoBeanCondex.encode(").append(varName).append(").getPayload();");
		sb.append("\n").append(paramsName).append(".add(").append(encodedVarName).append(");");
		
	}
	*/
	
}
