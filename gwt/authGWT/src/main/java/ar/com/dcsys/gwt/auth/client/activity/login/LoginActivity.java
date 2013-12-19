package ar.com.dcsys.gwt.auth.client.activity.login;

import ar.com.dcsys.gwt.auth.client.ui.login.LoginView;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;


public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	private LoginView view;
	private EventBus eventBus;

	@Inject
	public LoginActivity(LoginView view) {
		this.view = view;
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
	
	private String getAuthUrl() {
		return GWT.getHostPageBaseURL() + "AuthGWT.html";
	}

	private void showMessage(String msg) {
		eventBus.fireEvent(new MessageDialogEvent(msg));
	}
	
	@Override
	public void login() {
		
		String username = view.getUser();
		String password = view.getPassword();

		String url = getAuthUrl();
		
		// envío mediante post asi pasa por el servlet de shiro y es procesado por la autentificacion.
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,url);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		StringBuilder sb = new StringBuilder();
		sb.append("username").append("=").append(URL.encode(username));
		sb.append("&");
		sb.append("password").append("=").append(URL.encode(password));
		
		try {
			builder.sendRequest(sb.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (Response.SC_OK == response.getStatusCode()) {
						
						Window.open(getModuleUrl(), "_self", "");
						
					} else {
						showMessage("Error, verifique los datos y vuelva a intentarlo");
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					showMessage("Error de comunicacion con el servidor");
				}
			});
		} catch (RequestException e) {
			showMessage("Error de comunicación con el servidor");
		}
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
