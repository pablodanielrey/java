package ar.com.dcsys.gwt.person.shared;

public class PersonValueProxyImpl implements PersonValueProxy {

	private String id;
	private String name;
	private String lastName;
	private String dni;
	
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
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String ln) {
		lastName = ln;
	}

	@Override
	public String getDni() {
		return dni;
	}

	@Override
	public void setDni(String dni) {
		this.dni = dni;
	}

}
