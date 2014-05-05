package ar.com.dcsys.gwt.auth.client.gin;

import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.auth.client.manager.AuthManagerBean;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AuthModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(AuthManager.class).to(AuthManagerBean.class).in(Singleton.class);

	}
	
}
