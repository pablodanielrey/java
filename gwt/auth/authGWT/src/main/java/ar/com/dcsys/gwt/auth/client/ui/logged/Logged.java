package ar.com.dcsys.gwt.auth.client.ui.logged;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Logged extends Composite implements LoggedView {

	private static LoggedUiBinder uiBinder = GWT.create(LoggedUiBinder.class);

	interface LoggedUiBinder extends UiBinder<Widget, Logged> {
	}

	
	@UiField Label username;
		
	
	public Logged() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void clear() {
		username.setText("");
	}
	
	@Override
	public void setUser(String username) {
		this.username.setText(username);	
	}

}
