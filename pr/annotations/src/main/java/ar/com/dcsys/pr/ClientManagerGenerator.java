package ar.com.dcsys.pr;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;


/**
 * Clase que implementa el binding entre la clase generada por ATP de java y el GWT.create de gwt.
 * para que los clientes puedan acceder a la implementación de la clase.
 * por convención se toma que la interfaz debe ser generada en .shared.
 * el codigo generado queda en .client y en .server
 * con los nombres OriginalInterfaceName + Bean
 * 
 * Ej :
 * 
 * package ar.com.shared;
 * 
 * public interface Pepe {
 * .....
 * 
 *  debería retornar :
 *  
 *  ar.com.client.PepeBean
 *  
 * 
 * 
 * @author pablo
 *
 */
public class ClientManagerGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

		TypeOracle oracle = context.getTypeOracle();
		try {
			
			JType jtype = oracle.parse("ar.com.dcsys.pr.GwtClientManager<ar.com.dcsys.pr.shared.TestManager>");
			JParameterizedType ptype = jtype.isParameterized();
			String subType = ptype.getTypeArgs()[0].getQualifiedSourceName();
			
			return subType.replace(".shared.", ".client.") + "Bean";			
			
/*			JClassType type = oracle.getType(typeName);
			JParameterizedType ptype = type.isParameterized();
			String subType = ptype.getTypeArgs()[0].getName();
			
			return subType.replace(".shared.", ".client.") + "Bean";
*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnableToCompleteException();
		}
		
	}

}

