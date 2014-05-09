package ar.com.dcsys.pr.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

import ar.com.dcsys.pr.Utils;

public class Factory {

	private final String packageName;
	private final String type;
	private final List<Getter> getters = new ArrayList<>();
	
	public Factory(Manager manager) {
		this.packageName = manager.getSharedPackage();
		String t = manager.getType();
		this.type = this.packageName + "." + t.substring(t.lastIndexOf(".") + 1) + "Factory";
	}
	
	public void createGetter(String type) {
		Getter g = findByType(type);
		if (g != null) {
			return;
		}
		GetterFactory.create(this,type);
	}
	
	
	public List<Getter> getGetters() {
		return getters;
	}

	public Getter findByType(String type) {
		for (Getter g : getGetters()) {
			if (type.equals(g.getTypeContainer().getContainedType())) {
				return g;
			}
		}
		return null;
	}

	public String getPackage() {
		return packageName;
	}
	
	public String getType() {
		return type;
	}
	
	public void generateSourceFile(ProcessingEnvironment processingEnv) {

		StringBuilder sb = new StringBuilder();

		sb.append("\npackage ").append(getPackage()).append(";\n");
		sb.append("\npublic interface ").append(Utils.extractName(getType())).append(" extends com.google.web.bindery.autobean.shared.AutoBeanFactory {");
		
		for (Getter g : getGetters()) {
			g.generateSourceFile(processingEnv);

			sb.append("\n");
			g.toStringBuilder(sb);
		}
		
		sb.append("\n}\n");
		
		try {
			JavaFileObject jfo = processingEnv.getFiler().createSourceFile(getType());
			PrintWriter out = new PrintWriter(jfo.openWriter());
			out.println(sb.toString());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
