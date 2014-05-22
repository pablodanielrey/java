package ar.com.dcsys.pr.model;

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
	
	public void generateServerSourceFile(Manager manager, Manager.InstanceInfo ii, ProcessingEnvironment processingEnv) {

		String className = "M_" + getSignature().replace("<", "_").replace(">", "_").replace(".", "_");
		String classType = manager.getServerPackage() + "." + className;
		
		StringBuilder sb = new StringBuilder();

		
		sb.append("\n").append("package " + manager.getServerPackage()).append(";\n\n");
		sb.append("\n");
		sb.append("\n").append("public class ").append(className).append(" implements ar.com.dcsys.gwt.manager.server.handler.MethodHandler {");
		sb.append("\n");
		sb.append("\n").append("private final ").append(ii.messageFactoryClass).append(" ").append(ii.messageFactory).append(" = com.google.web.bindery.autobean.vm.AutoBeanFactorySource.create(").append(ii.messageFactoryClass).append(".class);");
		sb.append("\n").append("private final ").append(ii.managerFactoryClass).append(" ").append(ii.managerFactory).append(" = com.google.web.bindery.autobean.vm.AutoBeanFactorySource.create(").append(ii.managerFactoryClass).append(".class);");
		sb.append("\n").append("private final ").append(manager.getServerType()).append("Bean manager;");
		sb.append("\n");
		sb.append("\n").append("@javax.inject.Inject");
		sb.append("\n").append("public ").append(className).append("(").append(manager.getServerType() + "Bean").append(" manager) {");
		sb.append("\n").append("this.manager = manager;");
		sb.append("\n").append("}");
		sb.append("\n");
		sb.append("\n").append("private void register").append("(@javax.enterprise.event.Observes ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer<ar.com.dcsys.gwt.manager.server.handler.MethodHandler> hs) {");
		sb.append("\n").append("hs.add(this);");
		sb.append("\n").append("}");
		sb.append("\n");
		sb.append("\n").append("@Override");
		sb.append("\n").append("public boolean process(final String id, ar.com.dcsys.gwt.manager.shared.message.Message msg, final ar.com.dcsys.gwt.messages.server.MessageContext ctx) {");
		
		sb.append("\n").append("if (\"").append(getSignature()).append("\".equals(msg.getFunction())) {");
		
		// genero el receiver que responde al cliente el resultado.
		sb.append("\n").append(getReceiver().getType()).append(" receiver = new ").append(getReceiver().getType()).append("() {");
		sb.append("\n").append("@Override");
		sb.append("\n").append("public void onSuccess(").append(Utils.getInteralType(getReceiver().getType())).append(" response) {");
		sb.append("\n");
		String eVarName = "a" + UUID.randomUUID().toString().replace("-", "");
		sb.append("\n").append(TypesEncoderDecoder.encode(manager.getFactory(), ii.managerFactory, getReceiver().getInternalParam(), "response", eVarName));
		sb.append("\n").append("ctx.getTransport().send(id,").append(eVarName).append(",new ").append(ii.transportReceiverClass).append("() { public void onSuccess(String msg){}; public void onFailure(String e){}; });");
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
			sb.append("\n").append(TypesEncoderDecoder.decodeResponse(getManager().getFactory(), ii.managerFactory, p, feVarName, dVarName));
			
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

	public void toClientStringBuilder(StringBuilder sb, Manager.InstanceInfo ii) {
		
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
			sb.append(TypesEncoderDecoder.encode(manager.getFactory(), ii.managerFactory, param, param.getName(), eVarName));
			sb.append("\n").append("params.add(").append(eVarName).append(");");
		}
		
		sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<ar.com.dcsys.gwt.manager.shared.message.Message> msg = ").append(ii.messageFactory).append(".getMessage();");
		sb.append("\n").append(Utils.ident(8)).append("msg.as().setFunction(\"").append(getSignature()).append("\");");
		sb.append("\n").append(Utils.ident(8)).append("msg.as().setParams(params);");
		sb.append("\n").append(Utils.ident(8)).append("String emsg = com.google.web.bindery.autobean.shared.AutoBeanCodex.encode(msg).getPayload();");
		sb.append("\n\n");
	
		sb.append("\n").append(Utils.ident(8)).append(ii.transport).append(".send(emsg,new ar.com.dcsys.gwt.messages.shared.TransportReceiver() {");
		sb.append("\n").append(Utils.ident(10)).append("@Override");
		sb.append("\n").append(Utils.ident(10)).append("public void onSuccess(String msg) {");
		sb.append("\n").append(Utils.ident(12)).append("try {");
		sb.append(TypesEncoderDecoder.decodeResponse(getManager().getFactory(), ii.managerFactory, getReceiver().getInternalParam(), "msg", "response"));
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
		
		
		sb.append("\n").append(Utils.ident(6)).append("} catch (Exception e) {");
		sb.append("\n").append(Utils.ident(8)).append(getReceiver().getName()).append(".onError(e.getMessage());");
		sb.append("\n").append(Utils.ident(6)).append("}");
		
		sb.append("\n\n");
		sb.append("\n").append(Utils.ident(4)).append("}");
		
	}
	
			
}
