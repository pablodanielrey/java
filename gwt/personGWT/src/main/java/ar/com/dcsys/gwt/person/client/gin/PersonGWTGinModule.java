package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.manager.AuthManager;
import ar.com.dcsys.gwt.person.client.manager.AuthManagerBean;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonData;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.assistance.PersonAssistanceData;
import ar.com.dcsys.gwt.person.client.ui.assistance.PersonAssistanceDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataUser;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.client.ui.manage.ManagePersons;
import ar.com.dcsys.gwt.person.client.ui.manage.ManagePersonsView;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypes;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypesView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;


public class PersonGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(AuthManager.class).to(AuthManagerBean.class).in(Singleton.class);
		
		bind(PersonTypesView.class).to(PersonTypes.class).in(Singleton.class);
		bind(PersonDataView.class).to(PersonDataUser.class).in(Singleton.class);
		bind(PersonAssistanceDataView.class).to(PersonAssistanceData.class).in(Singleton.class);
		bind(UpdatePersonDataView.class).to(UpdatePersonData.class).in(Singleton.class);
		bind(ManagePersonsView.class).to(ManagePersons.class).in(Singleton.class);

		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(AssistedInjectionFactory.class));
	}
	
}
