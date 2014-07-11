package ar.com.dcsys.gwt.menu.client.gin;

import ar.com.dcsys.gwt.menu.client.activity.MenuActivityMapper;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = { MenuGWTGinModule.class,
					  WsGinModule.class
					})
public interface Injector extends Ginjector {
	
	public AssistedInjectionFactory factory();

	public EventBus eventBus();
	
	public MenuActivityMapper menuActivityMapper();
	
	public WebSocket ws();
}
