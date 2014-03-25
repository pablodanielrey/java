package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.manager.AssistanceManager;
import ar.com.dcsys.gwt.assistance.client.manager.AssistanceManagerBean;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AssistanceModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(AssistanceManager.class).to(AssistanceManagerBean.class).in(Singleton.class);

	}
	
}
