package ar.com.dcsys.data.person;

public class PersonTypePostgraduate implements PersonType {

	private String type;
	private String id;
	
	public PersonTypePostgraduate() {
		type = PersonTypePostgraduate.class.getName();
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return "Posgrado";
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
