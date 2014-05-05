package ar.com.dcsys.gwt.manager.client.gin;

import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.ManagerFactory;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class ManagerGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(ManagerFactory.class).in(Singleton.class);
		bind(ManagerUtils.class);
		
	}
	
}
