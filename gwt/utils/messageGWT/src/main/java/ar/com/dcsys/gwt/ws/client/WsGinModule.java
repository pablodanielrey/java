package ar.com.dcsys.gwt.ws.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class WsGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(WebSocket.class).to(WebSocketImpl.class).in(Singleton.class);
		
	}

}
