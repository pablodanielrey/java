package ar.com.dcsys.pr;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class Param {

	String name;
	TypeMirror typeMirror;
	TypeKind typeKind;
	
	public String getType() {
		return typeMirror.toString();
	}
	
	public String getName() {
		return name;
	}
	
}
