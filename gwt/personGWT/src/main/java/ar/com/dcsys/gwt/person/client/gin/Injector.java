package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.auth.client.gin.AuthModelGinModule;
import ar.com.dcsys.gwt.message.client.MessageGinModule;
import ar.com.dcsys.gwt.person.client.activity.PersonActivityMapper;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;


@GinModules(value = {PersonGWTGinModule.class, 
					 WsGinModule.class, 
					 MessageGinModule.class, 
					 PersonModelGinModule.class,
					 AuthModelGinModule.class })
public interface Injector extends Ginjector {
	
	public AssistedInjectionFactory factory();
	
	public EventBus eventbus();

	public PersonActivityMapper personActivityMapper();
	
	public WebSocket ws();
	
}
