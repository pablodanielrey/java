package ar.com.dcsys.pr.model;

import java.util.UUID;

public class ClientParamEncoder {

	public static String encode(Manager manager, Manager.InstanceInfo ii, Param param) {
		
		StringBuilder sb = new StringBuilder();
		
		String varName = UUID.randomUUID().toString().replace("-", "");
		String eVarName = UUID.randomUUID().toString().replace("-", "");
		String valueContainer = param.getName();
		
		Factory factory = manager.getFactory();
		Getter g = factory.findByType(param.getType());
		TypeContainer tc = g.getTypeContainer();

		
		if (param.getType().startsWith("java.util.List")) {
			// hago wrapping de los valores de la lista.
			
			String subType = param.getType().substring(param.getType().indexOf("<") + 1, param.getType().lastIndexOf(">"));
			valueContainer = UUID.randomUUID().toString().replace("-", "");
			
			sb.append("\n").append("java.util.List<").append(subType).append("> ").append(valueContainer).append(" = new java.util.ArrayList<>();");
			sb.append("\n").append("for (").append(subType).append(" e : ").append(param.getName()).append(") {");
			sb.append("\n").append("AutoBean<").append(subType).append("> eAutoBean = ").append(ii.managerFactory).append(".").append(factory.findByType(subType).getName()).append("(e);");
			sb.append("\n").append(valueContainer).append(".add(eAutoBean.as());");
			sb.append("\n").append("};");
			
		}
		
		
		sb.append("\n").append("AutoBean<").append(tc.getType()).append("> ").append(varName).append(" = ").append(ii.managerFactory).append(g.getName());
		sb.append("\n").append(varName).append(".as().setValue(").append(valueContainer).append(");");
		sb.append("\n").append(eVarName).append(" = com.google.web.bindery.autobean.shared.AutoBeanCodex.encode(").append(varName).append(").getPayload();");
		sb.append("\n").append("params.add(").append(eVarName).append(");");
		
		return sb.toString();
	}
	
}
