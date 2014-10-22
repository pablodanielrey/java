package ar.com.dcsys.gwt.person.client.ui.basicData;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypesView;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
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
		//mail.setText("");
		clearMails();
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
		
		createMails();
		
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
	public void setDniReadOnly(boolean v) {
		dni.setReadOnly(v);
	}

	@Override
	public void setStudentNumberReadOnly(boolean v) {
		studentNumber.setReadOnly(v);
	}
	
	/**
	 * MAILS
	 */

	@UiField FlowPanel mailData;
	//@UiField TextBox mail;

	@Override
	public void setMailReadOnly(boolean v) {
		//mail.setReadOnly(v);
	}
	
	@Override
	public void setMail(String mail) {
		//this.mail.setText(mail);
	}
	
	@Override
	public String getMail() {
		//return mail.getText();
		return "";
	}
	
	private List<Mail> mailsData = new ArrayList<Mail>();
	
	@UiField(provided=true) DataGrid<Mail> mails;
	private void createMails() {
		createPanelNewMail();
		
		mails = new DataGrid<Mail>();
		
		final EditTextCell nameCell = new EditTextCell();
		Column<Mail,String> mailName = new Column<Mail, String> (nameCell) {
			@Override
			public String getValue(Mail object) {
				if (object == null) {
					return "";
				}
				return object.getMail();
			}
		}; 
		
		mailName.setFieldUpdater(new FieldUpdater<Mail,String>() {
			@Override
			public void update(int index, Mail object, String value) {
				if (p == null || value == null || value.trim().equals("")) {
					nameCell.clearViewData(object);
					mails.redraw();
					return;
				}
				p.update(object,value);
			}
		});
		
		ActionCell<Mail> removeMailCell = new ActionCell<Mail>("Eliminar", new Delegate<Mail>() {
			@Override
			public void execute(Mail object) {
				if (p == null) {
					return;
				}
				p.remove(object);
			}
		});
		IdentityColumn<Mail> removeColumn = new IdentityColumn<Mail>(removeMailCell);		
		mails.addColumn(mailName, "Mail");
		mails.addColumn(removeColumn, "Eliminar");
	}
		
	@Override
	public List<Mail> getMails() {
		return mailsData;
	}
	
	@Override
	public void setMails(List<Mail> mails) {
		mailsData.clear();
		if (mails != null) {
			mailsData.addAll(mails);
		}
		this.mails.setRowData(mailsData);
	}
	
	private void clearMails() {
		mailsData.clear();
		mails.setRowCount(0);
		mails.setRowData(mailsData);
		
		newMail.setText("");
		displayPanelNewMail(false);		
	}
	
	@UiField(provided=true) Label labelAddMail;
	@UiField(provided=true) FlowPanel panelNewMail;
	@UiField(provided=true) TextBox newMail;
	@UiField(provided=true) Label submitNewMail;
	
	private void createPanelNewMail() {
		labelAddMail = new Label();
		labelAddMail.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				displayPanelNewMail(true);
				newMail.setText("");
			}
		});
		
		panelNewMail = new FlowPanel();
		setDisplay(panelNewMail.getElement(),Display.NONE);
		
		newMail = new TextBox();
		newMail.setText("");
		
		submitNewMail = new Label();
		submitNewMail.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				p.update(null, newMail.getText());
				displayPanelNewMail(false);
			}
		});
	}
	
	private void displayPanelNewMail(boolean v) {
		if (v) {
			setDisplay(labelAddMail.getElement(),Display.NONE);
			setDisplay(panelNewMail.getElement(),Display.BLOCK);
		} else {
			setDisplay(labelAddMail.getElement(),Display.BLOCK);
			setDisplay(panelNewMail.getElement(),Display.NONE);			
		}
	}
	
	private void setDisplay(Element e, Display d) {
		e.getStyle().setDisplay(d);
	}
	
}
