package ar.com.dcsys.pr.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ar.com.dcsys.pr.Utils;

public class Method {

	private final Manager manager;
	private final String name;
	private final List<Param> params = new ArrayList<>();
	private Param receiver;
	
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

	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name);
		for (Param p : params) {
			sb.append("-").append(p.getType()).append("-").append(p.getName());
		}
		
		return sb.toString();
	}
	
	
	public static void process(Manager manager, ExecutableElement e) {
		
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
			Param.process(method, ve);
			
		}		
		
		manager.getMethods().add(method);
	}
	

	public void toClientStringBuilder(StringBuilder sb, Manager.InstanceInfo ii) {
		
		sb.append("\n").append(Utils.ident(4)).append("public void ").append(getName()).append("(");
		
		// los parametros
		for (Param param : getParams()) {
			sb.append(param.getType()).append(" ").append(param.getName()).append(", ");
		}

		// el receiver  TODO: 
		sb.append("final ");
		sb.append("ar.com.dcsys.gwt.manager.Receiver<String> receiver");
		sb.append(") {\n");
		
		
		//////////////// try inicial /////////////////////
		
		sb.append("\n").append(Utils.ident(6)).append("try {\n");
		
		////////////////// codifico los parametros /////////////////////

		sb.append("\n").append(Utils.ident(8)).append("List<String> params = new ArrayList<>();");
		sb.append("\n");
		
		for (Param param: getParams()) {
			sb.append("\n");
			sb.append(ClientParamEncoder.encode(manager, ii, param));
		}
		
		sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<ar.com.dcsys.gwt.manager.shared.Message> msg = ").append(ii.messageFactory).append(".getMessage();");
		sb.append("\n").append(Utils.ident(8)).append("msg.as().setFunction(\"").append(getSignature()).append("\");");
		sb.append("\n").append(Utils.ident(8)).append("msg.as().setParams(params);");
		sb.append("\n").append(Utils.ident(8)).append("String emsg = AutoBeanCodex.encode(msg).getPayload();");
		sb.append("\n\n");
	
		
		sb.append("\n").append(Utils.ident(6)).append("} catch (Exception e) {");
		sb.append("\n").append(Utils.ident(8)).append("e.printStackTrace();");
		sb.append("\n").append(Utils.ident(6)).append("}");
		
		sb.append("\n\n");
		sb.append("\n").append(Utils.ident(4)).append("}");
		
	}
	
			
}
