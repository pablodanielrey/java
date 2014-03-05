package ar.com.dcsys.gwt.auth.client.ui.login2;

import ar.com.dcsys.gwt.auth.client.ui.login.LoginView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class Login extends Composite implements LoginView {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	
	@UiField Button login;
	@UiField TextBox username;
	@UiField TextBox password;

	private Presenter p;
	
	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	public Login() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void clear() {
		username.setText("");
		password.setText("");
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.p = presenter;
	}

	@Override
	public String getUser() {
		return username.getText();
	}

	@Override
	public String getPassword() {
		return password.getText();
	}

	@UiHandler("login")
	void onLoginClick(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.login();
	}
}
