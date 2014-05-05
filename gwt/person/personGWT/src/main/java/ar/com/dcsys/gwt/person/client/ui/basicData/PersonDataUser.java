package ar.com.dcsys.gwt.person.client.ui.basicData;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypesView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PersonDataUser extends Composite implements PersonDataView {

	private static AuthDataUiBinder uiBinder = GWT.create(AuthDataUiBinder.class);

	interface AuthDataUiBinder extends UiBinder<Widget, PersonDataUser> {
	}
	
	@UiField PersonDataViewResources res;
	
	@UiField TextBox name;
	@UiField TextBox lastName;
	@UiField TextBox dni;
	@UiField TextBox address;
	@UiField TextBox city;
	@UiField TextBox country;
	@UiField TextBox studentNumber;
	
	@UiField(provided=true) RadioButton masculineGender;
	@UiField(provided=true) RadioButton femenineGender;
	
	@UiField TextBox telephone;
	@UiField FlowPanel mailData;
	@UiField TextBox mail;
	
	@UiField FlowPanel studentData;					// panel que contiene los datos de la persona tipo alumno

	@UiField(provided=true) PersonTypesView personTypes;
	
	private static final String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final RegExp mailExp = RegExp.compile(pattern);

	
	@Override
	public PersonTypesView getPersonTypesView() {
		return personTypes;
	}
	
	@Override
	public void clear() {
		name.setText("");
		lastName.setText("");
		dni.setText("");
		address.setText("");
		city.setText("");
		country.setText("");
		
		studentNumber.setText("");
		
		masculineGender.setValue(false);
		femenineGender.setValue(false);
		
		telephone.setText("");
		mail.setText("");
	}
	
	
	@Override
	public void setMailVisible(boolean v) {
		mailData.setVisible(v);
	}
		
	private Presenter p;
	
	@Inject
	public PersonDataUser(PersonTypesView personTypes) {

		masculineGender = new RadioButton("rg","Masculino");
		masculineGender.setValue(false);
		femenineGender = new RadioButton("rg","Femenino");
		femenineGender.setValue(false);
		
		this.personTypes = personTypes;
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}
	
	@Override
	public void setName(String name) {
		this.name.setText(name);
	}	
	
	@Override
	public String getName() {
		return name.getText();
	}
	
	@Override
	public void setLastName(String lastName) {
		this.lastName.setText(lastName);
	}
	
	@Override
	public String getLastName() {
		return lastName.getText();
	}
	
	@Override
	public void setDni(String dni) {
		this.dni.setText(dni);
	}
	
	@Override
	public String getDni() {
		return dni.getText();
	}
	
	@Override
	public void setAddress(String addr) {
		address.setText(addr);
	}
	
	@Override
	public String getAddress() {
		return address.getText();
	}
	
	@Override
	public void setMail(String mail) {
		this.mail.setText(mail);
	}
	
	@Override
	public String getMail() {
		return mail.getText();
	}
	
	@Override
	public void setGender(Gender gender) {
		masculineGender.setValue(false);
		femenineGender.setValue(false);
		
		if (gender == null) {
			return;
		}
		
		switch (gender) {
		case M:
			masculineGender.setValue(true);
			break;
		case F:
			femenineGender.setValue(true);
			break;
		}
	}
	
	@Override
	public Gender getGender() {
		if (masculineGender.getValue()) {
			return Gender.M;
		}
		if (femenineGender.getValue()) {
			return Gender.F;
		}
		return null;
	}

	@Override
	public void setCountry(String country) {
		this.country.setText(country);
	}

	@Override
	public String getCountry() {
		return country.getText();
	}

	@Override
	public void setCity(String city) {
		this.city.setText(city);
	}
	
	@Override
	public String getCity() {
		return city.getText();
	}


	@Override
	public void setTelephone(String tel) {
		this.telephone.setText(tel);
	}
	
	@Override
	public String getTelephone() {
		return telephone.getText();
	}
	
	
	//////////// datos de alumno /////////////////
	
	@Override
	public void setStudentNumber(String num) {
		studentNumber.setText(num);
	}
	
	@Override
	public String getStudentNumber() {
		return studentNumber.getText();
	}
	
	@Override
	public void setStudentDataVisible(boolean value) {
		studentData.setVisible(value);
	}

	@Override
	public void setNameReadOnly(boolean v) {
		name.setReadOnly(v);
	}

	@Override
	public void setLastNameReadOnly(boolean v) {
		lastName.setReadOnly(v);
	}

	@Override
	public void setMailReadOnly(boolean v) {
		mail.setReadOnly(v);
	}

	@Override
	public void setDniReadOnly(boolean v) {
		dni.setReadOnly(v);
	}

	@Override
	public void setStudentNumberReadOnly(boolean v) {
		studentNumber.setReadOnly(v);
	}

}
