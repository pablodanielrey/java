package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManagerBean;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManagerBean;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManagerBean;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AssistanceModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(JustificationsManager.class).to(JustificationsManagerBean.class).in(Singleton.class);
		bind(PeriodsManager.class).to(PeriodsManagerBean.class).in(Singleton.class);
		bind(PersonsManager.class).to(PersonsManagerBean.class).in(Singleton.class);
	}
	
}
