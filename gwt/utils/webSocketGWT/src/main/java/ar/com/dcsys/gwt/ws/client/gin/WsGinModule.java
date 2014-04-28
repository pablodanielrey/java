package ar.com.dcsys.gwt.ws.client.gin;

import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class WsGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(WebSocket.class).to(WebSocketImpl.class).in(Singleton.class);
		
	}

}
