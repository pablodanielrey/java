package ar.com.dcsys.model.device;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import ar.com.dcsys.security.Fingerprint;

@ClientEndpoint
public class EnrollWebsocketClient {

	public static final Logger logger = Logger.getLogger(EnrollWebsocketClient.class.getName());
	
	private final String personId;
	private final EnrollManager enrollManager;
	
	public EnrollWebsocketClient(String personId, EnrollManager em) {
		this.personId = personId;
		this.enrollManager = em;
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.fine("EnrollWebsocketClient - onOpen");
		
		try {
			session.getBasicRemote().sendText("enroll;" + personId);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			enrollManager.onMessage(EnrollAction.ERROR);
		}
		
	}
	
	@OnMessage
	public void onMessage(String m, Session session) {
		
		logger.fine("Mensaje recibido : " + m);
		
		EnrollAction action = null;
		if (m.contains("primera")) {
			
			action = EnrollAction.NEED_FIRST_SWEEP;
			
		} else if (m.contains("segunda")) {
			
			action = EnrollAction.NEED_SECOND_SWEEP;
			
		} else if (m.contains("tercera")) {
			
			action = EnrollAction.NEED_THIRD_SWEEP;
			
		} else if (m.contains("timeout")) {
			
			action = EnrollAction.TIMEOUT;
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
		} else if (m.contains("cancelado")) {
			
			action = EnrollAction.CANCELED;
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
		} else if (m.contains("levantar")) {
			
			action = EnrollAction.RELEASE;
			
		} else if (m.contains("calidad")) {
			
			action = EnrollAction.BAD;			
			
		} else {
			
			Fingerprint fp = new Fingerprint();
			fp.setTemplate(m.getBytes());
			enrollManager.onSuccess(fp);
			
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		enrollManager.onMessage(action);
	}
	
	@OnClose
	public void onClose(Session s, CloseReason reason) {
		logger.fine("EnrollWebsocketClient - onClose");
	}
	
}
