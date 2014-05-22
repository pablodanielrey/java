package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Representa las clases receivers de los mensajes.
 * siempre es el último parámetro de la lista del método y esta en el formato :
 * 
 * ar.com.dcsys.gwt.manager.shared.Receiver<tipo>
 * 
 * donde tipo puede ser las siguientes alternativas :
 * 
 * t definido por el usuario
 * t tipo primitivo (java.lang.x)
 * List<tipo primitivo>
 * List<Tipo definido por el usuario>
 * 
 * 
 * @author pablo
 *
 */

public class Receiver extends Param {

	private final Param internalParam;
	
	public Receiver(VariableElement ve, ProcessingEnvironment env) {
		super(ve,env);
		internalParam = Param.extractInternalParam(this,env);		
	}
	
	public static void process(Method method, VariableElement ve, ProcessingEnvironment env) {
		Receiver param = new Receiver(ve,env);
		method.setReceiver(param);
		
		Param p2 = param.getInternalParam();
		
		Factory factory = method.getManager().getFactory();
		factory.createGetter(p2,env);
	}
	
	
	public Param getInternalParam() {
		return internalParam;
	}
	
	
}
