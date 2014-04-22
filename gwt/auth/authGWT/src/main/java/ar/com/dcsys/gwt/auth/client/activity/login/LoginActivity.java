package ar.com.dcsys.gwt.auth.client.activity.login;

import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.auth.client.ui.login.LoginView;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;


public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	private final LoginView view;
	private final AuthManager authManager;
	private EventBus eventBus;

	@Inject
	public LoginActivity(AuthManager authManager, LoginView view) {
		this.view = view;
		this.authManager = authManager;
	}
	
	@Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {

		this.eventBus = eventBus;

		panel.setWidget(view);
        view.clear();
        view.setPresenter(this);
    }
	
	private String getModuleUrl() {
		return "/personGWT/";
	}
	


	private void showMessage(String msg) {
		eventBus.fireEvent(new MessageDialogEvent(msg));
	}
	
	@Override
	public void login() {
		
		String username = view.getUser();
		String password = view.getPassword();

		authManager.login(username, password, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				// aca deberia redireccionar a la aplicacion principal que tiene el menu.
				Window.open(getModuleUrl(), "_self", "");		
			}
			
			@Override
			public void onFailure(Throwable t) {
				if (t != null) {
					showMessage(t.getMessage());
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
