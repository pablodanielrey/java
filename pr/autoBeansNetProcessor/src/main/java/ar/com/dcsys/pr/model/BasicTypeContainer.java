package ar.com.dcsys.pr.model;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

public class BasicTypeContainer implements TypeContainer {

	final String pack;
	final Param param;
	
	public BasicTypeContainer(String pack, Param param) {
		this.param = param;
		this.pack = pack;
	}
	

	@Override
	public String getPackage() {
		return pack;
	}
	
	@Override
	public String getContainedType() {
		return param.getType();
	}
	
	@Override
	public String getType() {
		String type = param.getType();
		String typeName = type.substring(type.lastIndexOf(".") + 1);
		return getPackage() + "." + typeName + "Container"; 
	}
	

	@Override
	public void generateSourceFile(ProcessingEnvironment processingEnv) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(getPackage()).append(";");
		sb.append("\n\n");

		String type = getType();
		String name = type.substring(type.lastIndexOf(".") + 1);
		
		
		sb.append("\n").append("public interface ").append(name).append(" {");
		sb.append("\n").append("public ").append(getContainedType()).append(" getValue();");
		sb.append("\n").append("public void setValue(").append(getContainedType()).append(" t);");
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
	
}
