package ar.com.dcsys.gwt.person.client.ui.assistance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class PersonAssistanceData extends Composite implements PersonAssistanceDataView {

	private static PersonAssistanceDataUiBinder uiBinder = GWT.create(PersonAssistanceDataUiBinder.class);

	interface PersonAssistanceDataUiBinder extends
			UiBinder<Widget, PersonAssistanceData> {
	}

	@UiField PasswordTextBox pin;
	@UiField TextArea notes;
	
	public PersonAssistanceData() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clear() {
		pin.setValue("");
		notes.setText("");
	}

	@Override
	public String getPinNumber() {
		return pin.getValue();
	}

	@Override
	public void setPinNumber(String pin) {
		this.pin.setValue(pin);
	}

	@Override
	public void setReadOnly(boolean v) {
		pin.setReadOnly(v);
		notes.setReadOnly(v);
	}
	
	@Override
	public String getNotes() {
		return notes.getText();
	}
	
	@Override
	public void setNotes(String notes) {
		if (notes == null) {
			this.notes.setText("");
		}
		this.notes.setText(notes);
	}

}
