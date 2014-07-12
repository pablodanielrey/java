package ar.com.dcsys.gwt.auth.client.ui.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Login extends Composite implements LoginView {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	public Login() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	private Presenter presenter ;

	@UiField TextBox user;
	@UiField PasswordTextBox password;

	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	@Override
	public void clear() {
		user.setText("");
		password.setText("");
	}

	@Override
	public String getUser() {
		return user.getText();
	}
	
	@Override
	public String getPassword() {
		return password.getText();

	}

	@UiHandler("accept")
	public void onAccept(ClickEvent event) {
		if (presenter != null) {
			presenter.login();
		}
	}
	
	
}

