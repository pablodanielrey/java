package ar.com.dcsys.gwt.person.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/websockets")
public class Websockets {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());

	private Map<String,Session> sessions = new HashMap<>();
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.log(Level.INFO,"onOpen");

		Session s = sessions.get(session.getId());
		if (s == null) {
			sessions.put(session.getId(), session);
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("onClose - ").append(reason.getCloseCode().getCode()).append(" - ").append(reason.getReasonPhrase());
		logger.log(Level.INFO,sb.toString());
		
		sessions.remove(session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		logger.log(Level.WARNING,error.getMessage(),error);
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		logger.log(Level.INFO,"Msg : " + msg);

 		for (Session s1 : sessions.values()) {
			if (s1.isOpen()) {
				try {
					s1.getBasicRemote().sendText("YEA PEPE");
				} catch (IOException e) {
					logger.log(Level.WARNING,e.getMessage(),e);
				}
			}
		}
	}
	
	
}

