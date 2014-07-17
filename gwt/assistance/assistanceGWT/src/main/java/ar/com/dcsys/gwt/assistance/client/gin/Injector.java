package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.auth.client.gin.AuthModelGinModule;
import ar.com.dcsys.gwt.person.client.gin.PersonModelGinModule;
import ar.com.dcsys.gwt.person.client.gin.PersonViewsGinModule;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;


@GinModules(value = {AssistanceGWTGinModule.class,
					 AssistanceModelGinModule.class,
					 WsGinModule.class,
					 PersonModelGinModule.class,
					 PersonViewsGinModule.class,
					 AuthModelGinModule.class})
public interface Injector extends Ginjector {
	
	public AssistedInjectionFactory factory();
	
	public EventBus eventbus();

	public AssistanceActivityMapper assistanceActivityMapper();
	
	public WebSocket ws();

	
	/// modulos de person ///
	
//	public EnrollModule enrollModule();
	
}
