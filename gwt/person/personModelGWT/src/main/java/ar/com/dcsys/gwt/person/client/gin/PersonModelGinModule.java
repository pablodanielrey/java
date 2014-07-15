package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.manager.GroupsManager;
import ar.com.dcsys.gwt.person.client.manager.GroupsManagerBean;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManager;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManagerBean;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManagerBean;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class PersonModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(PersonsManager.class).to(PersonsManagerBean.class).in(Singleton.class);
		bind(MailChangesManager.class).to(MailChangesManagerBean.class).in(Singleton.class);
		bind(GroupsManager.class).to(GroupsManagerBean.class).in(Singleton.class);
		
	}

}
