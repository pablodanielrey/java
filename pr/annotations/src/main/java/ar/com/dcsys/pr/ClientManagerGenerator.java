package ar.com.dcsys.pr;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

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

		return typeName.replace(".shared.", ".client.") + "Bean";
		
	}

}

