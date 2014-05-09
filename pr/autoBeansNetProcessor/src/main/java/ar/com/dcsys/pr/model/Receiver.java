package ar.com.dcsys.pr.model;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import ar.com.dcsys.pr.Utils;

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

public class Receiver {

	private final String name;
	private final TypeMirror typeMirror;
	private final TypeKind typeKind;
	
	public Receiver(String name, TypeMirror mirror) {
		this.name = name;
		this.typeMirror = mirror;
		this.typeKind = mirror.getKind();
	}
	
	public String getType() {
		return typeMirror.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPackage() {
		String t = getType();
		if (t.contains("<")) {
			String st = t.substring(0,t.indexOf("<")); 
			return st.substring(0, st.lastIndexOf("."));
		} else {
			return t.substring(0, t.lastIndexOf("."));
		}
	}
	
	
	public static void process(Method method, VariableElement ve) {
		String name = ve.getSimpleName().toString();
		Receiver param = new Receiver(name,ve.asType());
		method.setReceiver(param);
		
		Factory factory = method.getManager().getFactory();
		factory.createGetter(Utils.getInteralType(param.getType()));
	}
	
}
