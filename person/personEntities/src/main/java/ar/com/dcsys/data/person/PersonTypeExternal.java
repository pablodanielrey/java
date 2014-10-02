package ar.com.dcsys.data.person;

public class PersonTypeExternal implements PersonType {

	private String type;
	private String id;
	
	public PersonTypeExternal() {
		type = PersonTypeExternal.class.getName();
	}
	
	@Override
	public String getType() {
		return type;
	}
	@Override
	public String getName() {
		return "Externo";
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
