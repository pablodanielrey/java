package ar.com.dcsys.gwt.mapau.client.gin;

import ar.com.dcsys.gwt.message.client.MessageGinModule;
import ar.com.dcsys.gwt.person.client.gin.PersonModelGinModule;
import ar.com.dcsys.gwt.ws.client.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = { MapauModelGinModule.class,
					  MapauGwtGinModule.class,
					  WsGinModule.class, 
					  MessageGinModule.class, 
					  PersonModelGinModule.class })
public interface Injector extends Ginjector {

	public EventBus eventBus();
	
	public MapauAssistedInjectionFactory factory();
	
}
