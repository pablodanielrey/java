package ar.com.dcsys.gwt.auth.client.gin;

import ar.com.dcsys.gwt.auth.client.activity.login.LoginActivity;
import ar.com.dcsys.gwt.auth.client.ui.login.LoginView;
import ar.com.dcsys.gwt.auth.client.ui.login2.Login;
import ar.com.dcsys.gwt.clientMessages.client.Message;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AuthGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		bind(Message.class).asEagerSingleton();

		bind(LoginView.class).to(Login.class).in(Singleton.class);

		bind(LoginActivity.class);
	}
	
}
