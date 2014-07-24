package ar.com.dcsys.model.device;

import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class GenericWebsocketClient {

	private static final Logger logger = Logger.getLogger(GenericWebsocketClient.class.getName());
	
	public interface WebsocketClient {
		public void onOpen(Session s, EndpointConfig config);
		public void onMessage(String m, Session s);
		public void onClose(Session s, CloseReason reason);
	}
	
	private final WebsocketClient wc;
	
	public GenericWebsocketClient(WebsocketClient wc) {
		this.wc = wc;
	}
	
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.fine("onOpen");
		wc.onOpen(session, config);
	}
	
	@OnMessage
	public void onMessage(String m, Session session) {
		logger.fine("onMessage");
		wc.onMessage(m, session);
	}
	
	@OnClose
	public void onClose(Session s, CloseReason reason) {
		logger.fine("onClose");
		wc.onClose(s, reason);
	}
	
}
