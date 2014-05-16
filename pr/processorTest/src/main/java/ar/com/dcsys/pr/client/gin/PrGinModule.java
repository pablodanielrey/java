package ar.com.dcsys.pr.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;

public class PrGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();
		
	}
	
}

