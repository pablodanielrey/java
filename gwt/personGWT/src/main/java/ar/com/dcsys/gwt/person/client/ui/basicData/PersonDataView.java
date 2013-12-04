package ar.com.dcsys.gwt.person.client.ui.basicData;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypesView;

import com.google.gwt.user.client.ui.IsWidget;

public interface PersonDataView extends IsWidget {
	
	public PersonTypesView getPersonTypesView();
	
	public void setPresenter(Presenter p);
	public void clear();
	
	public void setNameReadOnly(boolean v);
	public String getName();
	public void setName(String name);
	
	public void setLastNameReadOnly(boolean v);
	public String getLastName();
	public void setLastName(String lastName);
	
	public void setMailVisible(boolean v);
	public void setMailReadOnly(boolean v);
	public String getMail();
	public void setMail(String mail);
	
	public Gender getGender();
	public void setGender(Gender gender);
	
	public String getCountry();
	public void setCountry(String country);
	
	public String getCity();
	public void setCity(String city);
	
	public String getTelephone();
	public void setTelephone(String tel);
	
	public String getAddress();
	public void setAddress(String addr);
	
	public void setDniReadOnly(boolean v);
	public String getDni();
	public void setDni(String dni);

	///// tipos alumnos /////
	
	public void setStudentNumberReadOnly(boolean v);
	public String getStudentNumber();
	public void setStudentNumber(String num);
	public void setStudentDataVisible(boolean value);

	
	
	public interface Presenter {
		public void persist();
	}
	
}
