package ar.com.dcsys.gwt.auth.client.gin;

import ar.com.dcsys.gwt.auth.client.activity.login.LoginActivity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = AuthGinModule.class)
public interface Injector extends Ginjector {

	public EventBus eventBus();
	public LoginActivity loginActivity();
	
}
