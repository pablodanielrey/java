package ar.com.dcsys.gwt.assistance.client.gin;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManagerBean;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;

public class AssistanceModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();
	}
	
}
