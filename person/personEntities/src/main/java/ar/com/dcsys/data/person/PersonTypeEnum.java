package ar.com.dcsys.data.person;

public enum PersonTypeEnum {
	EXTERNAL("Externo",PersonTypeExternal.class.getName()),
	PERSONAL("Empleado",PersonTypePersonal.class.getName()),
	POSTGRADUATE("Posgrado",PersonTypePostgraduate.class.getName()),
	STUDENT("Estudiante",PersonTypeStudent.class.getName()),
	TEACHER("Docente",PersonTypeTeacher.class.getName());
	
	private String description;
	private String clazz;
	
	PersonTypeEnum(String description, String clazz) {
		this.description = description;
		this.clazz = clazz;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getClazz() {
		return clazz;
	}
}
