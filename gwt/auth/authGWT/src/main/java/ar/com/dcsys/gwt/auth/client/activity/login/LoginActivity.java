package ar.com.dcsys.gwt.auth.client.activity.login;

import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.auth.client.ui.logged.LoggedView;
import ar.com.dcsys.gwt.auth.client.ui.login.LoginView;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;


public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	private final LoginView loginView;
	private final LoggedView loggedView;
	private final AuthManager authManager;
	private EventBus eventBus;
	private AcceptsOneWidget panel;

	@Inject
	public LoginActivity(AuthManager authManager, LoginView loginView, LoggedView loggedView) {
		this.loginView = loginView;
		this.loggedView = loggedView;
		this.authManager = authManager;
	}
	
	@Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {

		this.eventBus = eventBus;
		this.panel = panel;
		
        loginView.clear();
        loginView.setPresenter(this);
        
        loggedView.clear();
        
		authManager.isAuthenticated(new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean t) {

				if (t == null || !(t.booleanValue())) {
					// no esta logueado.
					panel.setWidget(loginView);
				} else {
					// esta logueado
					panel.setWidget(loggedView);
				}
				
			}
			@Override
			public void onError(String t) {
				Window.alert(t);
			}
		});
    }


	private void showMessage(String msg) {
		eventBus.fireEvent(new MessageDialogEvent(msg));
	}
	
	@Override
	public void login() {
		
		String username = loginView.getUser();
		String password = loginView.getPassword();

		authManager.login(username, password, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {	
				panel.setWidget(loggedView);
			}
			
			@Override
			public void onError(String t) {
				if (t != null) {
					showMessage(t);
				} else {
					showMessage("Error autentificando al usuario");
				}
			}
		});
		
		
		
	}
	
/*
	public void checkUser() {
		
		login.setUser(view.getUser());
		login.setPass(view.getPassword());
		
		AutoBean<Login> bean = AutoBeanUtils.getAutoBean(login);

		String serializeLogin = AutoBeanCodex.encode(bean).getPayload();

		
		
		if (!result){
			Window.alert(serializeLogin); 
		} else {
			Window.alert(serializeLogin); 
		}
		
		
		AutoBean<Login> alogin = AutoBeanCodex.decode(factory, Login.class, serializeLogin);
		Login llogin = alogin.as();
		
		Window.alert(llogin.getUser() + " " + llogin.getPass());
		
	}
*/	
	
	
}
