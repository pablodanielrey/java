package ar.com.dcsys.data.group;

public enum GroupType {
	ALIAS("Alias"), OFFICE("Oficina"), OU("Unidad Organizacional"), POSITION("Cargo"), PROFILE("Perfil"), TIMETABLE("Horario");
	
	private final String description;
	
	private GroupType(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return description;
	}
}
