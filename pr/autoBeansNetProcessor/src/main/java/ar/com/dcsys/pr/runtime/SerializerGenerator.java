package ar.com.dcsys.pr.runtime;

import java.io.PrintWriter;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

public class SerializerGenerator {

	public static void generateServerSerializer(String paramType, String packageName, RuntimeInfo ri, ProcessingEnvironment env) {

		env.getMessager().printMessage(Kind.WARNING, "chequeando serializer servidor para el tipo : " + paramType);
		
		if (ri.serverSerializersMap.get(paramType) != null) {
			// ya fue generado asi que retorno
			return;
		}
		
		env.getMessager().printMessage(Kind.WARNING, "generando serializer servidor para el tipo : " + paramType);
		
		// genero usando gson
		
		String serName = "S" + paramType.replace(".", "_").replace("<","_").replace(">", "_") + "Bean";
		String serType = packageName + "." + serName; 
		
		StringBuilder sb = new StringBuilder();
		sb.append("\npackage ").append(packageName).append(";");
		sb.append("\n").append("public class ").append(serName).append(" implements ar.com.dcsys.pr.CSD<").append(paramType).append("> {");
		
		sb.append("\n").append("private final com.google.gson.Gson gson;");
		

		sb.append("\n").append("public ").append(serName).append("() {");
		sb.append("\n").append("gson = (new com.google.gson.GsonBuilder()).create();");
		sb.append("\n").append("}");
		
		sb.append("\n").append("@Override").append("\n");
		sb.append("\n").append("public String toJson(").append(paramType).append(" o) {");
		sb.append("\n").append("return gson.toJson(o);");
		sb.append("\n").append("}");
		
		sb.append("\n").append("@Override").append("\n");
		sb.append("\n").append("public ").append(paramType).append(" read(String json) {");
		sb.append("\n").append("com.google.gson.reflect.TypeToken typeToken = new com.google.gson.reflect.TypeToken<").append(paramType).append(">() {};");
		sb.append("\n").append("java.lang.reflect.Type type = typeToken.getType();");
		sb.append("\n").append("return gson.fromJson(json,type);");
		sb.append("\n").append("}");
		
		sb.append("\n").append("}");
		

		// genero la info de runtime
		String varName = "s" + UUID.randomUUID().toString().replace("-", "");
		ri.serverSerializersMap.put(paramType, serType);
		ri.serverRuntimeVars.put(serType, varName);
		
		
		// escribo el archivo
		
		try {
			JavaFileObject jfo = env.getFiler().createSourceFile(serType);
			PrintWriter out = new PrintWriter(jfo.openWriter());
			out.println(sb.toString());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			
		}				
		
	}	
	
	
	
	public static void generateClientSerializer(String paramType, String packageName, RuntimeInfo ri, ProcessingEnvironment env) {

		env.getMessager().printMessage(Kind.WARNING, "chequeando serializer cliente para el tipo : " + paramType);
		
		if (ri.clientSerializersMap.get(paramType) != null) {
			// ya fue generado asi que retorno.
			return;
		}

		env.getMessager().printMessage(Kind.WARNING, "generando serializer cliente para el tipo : " + paramType);

		
		// genero usando piriti
		
		String serName = "S" + paramType.replace(".", "_").replace("<","_").replace(">", "_") + "Bean";
		String serType = packageName + "." + serName; 
		
		StringBuilder sb = new StringBuilder();
		sb.append("\npackage ").append(packageName).append(";");
		sb.append("\n").append("public class ").append(serName).append(" implements ar.com.dcsys.pr.CSD<").append(paramType).append("> {");
		
		sb.append("\n").append("interface Reader extends name.pehl.piriti.json.client.JsonReader<").append(paramType).append("> {};");
		sb.append("\n").append("private final Reader READER = com.google.gwt.core.client.GWT.create(Reader.class);");
		
		sb.append("\n").append("interface Writer extends name.pehl.piriti.json.client.JsonWriter<").append(paramType).append("> {};");
		sb.append("\n").append("private final Writer WRITER = com.google.gwt.core.client.GWT.create(Writer.class);");
		
		sb.append("\n").append("@Override").append("\n");
		sb.append("\n").append("public String toJson(").append(paramType).append(" o) {");
		sb.append("\n").append("return WRITER.toJson(o);");
		sb.append("\n").append("}");
		
		sb.append("\n").append("@Override").append("\n");
		sb.append("\n").append("public ").append(paramType).append(" read(String json) {");
		sb.append("\n").append("return READER.read(json);");
		sb.append("\n").append("}");
		
		sb.append("\n").append("}");
		

		// genero la info de runtime
		String varName = "c" + UUID.randomUUID().toString().replace("-", "");
		ri.clientSerializersMap.put(paramType, serType);
		ri.clientRuntimeVars.put(serType, varName);
		
		
		// escribo el archivo
		
		try {
			JavaFileObject jfo = env.getFiler().createSourceFile(serType);
			PrintWriter out = new PrintWriter(jfo.openWriter());
			out.println(sb.toString());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			
		}		
		
	}	
	
}
