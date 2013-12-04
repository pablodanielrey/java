package ar.com.dcsys.gwt.ws.server;

import javax.websocket.server.ServerEndpointConfig;

public class WebsocketsConfigurator extends ServerEndpointConfig.Configurator {

	private static Websockets ws;
	
	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		if (Websockets.class.equals(endpointClass)) {
			if (ws == null) {
				ws = new Websockets();
			}
			return (T)ws;
		}
		return super.getEndpointInstance(endpointClass);
	}
	
}
