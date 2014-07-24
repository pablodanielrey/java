package ar.com.dcsys.gwt.assistance.client.ui.person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PersonData extends Composite implements PersonDataView {

	private static PersonDataUiBinder uiBinder = GWT.create(PersonDataUiBinder.class);

	interface PersonDataUiBinder extends UiBinder<Widget, PersonData> {
	}

	private Presenter p;
	
	@UiField VerticalPanel messages;
	
	@Override
	public void showMessage(String msg) {
		if (msg == null) {
			return;
		}
		messages.add(new Label(msg));
	}	
	
	public PersonData() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}
	
	@UiHandler("persist")
	public void onPersist(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.persist();
	}
	
	@UiHandler("enroll")
	public void onEnroll(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.enroll();
	}

}
