package ar.com.dcsys.data.person;

public class PersonTypePersonal implements PersonType {

	private String id;
	private String type;
	
	public PersonTypePersonal() {
		type = PersonTypePersonal.class.getName();
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return "Empleado";
	}
	
	@Override
	public String getType() {
		return type;
	}
}
