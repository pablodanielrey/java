package ar.com.dcsys.pr.runtime;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import ar.com.dcsys.pr.Utils;

public class Method {

	private final Manager manager;
	private final String name;
	private final List<Param> params = new ArrayList<>();
	private Receiver receiver;
	
	public Method(Manager manager, String name) {
		this.manager = manager;
		this.name = name;
	}
	
	public Manager getManager() {
		return manager;
	}

	public String getName() {
		return name;
	}

	public List<Param> getParams() {
		return params;
	}

	public Receiver getReceiver() {
		return receiver;
	}
	
	public void setReceiver(Receiver r) {
		this.receiver = r;
	}
	
	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(manager.getType());
		sb.append(name);
		for (Param p : params) {
			sb.append("_").append(p.getType()).append("_").append(p.getName());
		}
		
		return sb.toString();
	}
	
	
	public static void process(Manager manager, ExecutableElement e, ProcessingEnvironment env) {
		
		/////////// controlo las precondiciones de los métodos antes de procesarlos //////////////
		//
		//  retorno = void y último parámetro = Receiver.
		//
		///////////////
		
		TypeMirror tm = e.getReturnType();
		if (tm.getKind() != TypeKind.VOID) {
			return;
		}
		
		List<? extends VariableElement> params = e.getParameters();
		if (params.size() <= 0) {
			return;
		}
		VariableElement rec = params.get(params.size() - 1);
		TypeMirror type = rec.asType();
		if (!type.toString().startsWith("ar.com.dcsys.gwt.manager.shared.Receiver")) {
			return;
		}
		
		/////////////////////////////////////////
		
		String name = e.getSimpleName().toString();
		Method method = new Method(manager,name);
		
		for (int i = 0; i < params.size() - 1; i++) {
			
			VariableElement ve = params.get(i);
			Param.process(method, ve, env);
			
		}		
		
		Receiver.process(method, params.get(params.size()-1), env);
		
		manager.getMethods().add(method);
	}
	
	public void generateServerSourceFile(Manager manager, RuntimeInfo ii, ProcessingEnvironment processingEnv) {

		String className = "M_" + getSignature().replace("<", "_").replace(">", "_").replace(".", "_");
		String classType = manager.getServerPackage() + "." + className;
		
		StringBuilder sb = new StringBuilder();

		
		sb.append("\n").append("package " + manager.getServerPackage()).append(";\n\n");
		sb.append("\n");
		sb.append("\n").append("public class ").append(className).append(" implements ar.com.dcsys.gwt.manager.server.handler.MethodHandler {");
		sb.append("\n");
		sb.append("\n").append("private final ").append(manager.getServerType()).append("Bean manager;");
		sb.append("\n");
		
		// variables de instancia para guardar todos los serializers/deserializers
		for (String type : ii.serverRuntimeVars.keySet()) {
			String name = ii.serverRuntimeVars.get(type);
			sb.append("\n // serializer para : ").append(type);
			sb.append("\n").append("private final ").append(type).append(" ").append(name).append(";");
		}
		

		// constructor donde se recibe el manager implementado por el usuario del lado del server.
		sb.append("\n").append("@javax.inject.Inject");
		sb.append("\n").append("public ").append(className).append("(").append(manager.getServerType() + "Bean").append(" manager ");
		
		// injecto cada serializer
		for (String type : ii.serverRuntimeVars.keySet()) {
			String name = ii.serverRuntimeVars.get(type);
			sb.append(", ").append(type).append(" ").append(name);
		}
		
		sb.append(") {");
		sb.append("\n").append("this.manager = manager;");
		
		// asigno los serializers
		for (String type : ii.serverRuntimeVars.keySet()) {
			String name = ii.serverRuntimeVars.get(type);
			sb.append("\n").append("this.").append(name).append(" = ").append(name).append(";");
		}
		
		
		sb.append("\n").append("}");
		sb.append("\n");
		
		// metodo donde se registra como observer de los handlers de los mensajes.
		sb.append("\n").append("private void register").append("(@javax.enterprise.event.Observes ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer<ar.com.dcsys.gwt.manager.server.handler.MethodHandler> hs) {");
		sb.append("\n").append("hs.add(this);");
		sb.append("\n").append("}");
		sb.append("\n");
		
		// procesa la llegada de un mensaje.
		sb.append("\n").append("@Override");
		sb.append("\n").append("public boolean process(final String id, ar.com.dcsys.gwt.manager.shared.message.Message msg, final ar.com.dcsys.gwt.messages.server.MessageContext ctx) {");
		
		sb.append("\n").append("if (\"").append(getSignature()).append("\".equals(msg.getFunction())) {");
		
		// genero el receiver que responde al cliente el resultado.
		sb.append("\n").append(getReceiver().getType()).append(" receiver = new ").append(getReceiver().getType()).append("() {");
		sb.append("\n").append("@Override");
		sb.append("\n").append("public void onSuccess(").append(getReceiver().getInternalParam().getType()).append(" response) {");
		sb.append("\n");
		
		// se codifica la respuesta
		String eVarName = "a" + UUID.randomUUID().toString().replace("-", "");
		String varSerializer = getServerSerializerVarName(getReceiver().getInternalParam().getType(), ii);
		sb.append("\n").append("String ").append(eVarName).append(" = ").append(varSerializer).append(".toJson(response);");		

		// se envía la respuesta al cliente.
		sb.append("\n").append("ctx.getTransport().send(id,").append(eVarName).append(",new ").append(Manager.transportReceiverClass).append("() { public void onSuccess(String msg){}; public void onFailure(String e){}; });");
		sb.append("\n");
		sb.append("\n").append("};");
		
		sb.append("\n").append("@Override");
		sb.append("\n").append("public void onError(String error) {");
		sb.append("\n").append("// no hago nada porque todavia no se tiene solucionado el manejo de errores y la transferncia de estos hacia el cliente");
		sb.append("\n").append("};");
		sb.append("\n").append("};");

		// decodifico los parámetros
		StringBuilder sb2 = new StringBuilder();
		sb2.append("\n").append("this.manager.").append(getName()).append("(");
		sb.append("\n").append("int count = 0;");
		for (Param p : getParams()) {
			String feVarName = "e" + UUID.randomUUID().toString().replace("-", "");
			String dVarName = "d" + UUID.randomUUID().toString().replace("-", "");
			String type = p.getType();
			
			sb.append("\n").append("String ").append(feVarName).append(" = ").append("msg.getParams().get(count);");
			sb.append("\n").append("count = count + 1;");

			String ser = getServerSerializerVarName(type, ii);
			sb.append("\n").append(type).append(" ").append(dVarName).append(" = ").append(ser).append(".read(").append(feVarName).append(");");
			
			sb2.append(dVarName).append(",");
		}
		
		sb2.append("receiver);");
		
		// llamo al metodo de la clase que debería implemntarlo desde el lado del server.
		sb.append(sb2.toString());
		
		
		sb.append("\n").append("return true;");
		sb.append("\n").append("} else {");
		sb.append("\n").append("return false;");
		sb.append("\n").append("}");
		
		sb.append("\n").append("}");
		sb.append("\n").append("}");
		
	
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
	
	
	/**
	 * Retorna el nombre de la variable instanciada de tipo del serializador que se usaría para serializar/deserializar la variable de tipo variableType
	 * @param variableType
	 * @param ii
	 * @return
	 */
	private String getServerSerializerVarName(String variableType, RuntimeInfo ii) {
		
		String serializer = ii.serverSerializersMap.get(variableType);
		String varSerializer = ii.serverRuntimeVars.get(serializer);
		
		return varSerializer;
	}	
	

	/**
	 * Retorna el nombre de la variable instanciada de tipo del serializador que se usaría para serializar/deserializar la variable de tipo variableType
	 * @param variableType
	 * @param ii
	 * @return
	 */
	private String getClientSerializerVarName(String variableType, RuntimeInfo ii) {
		
		String serializer = ii.clientSerializersMap.get(variableType);
		String varSerializer = ii.clientRuntimeVars.get(serializer);
		
		return varSerializer;
	}
	

	public void toClientStringBuilder(StringBuilder sb, RuntimeInfo ii) {
		
		sb.append("\n").append(Utils.ident(4)).append("public void ").append(getName()).append("(");
		
		// los parametros
		for (Param param : getParams()) {
			sb.append(param.getType()).append(" ").append(param.getName()).append(", ");
		}

		// el receiver 
		sb.append("final ");
		sb.append(getReceiver().getType()).append(" ").append(getReceiver().getName());
		sb.append(") {\n");
		
		
		//////////////// try inicial /////////////////////
		
		sb.append("\n").append(Utils.ident(6)).append("try {\n");
		
		////////////////// codifico los parametros /////////////////////

		sb.append("\n").append(Utils.ident(8)).append("java.util.List<String> params = new java.util.ArrayList<>();");
		sb.append("\n");
		
		for (Param param: getParams()) {
			sb.append("\n");
			String eVarName = "a" + UUID.randomUUID().toString().replace("-", "");
			String varSerializer = getClientSerializerVarName(param.getType(), ii);
			sb.append("\n").append("String ").append(eVarName).append(" = ").append(varSerializer).append(".toJson(").append(param.getName()).append(");");
			sb.append("\n").append("params.add(").append(eVarName).append(");");
		}
		
		
		sb.append("\n").append(Utils.ident(8)).append(Manager.messageClass).append(" msg = new ").append(Manager.messageClass).append("();");
		sb.append("\n").append(Utils.ident(8)).append("msg.setFunction(\"").append(getSignature()).append("\");");
		sb.append("\n").append(Utils.ident(8)).append("msg.setParams(params);");
		String varSerializer = getClientSerializerVarName(Manager.messageClass, ii);
		sb.append("\n").append(Utils.ident(8)).append("String emsg = ").append(varSerializer).append(".toJson(msg);");
		sb.append("\n\n");
	
		
		/// creo un receiver que decodifique el mensaje y llame a onSuccess. el dato a decodificar esta dado por el tipo interno del receiver.
		
		sb.append("\n").append(Utils.ident(8)).append(Manager.transport).append(".send(emsg, new ").append(Manager.transportReceiverClass).append("() {");
		sb.append("\n").append(Utils.ident(10)).append("@Override");
		sb.append("\n").append(Utils.ident(10)).append("public void onSuccess(String msg) {");
		
		sb.append("\n").append(Utils.ident(12)).append("if (msg == null || msg.equals(\"null\")) { ").append(getReceiver().getName()).append(".onSuccess(null); return; }");
		
		sb.append("\n").append(Utils.ident(12)).append("try {");
		
		String internalType = getReceiver().getInternalParam().getType();
		varSerializer = getClientSerializerVarName(internalType, ii);
		sb.append("\n").append(internalType).append(" response = ").append(varSerializer).append(".read(msg);");

		sb.append("\n").append(Utils.ident(12)).append(getReceiver().getName()).append(".onSuccess(response);");

		sb.append("\n").append(Utils.ident(12)).append("} catch (Exception a) {");
		sb.append("\n").append(Utils.ident(14)).append(getReceiver().getName()).append(".onError(a.getMessage());");
		sb.append("\n").append(Utils.ident(12)).append("};");
		
		
		sb.append("\n").append(Utils.ident(10)).append("}");
		sb.append("\n");
		sb.append("\n").append(Utils.ident(10)).append("@Override");
		sb.append("\n").append(Utils.ident(10)).append("public void onFailure(String error) {");
		sb.append("\n").append(Utils.ident(12)).append("try {");
		sb.append("\n").append(Utils.ident(14)).append(getReceiver().getName()).append(".onError(error);");
		sb.append("\n").append(Utils.ident(12)).append("} catch (Exception a) { };");
		sb.append("\n").append(Utils.ident(10)).append("}");
		sb.append("\n").append(Utils.ident(8)).append("});");
		sb.append("\n");
		
		
		////////// aca termina el receiver /////////
		
		
		sb.append("\n").append(Utils.ident(6)).append("} catch (Exception e) {");
		sb.append("\n").append(Utils.ident(8)).append(getReceiver().getName()).append(".onError(e.getMessage());");
		sb.append("\n").append(Utils.ident(6)).append("}");
		
		sb.append("\n\n");
		sb.append("\n").append(Utils.ident(4)).append("}");
		
	}
	
			
}
