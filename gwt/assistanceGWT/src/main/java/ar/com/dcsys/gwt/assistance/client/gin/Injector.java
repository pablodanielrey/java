package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.manager.client.gin.ManagerGinModule;
import ar.com.dcsys.gwt.message.client.MessageGinModule;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;


@GinModules(value = {AssistanceGWTGinModule.class, 
					 WsGinModule.class, 
					 MessageGinModule.class,
					 ManagerGinModule.class/*,
					 AuthModelGinModule.class */})
public interface Injector extends Ginjector {
	
	public AssistedInjectionFactory factory();
	
	public EventBus eventbus();

	public AssistanceActivityMapper assistanceActivityMapper();
	
	public WebSocket ws();
	
}
