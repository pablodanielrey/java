package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.message.client.MessageGinModule;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.ws.client.WsGinModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = {PersonGWTGinModule.class, WsGinModule.class, MessageGinModule.class})
public interface Injector extends Ginjector {
	
	public PersonDataView personDataView();
	public UpdatePersonDataView updatePersonDataView();
	
	public PersonFactory personFactory();
	public PersonsManager personsManager();
	
}
