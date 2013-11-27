package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManagerBean;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonData;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataUser;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypes;
import ar.com.dcsys.gwt.person.client.ui.types.PersonTypesView;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class PersonGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
	
		bind(PersonTypesView.class).to(PersonTypes.class).in(Singleton.class);
		bind(PersonDataView.class).to(PersonDataUser.class).in(Singleton.class);
		bind(UpdatePersonDataView.class).to(UpdatePersonData.class).in(Singleton.class);
		
		bind(PersonsManager.class).to(PersonsManagerBean.class).in(Singleton.class);
	}
	
}
