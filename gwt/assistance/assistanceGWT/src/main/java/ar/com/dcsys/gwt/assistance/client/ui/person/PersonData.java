package ar.com.dcsys.gwt.assistance.client.ui.person;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PersonData extends Composite implements PersonDataView {

	private static PersonDataUiBinder uiBinder = GWT.create(PersonDataUiBinder.class);

	interface PersonDataUiBinder extends UiBinder<Widget, PersonData> {
	}

	private Presenter p;
	
	@UiField Label messages;
	
	@Override
	public void showMessage(String msg) {
		if (msg == null) {
			msg = "";
		}
		messages.setText(msg);
	}	
	
	public PersonData() {
		createPersonData();
		createPersist();
		createEnroll();
		createTransfer();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField(provided=true)Label personData;
	private void createPersonData() {
		personData = new Label();
	}
		
	@Override
	public void setPersonData(Person person) {
		String text = "";
		if (person != null) {
			text = person.getLastName() + ", " + person.getName() + " (" + person.getDni() + ")";
		}
		personData.setText(text);
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}
	
	@UiField(provided=true)Label persist;
	private void createPersist() {
		persist = new Label();
		persist.setText("Transferir Persona");
		persist.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.persist();
			}
		});
	}
	
	@UiField(provided=true)Label enroll;
	private void createEnroll() {
		enroll = new Label();
		enroll.setText("Enrolar");
		enroll.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.enroll();
			}
		});
	}
	
	@UiField(provided=true)Label transfer;
	private void createTransfer() {
		transfer = new Label();
		transfer.setText("Transferir Huellas");
		transfer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.transferFingerprints();				
			}
		});
		
	}
}
