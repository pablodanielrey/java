package ar.com.dcsys.gwt.ws.server;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Configura un websocket para todas las llamadas a la misma url.
 * hace el binding de la session http ya que es usada posteriormente.
 * @author pablo
 *
 */
public class WebsocketsConfigurator extends ServerEndpointConfig.Configurator {

	private static Websockets ws;
	
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		
		// guardo la http session dentro de las propiedades del usuario.
		HttpSession session = (HttpSession)request.getHttpSession();
		sec.getUserProperties().put(HttpSession.class.getName(), session);
		
		super.modifyHandshake(sec, request, response);
	}
	
	
	/*
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
	*/
	
}
