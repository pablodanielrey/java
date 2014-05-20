package ar.com.dcsys.gwt.auth.client.gin;

import ar.com.dcsys.gwt.auth.client.activity.login.LoginActivity;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = { AuthGinModule.class,
		 			  WsGinModule.class,
					  AuthModelGinModule.class })
public interface Injector extends Ginjector {

	public EventBus eventBus();
	public LoginActivity loginActivity();
	public AuthManager authManager();
	
	public WebSocket ws();
	
}
