package ar.com.dcsys.pr.model;

import java.util.UUID;

import ar.com.dcsys.pr.Utils;

public class TypesEncoderDecoder {

	/**
	 * Decodifica un tipo "type" codificado dentro de la variable "encoded" y lo deja en una nueva variable de ese mismo tipo llamada "decoded"
	 * @param factory
	 * @param managerFactory
	 * @param type
	 * @param encoded
	 * @param decoded
	 * @return
	 */
	public static String decodeResponse(Factory factory, String managerFactory, Param param, String encoded, String decoded) {

		String varName = "a" + UUID.randomUUID().toString().replace("-", "");
		
		String type = param.getType();
		
		Getter g = factory.findByType(type);
		TypeContainer tc = g.getTypeContainer();
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName)
					   .append("= com.google.web.bindery.autobean.shared.AutoBeanCodex.decode(").append(managerFactory).append(",").append(tc.getType()).append(".class,").append(encoded).append(");");
		
		if (param.isPrimitive() || param.isList()) {
			String auxVarName = "aux" + UUID.randomUUID().toString().replace("-", "");
			sb.append("\n").append(tc.getType()).append(" ").append(auxVarName).append(" = ").append(varName).append(".as();");
			sb.append("\n").append(type).append(" ").append(decoded).append(" = ").append(auxVarName).append(".getValue();");
		} else {
			sb.append("\n").append(type).append(" ").append(decoded).append(" = ").append(varName).append(".as();");
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 * Genera el codigo para codificar una variable de cierto tipo.
	 * @param factory
	 * @param managerFactory
	 * @param type
	 * @param paramName
	 * @return
	 */
	public static String encode(Factory factory, String managerFactory, Param param, String paramName, String eVarName) {
		
		StringBuilder sb = new StringBuilder();
		
		String varName = "a" + UUID.randomUUID().toString().replace("-", "");
		String valueContainer = paramName;
		
		
		String type = param.getType();
		
		Getter g = factory.findByType(type);
		TypeContainer tc = g.getTypeContainer();

		
		// chequeo si es una lista de tipos definidos por usuario (o sea no de tipos primitivos.)
		if (param.isDeclaredTypeList()) {
			// hago wrapping de los valores de la lista.
			
			String subType = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
			valueContainer = "a" + UUID.randomUUID().toString().replace("-", "");
			
			sb.append("\n").append(Utils.ident(8)).append("java.util.List<").append(subType).append("> ").append(valueContainer).append(" = new java.util.ArrayList<>();");
			sb.append("\n").append(Utils.ident(8)).append("for (").append(subType).append(" e : ").append(paramName).append(") {");
			
			Getter get = factory.findByType(subType);
			if (get == null) {
				throw new RuntimeException("No se puede encontrar el getter para el tipo : " + subType);
			}
			String getName = get.getName();

			
			sb.append("\n").append(Utils.ident(10)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(subType).append("> eAutoBean = ").append(managerFactory).append(".").append(getName).append("(e);");
			sb.append("\n").append(Utils.ident(10)).append(valueContainer).append(".add(eAutoBean.as());");
			sb.append("\n").append(Utils.ident(8)).append("};");
			
		}
		

		if (param.isList() || param.isPrimitive()) {
			
			// se generaron containers. asi que se setea el valor.
			sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName).append(" = ").append(managerFactory).append(".").append(g.getName()).append("();");
			sb.append("\n").append(Utils.ident(8)).append(varName).append(".as().setValue(").append(valueContainer).append(");");
			
		} else {

			// es un tipo definido por el usuario asi que no se genera container. se usa el AutoBean<Tipo>
			sb.append("\n").append(Utils.ident(8)).append("com.google.web.bindery.autobean.shared.AutoBean<").append(tc.getType()).append("> ").append(varName).append(" = ").append(managerFactory).append(".").append(g.getName()).append("(").append(valueContainer).append(");");
			
		}
		
		
		sb.append("\n").append(Utils.ident(8)).append("String ").append(eVarName).append(" = com.google.web.bindery.autobean.shared.AutoBeanCodex.encode(").append(varName).append(").getPayload();");
		
		return sb.toString();
	}	
	
	
	
	
	
	public static String encode(Manager manager, Manager.InstanceInfo ii, Param param) {
		
		String eVarName = "a" + UUID.randomUUID().toString().replace("-", "");
		
		StringBuilder sb = new StringBuilder();
		sb.append(encode(manager.getFactory(),ii.managerFactory,param,param.getName(),eVarName));
		sb.append("params.add(").append(eVarName).append(");");
		
		return sb.toString();
	}
	
}
