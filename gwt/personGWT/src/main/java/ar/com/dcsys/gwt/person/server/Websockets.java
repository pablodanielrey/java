package ar.com.dcsys.gwt.person.server;

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
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.log(Level.INFO,"onOpen");
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("onClose - ").append(reason.getCloseCode().getCode()).append(" - ").append(reason.getReasonPhrase());
		logger.log(Level.INFO,sb.toString());
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		logger.log(Level.WARNING,error.getMessage(),error);
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		logger.log(Level.INFO,"Msg : " + msg);
	}
}

