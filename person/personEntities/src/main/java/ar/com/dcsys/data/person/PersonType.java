package ar.com.dcsys.data.person;


public enum PersonType {
	
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
