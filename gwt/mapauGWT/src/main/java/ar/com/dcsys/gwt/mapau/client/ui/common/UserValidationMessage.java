package ar.com.dcsys.gwt.mapau.client.ui.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UserValidationMessage extends Composite implements UserValidationMessageView {

	private static UserValidationMessageUiBinder uiBinder = GWT.create(UserValidationMessageUiBinder.class);

	interface UserValidationMessageUiBinder extends	UiBinder<Widget, UserValidationMessage> {
	}

	
	@UiField(provided=true)Label message;
	@UiField(provided=true)CheckBox confirmation;
	@UiField(provided=true)Label confirmationLabel;
	@UiField(provided=true)Label cancel;
	@UiField(provided=true)Label commit;
	
	private Presenter presenter;
	
	public UserValidationMessage() {
		createMessage();
		createConfirmation();
		createActions();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void createMessage() {
		message = new Label();
		message.setText("");
	}
	
	private void createConfirmation() {
		confirmationLabel = new Label();
		confirmationLabel.setText("");
		
		confirmation = new CheckBox();
		confirmation.setValue(false);
	}
	
	private void createActions() {
		cancel = new Label();
		cancel.setText("Cancelar");
		cancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				presenter.cancel();
			}
		});
		
		commit = new Label();
		commit.setText("Aceptar");
		commit.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				if (confirmation.getValue()) {
					presenter.commit();
				}
			}
		});		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void clear() {
		message.setText("");
		confirmation.setValue(false);
	}

	@Override
	public void setMessage(String text) {
		message.setText(text);
	}

	@Override
	public void setConfirmationLabel(String text) {
		this.confirmationLabel.setText(text);
	}
}
