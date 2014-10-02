package ar.com.dcsys.data.person;

public class PersonTypeTeacher implements PersonType {

	private String type;
	private String id;
	
	public PersonTypeTeacher() {
		type = PersonTypeTeacher.class.getName();
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return "Docente";
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

}
