package ar.com.dcsys.pr.model;

import java.util.UUID;

import ar.com.dcsys.pr.Utils;

public class ClientParamEncoder {
	
	
	public static String decodeResponse(Manager manager, Manager.InstanceInfo ii, Receiver param, String coded) {

		String type = Utils.getInteralType(param.getType());
		String varName = "a" + UUID.randomUUID().toString().replace("-", "");
		String var2Name = "a" + UUID.randomUUID().toString().replace("-", "");
		
		Factory factory = manager.getFactory();
		Getter g = factory.findByType(type);
		TypeContainer tc = g.getTypeContainer();
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName)
					   .append("= com.google.web.bindery.autobean.shared.AutoBeanCodex.decode(").append(ii.managerFactory).append(",").append(tc.getType()).append(".class,").append(coded).append(");");
		
		if (type.startsWith("java.lang.") || (type.startsWith("java.util.List"))) {		
			sb.append("\n").append(type).append(" ").append(var2Name).append(" = ").append(varName).append(".as().getValue();");
		} else {
			sb.append("\n").append(type).append(" ").append(var2Name).append(" = ").append(varName).append(".as();");
		}
		
		sb.append("\n").append(param.getName()).append(".onSuccess(").append(var2Name).append(");");
		
		return sb.toString();
	}
	
	
	public static String encode(Manager manager, Manager.InstanceInfo ii, Param param) {
		
		StringBuilder sb = new StringBuilder();
		
		String varName = "a" + UUID.randomUUID().toString().replace("-", "");
		String eVarName = "a" + UUID.randomUUID().toString().replace("-", "");
		String valueContainer = param.getName();
		
		Factory factory = manager.getFactory();
		Getter g = factory.findByType(param.getType());
		TypeContainer tc = g.getTypeContainer();

		
		// chequeo si es una lista de tipos definidos por usuario (o sea no de tipos primitivos.)
		if (param.getType().startsWith("java.util.List") && (!param.getType().startsWith("java.util.List<java.lang."))) {
			// hago wrapping de los valores de la lista.
			
			String subType = param.getType().substring(param.getType().indexOf("<") + 1, param.getType().lastIndexOf(">"));
			valueContainer = "a" + UUID.randomUUID().toString().replace("-", "");
			
			sb.append("\n").append(Utils.ident(8)).append("java.util.List<").append(subType).append("> ").append(valueContainer).append(" = new java.util.ArrayList<>();");
			sb.append("\n").append(Utils.ident(8)).append("for (").append(subType).append(" e : ").append(param.getName()).append(") {");
			sb.append("\n").append(Utils.ident(10)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(subType).append("> eAutoBean = ").append(ii.managerFactory).append(".").append(factory.findByType(subType).getName()).append("(e);");
			sb.append("\n").append(Utils.ident(10)).append(valueContainer).append(".add(eAutoBean.as());");
			sb.append("\n").append(Utils.ident(8)).append("};");
			
		}
		

		if (param.getType().startsWith("java.lang.") || param.getType().startsWith("java.util.List")) {
			
			// se generaron containers. asi que se setea el valor.
			sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName).append(" = ").append(ii.managerFactory).append(".").append(g.getName()).append("();");
			sb.append("\n").append(Utils.ident(8)).append(varName).append(".as().setValue(").append(valueContainer).append(");");
			
		} else {

			// es un tipo definido por el usuario asi que no se genera container. se usa el AutoBean<Tipo>
			sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName).append(" = ").append(ii.managerFactory).append(".").append(g.getName()).append("(").append(valueContainer).append(");");
			
		}
		
		
		sb.append("\n").append(Utils.ident(8)).append("String ").append(eVarName).append(" = com.google.web.bindery.autobean.shared.AutoBeanCodex.encode(").append(varName).append(").getPayload();");
		sb.append("\n").append(Utils.ident(8)).append("params.add(").append(eVarName).append(");");
		
		return sb.toString();
	}
	
}
