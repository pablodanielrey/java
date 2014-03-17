package ar.com.dcsys.gwt.assistance.client.ui.auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

public class PinAuthData extends Composite implements PinAuthDataView {

	private static PinAuthDataUiBinder uiBinder = GWT.create(PinAuthDataUiBinder.class);

	interface PinAuthDataUiBinder extends UiBinder<Widget, PinAuthData> {
	}
	
	@UiField DialogBox messageDialog;
	@UiField Label message;
	@UiField Button okMessage;
	
	@UiField PasswordTextBox pin;
	@UiField PasswordTextBox pin2; 
	
	@UiField Button save;
	@UiField Label pinError;
	@UiField Label pin2Error;
	
	private Presenter p;


	public PinAuthData() {
		initWidget(uiBinder.createAndBindUi(this));
		
		KeyUpHandler kuh = new KeyUpHandler() {			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String pinStr1 = pin.getText();
				String pinStr2 = pin2.getText();
				
				if (pinStr1 != null && pinStr2 != null && pinStr1.equals(pinStr2)) {
					save.setEnabled(true);
					pinError.setVisible(false);
				} else {
					save.setEnabled(false);
					pinError.setVisible(true);
				}
			}
		};
		
		pin.addKeyUpHandler(kuh);
		pin2.addKeyUpHandler(kuh);
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}
	
	
	@Override
	public void clear() {
		pin.setText("");
		pin2.setText("");
		
		clearErrors();
	}
	
	private void clearErrors() {
		pinError.setVisible(false);
		pin2Error.setVisible(false);
	}
	
	@Override
	public String getPin() {
		return pin.getText();
	}
	
	private boolean checkConstraints() {
		boolean ok = true;
		if (pin.getText() != null) {
			//isEmpty
			if (pin.getText().trim().equals("")) {
				pinError.setVisible(true);
				ok = false;
			}
		}
		return ok;
	}
	
	@UiHandler("okMessage")
	public void onOkMessage(ClickEvent event) {
		messageDialog.hide();
	}
	
	@Override
	public void showMessage(String message) {
		this.message.setText(message);
		messageDialog.center();
		messageDialog.setVisible(true);
	}
	
	@UiHandler("save")
	public void onSave(ClickEvent event) {
		clearErrors();
		if (p == null) {
			return;
		}
		if (checkConstraints()) {
			p.persist();
		}
	}

}
