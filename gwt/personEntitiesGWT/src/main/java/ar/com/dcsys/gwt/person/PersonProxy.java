package ar.com.dcsys.gwt.person;


public interface PersonProxy {

	public String getId();
	
	public String getName();
	public void setName(String n);
	
	public String getLastName();
	public void setLastName(String n);
	
	public String getDni();
	public void setDni(String dni);
	
	public String getAddress();
	public void setAddress(String addr);
	
	public String getCity();
	public void setCity(String city);
	
	public String getCountry();
	public void setCountry(String country);
	
	public String getGender();
	public void setGender(String gender);
	
	public String getStudentNumber();
	public void setStudentNumber(String number);
	
}
