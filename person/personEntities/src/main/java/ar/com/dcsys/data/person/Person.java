package ar.com.dcsys.data.person;


import java.util.Date;
import java.util.List;


public interface Person {

	public String getId();
	public void setId(String id);
	
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
	
	public Gender getGender();
	public void setGender(Gender gender);
	
	public Date getBirthDate();
	public void setBirthDate(Date d);
	
	public String getStudentNumber();
	public void setStudentNumber(String number);
	
	public List<PersonType> getTypes();
	public void setTypes(List<PersonType> types);
	
	public List<Telephone> getTelephones();
	public void setTelephones(List<Telephone> tels);

}
