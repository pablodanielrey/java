package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({AssistanceModelGinModule.class, WsGinModule.class})
public interface Injector extends Ginjector {
	
	WebSocket getWebSocket();
	EventBus getEventBus();
	
	PeriodsManagerProvider periodsManagerProvider();
	JustificationsManagerProvider justificationsManagerProvider();
	
}
