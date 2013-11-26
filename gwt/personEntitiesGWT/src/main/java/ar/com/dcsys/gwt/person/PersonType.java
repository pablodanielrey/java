package ar.com.dcsys.gwt.person;

import java.io.Serializable;

public enum PersonType implements Serializable {
	
	PERSONAL("No Docente"), POSTGRADUATE("Posgrado"), STUDENT("Estudiante"), TEACHER("Docente"), EXTERNAL("Visitante");
		
	private String description;
	
	private PersonType() {
		// por ser serializable
	}

	private PersonType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
