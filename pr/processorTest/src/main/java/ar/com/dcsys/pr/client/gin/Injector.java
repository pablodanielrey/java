package ar.com.dcsys.pr.client.gin;

import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({PrGinModule.class, WsGinModule.class})
public interface Injector extends Ginjector {
	
	WebSocket getWebSocket();
	EventBus getEventBus();
	
}
